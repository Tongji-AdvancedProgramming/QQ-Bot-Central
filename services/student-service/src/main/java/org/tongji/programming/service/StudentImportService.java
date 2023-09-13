package org.tongji.programming.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * 服务类：导入学生
 */
public interface StudentImportService {
    public int resolvePlainText(InputStream fileStream) throws IOException;
    public int resolveCsv(InputStream fileStream);
}
