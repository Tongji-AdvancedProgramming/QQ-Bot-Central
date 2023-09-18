package org.tongji.programming;

import com.alibaba.fastjson.JSON;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.tongji.programming.DTO.cqhttp.APIResponse;
import org.tongji.programming.helper.ListHelper;

import java.util.List;

@SpringBootTest(classes = { BackstageServiceProvider.class })
public class TestJson {
    @Test
    void testJsonArray(){
        /* 1. 先生成一个json */
        var json = "{\n" +
                "    \"data\": [1,2,3,4,5],\n" +
                "    \"message\": \"\",\n" +
                "    \"msg\": \"\",\n" +
                "    \"retcode\": 0,\n" +
                "    \"status\": \"\",\n" +
                "    \"wording\": \"测试JSON\"\n" +
                "}";

        /* 2. 先转换成APIResponse */
        var response = JSON.parseObject(json, APIResponse.class);
        System.err.println(response);

        /* 3. 取出里面的Data */
        var data = response.getData();

        /* 4. 把data解析为一个List */
        List<?> listRaw = data.toJavaObject(List.class);

        List<Integer> list = ListHelper.safeConvertToList(listRaw, Integer.class);

        /* 5. 输出结果 */
        System.err.println(list);

    }
}
