package org.tongji.programming.service.impl;

import com.alibaba.fastjson2.JSON;
import com.csvreader.CsvReader;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.dto.StudentImportService.StudentImportExcelClass;
import org.tongji.programming.dto.StudentImportService.StudentImportResult;
import org.tongji.programming.helper.DirectoryDeleter;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.mapper.CourseMapper;
import org.tongji.programming.mapper.StudentLogMapper;
import org.tongji.programming.mapper.StudentMapper;
import org.tongji.programming.pojo.Student;
import org.tongji.programming.service.StudentImportService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StudentImportServiceImpl implements StudentImportService {

    StudentMapper studentMapper;

    CourseMapper courseMapper;

    StudentLogMapper studentLogMapper;

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Autowired
    public void setCourseMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setStudentLogMapper(StudentLogMapper studentLogMapper) {
        this.studentLogMapper = studentLogMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentImportResult resolvePlainText(InputStream fileStream, String course, String classId) throws IOException, RuntimeException {
        var reader = new BufferedReader(new InputStreamReader(fileStream));
        var pattern = Pattern.compile("(\\d{7})\\s(.*)");

        var studentsForImport = new LinkedList<Student>();

        while (reader.ready()) {
            var line = reader.readLine();
            line = line.trim();

            var matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                throw new RuntimeException(String.format("解析行“%s”时遇到异常：匹配失败", line));
            }

            var stuNo = matcher.group(1);
            var name = matcher.group(2);

            var student = Student.builder().stuNo(stuNo).name(name).major("").courseId(course).classId(classId).build();
            studentsForImport.add(student);
        }

        var students = studentMapper.selectByCourseClass(course,classId);
        Map<String, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getStuNo, student -> student));

        List<Student> insertList = new LinkedList<>();
        List<Student> updateList = new LinkedList<>();
        List<Student> deleteList;

        studentsForImport.forEach(student -> {
            // 寻找数据库中的记录
            var stuId = student.getStuNo();
            var studentInDb = studentMap.get(stuId);
            if (studentInDb == null) {
                // 若无记录，则插入到数据库
                studentInDb = Student.builder()
                        .stuNo(stuId).name(student.getName()).major(student.getMajor())
                        .courseId(course).classId(classId)
                        .build();
                insertList.add(studentInDb);
            }else{
                // 有记录，检查是否需要更新
                if(!student.equals(studentInDb)){
                    studentInDb = Student.builder()
                            .stuNo(stuId).name(student.getName()).major(student.getMajor())
                            .courseId(course).classId(classId)
                            .build();
                    updateList.add(studentInDb);
                }
            }
            // 将记录从表中删除
            studentMap.remove(stuId);
        });

        deleteList = new ArrayList<>(studentMap.values());

        return StudentImportResult.builder()
                .randomId(RandomStringUtils.randomAlphanumeric(8)).resolvedTime(LocalDateTime.now())
                .resolved(students.size()).repeated(students.size()-insertList.size()-updateList.size()-deleteList.size())
                .deleteList(deleteList).updateList(updateList).insertList(insertList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resolveCsv(InputStream fileStream) throws IOException {
        var reader = new CsvReader(fileStream, StandardCharsets.UTF_8);

        if (!reader.readHeaders()) {
            throw new RuntimeException("导入失败，未检测到表头。");
        }

        var students = new LinkedList<Student>();

        while (reader.readRecord()) {
            var name = reader.get("姓名");
            var stuNo = reader.get("学号");
            var courseId = reader.get("课号");
            var classId = reader.get("班号");
            var major = reader.get("专业");

            var student = Student.builder().major(major).classId(classId).classId(classId)
                    .courseId(courseId).stuNo(stuNo).name(name).build();
            students.add(student);
        }

        try {
            return studentMapper.insertStudents(students);
        } catch (Exception e) {
            throw new RuntimeException("插入失败，请检查是否有重复数据，或课程号是否已导入后台。");
        }
    }

    private static final Path tempPath;

    static {
        tempPath = Paths.get(System.getProperty("java.io.tmpdir"), "bot_backstage");
    }

    /**
     * 导入Excel是最难也最复杂的部分，Excel是同济大学点名册，假定点名册内容绝对正确。
     */
    @Override
    public StudentImportResult resolveExcel(InputStream fileStream) throws IOException, InterruptedException {
        var tempFileDir = Paths.get(tempPath.toAbsolutePath().toString(), RandomStringUtils.randomAlphanumeric(6));
        var tempFilePath = Paths.get(tempFileDir.toString(), "workbook.xlsx");
        Files.createDirectories(tempFileDir);
        Files.copy(fileStream, tempFilePath);
        fileStream.close();

        var command = String.format("tongji-roster-resolver -input %s", tempFilePath.toAbsolutePath());
        Process proc = Runtime.getRuntime().exec(command);

        StringBuilder output = new StringBuilder();
        Thread threadOutStream = new Thread(() -> {
            BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            char[] buffer = new char[1024];
            int bytesRead;

            try {
                while ((bytesRead = stdout.read(buffer)) != -1) {
                    output.append(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        threadOutStream.start();

        Thread threadErrStream = new Thread(() -> {
            BufferedReader stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            try {
                while (stderr.ready()) {
                    log.error(stderr.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        threadErrStream.start();

        int exitCode = proc.waitFor();
        threadOutStream.join();
        threadErrStream.join();

        System.err.printf("Finish With %d%n", exitCode);
        DirectoryDeleter.deleteDirectoryRecursively(tempFileDir);

        if (exitCode != 0)  {
            log.error("外部Go程序执行失败：");
            throw new RuntimeException("导入失败，解析程序发生运行时错误，请您检查是否传入了xlsx文档，并且文档可以正常打开，没有错误。");
        }

        var execResult = output.toString();

        var mapper = JSONHelper.getLossyMapper();
        var classInfo = mapper.readValue(execResult, StudentImportExcelClass.class);

        var course = courseMapper.selectById(classInfo.getCode());
        if (course == null) {
            throw new RuntimeException("导入失败，数据库中还没有添加这门课，请您先添加课程再导入。");
        }

        List<Student> students = studentMapper.selectByCourseClass(classInfo.getCode(),classInfo.getClazz());
        Map<String, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getStuNo, student -> student));

        List<Student> insertList = new LinkedList<>();
        List<Student> updateList = new LinkedList<>();
        List<Student> deleteList;

        classInfo.getStudents().forEach(student -> {
            // 寻找数据库中的记录
            var stuId = String.valueOf(student.getStudentId());
            var studentInDb = studentMap.get(stuId);
            if (studentInDb == null) {
                // 若无记录，则插入到数据库
                studentInDb = Student.builder()
                        .stuNo(stuId).name(student.getName()).major(student.getMajor())
                        .courseId(classInfo.getCode()).classId(classInfo.getClazz())
                        .build();
                insertList.add(studentInDb);
            }else{
                // 有记录，检查是否需要更新
                if(!student.isSame(studentInDb)){
                    studentInDb = Student.builder()
                            .stuNo(stuId).name(student.getName()).major(student.getMajor())
                            .courseId(classInfo.getCode()).classId(classInfo.getClazz())
                            .build();
                    updateList.add(studentInDb);
                }
            }
            // 将记录从表中删除
            studentMap.remove(stuId);
        });

        deleteList = new ArrayList<>(studentMap.values());

        return StudentImportResult.builder()
                .randomId(RandomStringUtils.randomAlphanumeric(8)).resolvedTime(LocalDateTime.now())
                .resolved(students.size()).repeated(students.size()-updateList.size()-deleteList.size())
                .deleteList(deleteList).updateList(updateList).insertList(insertList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse performResolvedOperation(StudentImportResult operation) {
        // 执行插入操作
        studentMapper.insertStudents(operation.getInsertList());

        // 执行更新操作
        operation.getUpdateList().forEach(student -> {
            studentMapper.updateById(student);
        });

        // 执行删除操作
        operation.getDeleteList().forEach(student -> {
            studentMapper.deleteById(student.getStuNo(),student.getCourseId());
        });

        // 备份本次操作到日志
        var jsonString = JSON.toJSON(operation).toString();
        studentLogMapper.addLog(jsonString);

        return APIResponse.Success();
    }
}
