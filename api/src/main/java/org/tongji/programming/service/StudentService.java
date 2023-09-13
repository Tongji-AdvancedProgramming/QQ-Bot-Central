package org.tongji.programming.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.tongji.programming.pojo.Student;

import java.util.List;

/**
 * 学生服务
 */
@DubboService
public interface StudentService {

    /**
     * 查询学生是否有效，也就是信息正确、属于本班
     * @return 0:OK；1:学生正确，信息错误；2:学生不正确
     */
    public int StudentValid(Student student);

}
