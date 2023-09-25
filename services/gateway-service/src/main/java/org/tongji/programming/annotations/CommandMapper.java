package org.tongji.programming.annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.tongji.programming.DTO.cqhttp.MessageUniversalReport;
import org.tongji.programming.enums.GroupLevel;
import org.tongji.programming.helper.JSONHelper;
import org.tongji.programming.service.RestrictLevelService;

import javax.annotation.PostConstruct;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CommandMapper implements ApplicationContextAware {

    private Map<String, Map<String, MappingConfig>> textCommandMap = new HashMap<>();
    private Map<Class<? extends CommandApplicableChecker>, Method> customCommandMap = new HashMap<>();

    final Logger logger = LoggerFactory.getLogger(CommandMapper.class);

    @Autowired
    BeanFactory beanFactory;

    private HashSet<String> beans = new HashSet<>();

    private Map<String, Object> components = new HashMap<>();

    @Autowired
    RestrictLevelService restrictLevelService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Collections.addAll(beans, applicationContext.getBeanDefinitionNames());

        beans.forEach(bean -> {
            Class<?> cls = beanFactory.getType(bean);
            if (cls != null) {
                CommandController controllerPrefix = cls.getAnnotation(CommandController.class);
                if (controllerPrefix != null) {
                    try {
                        Object a = cls.getDeclaredConstructor().newInstance();
                        applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(a, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
                        components.put(bean, a);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        });
    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CommandController {
        String value() default "";
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CommandMapping {
        String value();

        boolean groupChatOnly() default false;

        boolean privateChatOnly() default false;

        GroupLevel minimumGroupLevel() default GroupLevel.NORMAL;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CustomCommandMapping {
        Class<? extends CommandApplicableChecker> value();
    }

    @PostConstruct
    public void register() {

        logger.info("\n   _____ _     _       _    _   ____        _   \n" +
                "  / ____| |   (_)     | |  (_) |  _ \\      | |  \n" +
                " | |    | |__  _  __ _| | ___  | |_) | ___ | |_ \n" +
                " | |    | '_ \\| |/ _` | |/ / | |  _ < / _ \\| __|\n" +
                " | |____| | | | | (_| |   <| | | |_) | (_) | |_ \n" +
                "  \\_____|_| |_|_|\\__,_|_|\\_\\_| |____/ \\___/ \\__|\n" +
                "                                                \n" +
                " By Cinea (Zhang Yao)                             ");

        AtomicInteger handlerCount = new AtomicInteger();

        beans.forEach(bean -> {
            Class<?> cls = beanFactory.getType(bean);
            if (cls != null) {
                CommandController controllerPrefix = cls.getAnnotation(CommandController.class);
                if (controllerPrefix != null) {
                    if (!textCommandMap.containsKey(controllerPrefix.value())) {
                        textCommandMap.put(controllerPrefix.value(), new HashMap<>());
                    }
                    var innerMap = textCommandMap.get(controllerPrefix.value());

                    Method[] methods = cls.getMethods();
                    for (Method method : methods) {
                        CustomCommandMapping customCommandMapping = method.getAnnotation(CustomCommandMapping.class);
                        if (customCommandMapping != null) {
                            customCommandMap.put(customCommandMapping.value(), method);
                            continue;
                        }
                        CommandMapping commandMapping = method.getAnnotation(CommandMapping.class);
                        if (commandMapping == null) {
                            continue;
                        }
                        innerMap.putIfAbsent(commandMapping.value(),
                                MappingConfig.builder()
                                        .groupChatOnly(commandMapping.groupChatOnly())
                                        .privateChatOnly(commandMapping.privateChatOnly())
                                        .minimumGroupLevel(commandMapping.minimumGroupLevel())
                                        .handler(method)
                                        .clazz(bean)
                                        .build());
                        handlerCount.getAndIncrement();
                    }
                }
            }
        });

        logger.info(String.format("Chiaki Bot Framework has found %d handlers!", handlerCount.get()));
    }

    public String handleCommand(MessageUniversalReport event) throws InvocationTargetException, IllegalAccessException, JsonProcessingException, InstantiationException {
        var content = event.getRawMessage();

        for (var key : textCommandMap.keySet()) {
            if (content.startsWith(key)) {
                var innerMap = textCommandMap.get(key);
                for (var subKey : innerMap.keySet()) {
                    if (content.regionMatches(true, key.length() > 0 ? key.length() + 1 : 0, subKey, 0, subKey.length())) {
                        var config = innerMap.get(subKey);
                        if ((config.groupChatOnly && event.getMessageType().equals("private")) || (config.privateChatOnly && event.getMessageType().equals("group"))) {
                            continue;
                        }
                        if (event.getMessageType().equals("group") && !restrictLevelService.restrictTo(config.minimumGroupLevel, event.getGroupId())) {
                            continue;
                        }
                        /* **All OK!** */
                        var handler = config.handler;
                        var instance = components.get(config.clazz);
                        if (handler.getReturnType() == String.class) {
                            return (String) handler.invoke(instance, event);
                        } else {
                            var mapper = JSONHelper.getLossyMapper();
                            return mapper.writeValueAsString(handler.invoke(event));
                        }
                    }
                }
            }
        }

        return "{}";
    }

}

@Builder
class MappingConfig {
    boolean groupChatOnly = false;
    boolean privateChatOnly = false;
    GroupLevel minimumGroupLevel = GroupLevel.NORMAL;
    Method handler;
    String clazz;
}
