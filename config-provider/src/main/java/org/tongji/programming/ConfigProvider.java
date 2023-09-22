package org.tongji.programming;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tongji.programming.config.ConfigProviderConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ConfigProvider {
    JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    ConfigProviderConfiguration configuration;

    @Autowired
    public void setConfiguration(ConfigProviderConfiguration configuration) {
        this.configuration = configuration;
    }

    Jedis jedis = null;

    /**
     * 刷新配置项内容
     */
    public void refreshData() {
        var iniPath = configuration.getFile();

        if (iniPath == null) {
            throw new RuntimeException("必须提供配置项的位置（位于配置项bot-config.file）");
        }

        Wini ini;
        try {
            ini = new Wini(new File(iniPath));
        } catch (InvalidFileFormatException e) {
            throw new RuntimeException("提供的配置文件格式不正确：" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (jedis == null) {
            jedis = jedisPool.getResource();
            jedis.select(configuration.getRedis().getDb());
        }

        ini.forEach(((s, section) -> {
            Map<String, String> sectionMap = new HashMap<>(section);
            jedis.hset(s, sectionMap);
        }));
    }

    @PostConstruct
    public void init() {
        refreshData();
    }

    /**
     * 读取配置项（超快的）
     *
     * @param sectionName 配置类名称
     * @param fieldName   配置项名称
     * @return 配置内容（若不存在则为null）
     */
    public String get(String sectionName, String fieldName) {
        if (jedis == null) {
            jedis = jedisPool.getResource();
            jedis.select(configuration.getRedis().getDb());
        }

        return jedis.hget(sectionName, fieldName);
    }

    /**
     * 读取配置项（超快的）
     *
     * @param sectionName 配置类名称
     * @param fieldName   配置项名称
     * @param clazz       类型
     * @return 配置内容（若不存在则为null）
     */
    public <T extends Serializable> T get(String sectionName, String fieldName, Class<T> clazz) {
        if (jedis == null) {
            jedis = jedisPool.getResource();
            jedis.select(configuration.getRedis().getDb());
        }

        var result = jedis.hget(sectionName, fieldName);
        if (clazz == String.class)
            return clazz.cast(result);
        else if (clazz == Integer.class || clazz == int.class)
            return clazz.cast(Integer.parseInt(result));
        else if (clazz == Double.class || clazz == double.class)
            return clazz.cast(Double.parseDouble(result));
        else if (clazz == Boolean.class || clazz == boolean.class)
            return clazz.cast(Boolean.parseBoolean(result));

        return JSON.parseObject(result, clazz);
    }
}
