package dev.karmanov.library.service.register.executor;

import dev.karmanov.library.service.register.BotCommandRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class DefaultMethodExecutor implements Executor {
    private BotCommandRegister register;

    @Autowired(required = false)
    public void setRegister(BotCommandRegister register){
        this.register = register;
    }
    private static final Logger logger = LoggerFactory.getLogger(DefaultMethodExecutor.class);
    @Override
    public void executeMethod(Method method, Object... args) {
        Object bean = register.getBean(method);
        Method proxyMethod = ReflectionUtils.findMethod(bean.getClass(), method.getName(), method.getParameterTypes());

        logger.info("Executing method: {} in class: {}", proxyMethod.getName(), bean.getClass().getSimpleName());

        try {
            proxyMethod.invoke(bean, args);
            logger.info("Successfully executed method: {}", proxyMethod.getName());
        } catch (Exception e) {
            logger.error("Error executing method: {}. Exception: {}", proxyMethod.getName(), e.getMessage(), e);
        }
    }
}
