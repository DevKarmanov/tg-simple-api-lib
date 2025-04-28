package dev.karmanov.library.service.register.executor;

import dev.karmanov.library.service.register.BotCommandRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Default implementation of the {@link Executor} interface for executing methods dynamically.
 * <p>
 * This class uses a {@link BotCommandRegister} to retrieve the appropriate bean and its method, and then
 * invokes the method using reflection. It provides logging for both successful executions and errors.
 * </p>
 */
public class DefaultMethodExecutor implements Executor {
    private BotCommandRegister register;

    @Autowired(required = false)
    public void setRegister(BotCommandRegister register){
        this.register = register;
    }
    private static final Logger logger = LoggerFactory.getLogger(DefaultMethodExecutor.class);

    /**
     * Executes the specified method on the corresponding bean.
     * <p>
     * This method looks up the bean associated with the method using the {@link BotCommandRegister},
     * retrieves the method via reflection, and then invokes it with the provided arguments.
     * If the method executes successfully, a success log is generated. If an error occurs, it is logged as well.
     * </p>
     *
     * @param method the {@link Method} object representing the method to be executed.
     * @param args the arguments to be passed to the method during invocation.
     */
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
