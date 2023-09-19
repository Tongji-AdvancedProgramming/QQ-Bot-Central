package org.tongji.programming.http;

import com.dtflys.forest.annotation.DataFile;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.extensions.DownloadFile;

import java.io.InputStream;

public interface ExcelXlsToXlsxService {
    @Post(url = "https://service-0thnkbn3-1305284863.sh.apigw.tencentcs.com/release/conv")
    InputStream  convertXlsToXlsx(@DataFile(value = "file", fileName = "upload.xls", partContentType = "application/vnd.ms-excel") InputStream in);

}
