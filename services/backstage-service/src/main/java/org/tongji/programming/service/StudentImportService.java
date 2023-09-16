package org.tongji.programming.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * 服务类：导入学生
 */
public interface StudentImportService {
    int resolvePlainText(InputStream fileStream, String course, String classId) throws IOException, RuntimeException;

    public int resolveCsv(InputStream fileStream) throws IOException;
}
