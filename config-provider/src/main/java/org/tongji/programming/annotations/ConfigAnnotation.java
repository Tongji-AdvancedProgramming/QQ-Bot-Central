package org.tongji.programming.annotations;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tongji.programming.ConfigProvider;
import org.tongji.programming.base.IniRefreshManager;
import org.tongji.programming.config.ConfigProviderConfiguration;

import javax.annotation.PostConstruct;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ConfigAnnotation implements ApplicationContextAware, BeanPostProcessor {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    ConfigProvider configProvider;

    @Autowired
    public void setConfigProvider(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    @PostConstruct
    void prepareConfigValues() {
    }

    Map<String, Object> configBeans = new HashMap<>();
    long lastUpdateConfig = 0;

    private void configDataBind(@NonNull Object bean, String beanName) throws IllegalAccessException {
        Class<?> targetClass = bean.getClass();
        BotConfig config = targetClass.getAnnotation(BotConfig.class);
        var sectionName = config.value();

        for (Field field : targetClass.getDeclaredFields()) {
            var fieldName = field.getName();
            var clazz = field.getType();
            if (clazz.isPrimitive()) {
                log.error("在Bean {} 中发现了基本类型，问题字段为{}，类型是{}。使用基本类型会导致处理未填写的情况抛出异常，因此请使用包装类型。",
                        beanName, fieldName, clazz.getName());
                throw new RuntimeException(String.format("在Bean %s中发现了基本类型，正在终止初始化/数据绑定。", beanName));
            }

            var value = configProvider.get(sectionName, fieldName, clazz);
            if (value == null) {
                // 尝试获取默认值
                if (field.isAnnotationPresent(DefaultValue.class)) {
                    DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
                    value = ConfigProvider.convertValueToClass(defaultValue.value(), clazz);
                } else {
                    log.error("在Bean {} 中出现了未在配置文件中出现，且未指定默认值的字段，问题字段为{}，类型是{}。",
                            beanName, fieldName, clazz.getName());
                    throw new RuntimeException(String.format("在Bean %s中发现了未在配置文件中出现且无默认值的字段%s，正在终止初始化/数据绑定。", beanName, fieldName));
                }
            }
            field.setAccessible(true);
            field.set(bean, value);
        }

        lastUpdateConfig = new Date().getTime();
    }

    /**
     * 刷新配置数据（定时任务）
     */
    @Scheduled(fixedRate = 10000)
    public void refreshConfigData() {
        var updateTime = configProvider.getUpdateTime();
        if (updateTime > lastUpdateConfig) {
            log.info("检测到配置文件发生变化，正在重载配置项。");
            configBeans.forEach((s, o) -> {
                try {
                    configDataBind(o, s);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("绑定配置属性失败", e);
                }
            });
            lastUpdateConfig = new Date().getTime();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        if (targetClass.isAnnotationPresent(BotConfig.class)) {
            configBeans.put(beanName, bean);
            try {
                configDataBind(bean, beanName);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("绑定配置属性失败", e);
            }
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BotConfig {
        String value() default "";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefaultValue {
        String value() default "";
    }
}
