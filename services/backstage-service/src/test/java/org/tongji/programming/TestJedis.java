package org.tongji.programming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

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

        // 查看test-date是否存在
        boolean exists = jedis.exists("test-date");
        System.err.println(exists);

        // 设置test-date的值
        jedis.set("test-date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 读取test-date的值
        String date = jedis.get("test-date");
        System.err.printf("Test-date is %s!%n", date);

        // 设置一个在三分钟后过期的值
        jedis.set("test-date-3min", date, SetParams.setParams().ex(180L)); // 也就是180秒，请记得加数字后面的L

        // 关闭连接
        jedis.close();
    }
}
