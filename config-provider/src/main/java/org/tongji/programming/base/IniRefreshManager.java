package org.tongji.programming.base;

import lombok.var;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class IniRefreshManager {

    JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    Jedis jedis = null;

    public void refreshConfig(String iniPath, int db){
        if (jedis == null) {
            jedis = jedisPool.getResource();
            jedis.select(db);
        }

        var configFile = new File(iniPath);
        var lastModified = configFile.lastModified();
        var lastUpdate = jedis.get("conf-lastUpdate");
        if(lastUpdate!=null){
            if(Long.parseLong(lastUpdate)>lastModified){
                return;
            }
        }

        Wini ini;
        try {
            ini = new Wini(configFile);
        } catch (InvalidFileFormatException e) {
            throw new RuntimeException("提供的配置文件格式不正确：" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ini.forEach(((s, section) -> {
            Map<String, String> sectionMap = new HashMap<>(section);
            jedis.hset(s, sectionMap);
        }));

        jedis.set("conf-lastUpdate", String.valueOf(lastModified));
    }
}
