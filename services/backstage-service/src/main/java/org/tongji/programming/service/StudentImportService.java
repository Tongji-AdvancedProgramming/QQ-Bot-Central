package org.tongji.programming.service;

import org.springframework.web.multipart.MultipartFile;
import org.tongji.programming.dto.APIResponse;
import org.tongji.programming.dto.StudentImportService.StudentImportResult;

import java.io.IOException;
import java.io.InputStream;

/**
 * 服务类：导入学生
 */
public interface StudentImportService {
    StudentImportResult resolvePlainText(InputStream fileStream, String course, String classId) throws IOException, RuntimeException;

    /**
     * 使用resolveExcel作为替代。
     */
    @Deprecated
    int resolveCsv(InputStream fileStream) throws IOException;

    StudentImportResult resolveExcel(InputStream fileStream) throws IOException, InterruptedException;

    /**
     * 执行导入操作
     */
    APIResponse performResolvedOperation(StudentImportResult operation);
}
