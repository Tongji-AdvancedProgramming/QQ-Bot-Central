package org.tongji.programming;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import org.tongji.programming.base.IniRefreshManager;
import org.tongji.programming.config.ConfigProviderConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置提供
 */
@Slf4j
@Component
public class ConfigProvider {
    JedisPool jedisPool;
    Jedis jedis = null;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    ConfigProviderConfiguration configuration;

    @Autowired
    public void setConfiguration(ConfigProviderConfiguration configuration) {
        this.configuration = configuration;
    }

    IniRefreshManager iniRefreshManager;

    @Autowired
    public void setIniRefreshManager(IniRefreshManager iniRefreshManager) {
        this.iniRefreshManager = iniRefreshManager;
    }

    /**
     * 刷新配置项内容
     */
    public void refreshData() {
        var iniPath = configuration.getFile();

        if (iniPath == null) {
            throw new RuntimeException("必须提供配置项的位置（位于配置项bot-config.file）");
        }

        var db = configuration.getRedis().getDb();

        iniRefreshManager.refreshConfig(iniPath, db);
    }

    @PostConstruct
    public void init() {
        refreshData();
    }

    /**
     * 将文本形式的配置项转换为值（一般人用不到）
     * @param value 配置项文本
     * @param clazz 类
     * @return 值
     * @param <T> 不是基础类型的类
     */
    public static <T> T convertValueToClass(String value, Class<T> clazz){
        if (clazz == String.class)
            return clazz.cast(value);
        else if (clazz == Integer.class || clazz == int.class)
            return clazz.cast(Integer.parseInt(value));
        else if (clazz == Long.class || clazz == long.class)
            return clazz.cast(Long.parseLong(value));
        else if (clazz == Short.class || clazz == short.class)
            return clazz.cast(Short.parseShort(value));
        else if (clazz == Float.class || clazz == float.class)
            return clazz.cast(Float.parseFloat(value));
        else if (clazz == Double.class || clazz == double.class)
            return clazz.cast(Double.parseDouble(value));
        else if (clazz == Boolean.class || clazz == boolean.class)
            return clazz.cast(Boolean.parseBoolean(value));
        else if (clazz == Date.class)
            return clazz.cast(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").parse(value));
        else
            throw new RuntimeException(String.format("类型%s不支持读取和转换QAQ", clazz.getName()));
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
     * @return 配置内容（若不存在则为null，如果传入的是基本类型会NullPointerError）
     */
    public <T> T get(String sectionName, String fieldName, Class<T> clazz) {
        if (jedis == null) {
            jedis = jedisPool.getResource();
            jedis.select(configuration.getRedis().getDb());
        }

        var result = jedis.hget(sectionName, fieldName);
        //System.err.println(fieldName+":"+result);

        if (result == null) return null;

        return convertValueToClass(result,clazz);
    }

    /**
     * 获取上一次更新配置文件的时间
     * @return 获取上一次更新配置文件的时间
     */
    public long getUpdateTime(){
        if (jedis == null) {
            jedis = jedisPool.getResource();
            jedis.select(configuration.getRedis().getDb());
        }

        var result = jedis.get("conf-lastUpdate");
        return Long.parseLong(result);
    }
}
