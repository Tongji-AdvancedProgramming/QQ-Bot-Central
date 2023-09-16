package org.tongji.programming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest(classes = { BackstageServiceProvider.class })
public class TestJedis {

    JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Test
    public void testJedisConn(){
        // 获取Jedis连接
        Jedis jedis = jedisPool.getResource();

        // 选择1号数据库（默认共有15个）
        jedis.select(1);

        // 设置test-date的值
        jedis.set("test-date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 读取test-date的值
        String date = jedis.get("test-date");
        System.err.printf("Test-date is %s!%n", date);

        // 关闭连接
        jedis.close();
    }
}
