package dev.karmanov.library.service.register.executor;

import java.lang.reflect.Method;

/**
 * Interface for executing methods dynamically.
 * <p>
 * This interface defines a contract for executing a method on a given object with the specified arguments.
 * It is intended to be implemented by classes responsible for invoking methods dynamically at runtime.
 * </p>
 */
public interface Executor {
    /**
     * Executes a target method with the specified arguments.
     * <p>
     * This method allows for dynamically invoking a method on a given object.
     * </p>
     *
     * @param method the {@link Method} object representing the method to be executed.
     * @param args the arguments to be passed to the method during invocation.
     */
    void executeMethod(Method method, Long chatId, Object... args);
    void executeMethod(Method method, Object... args);
}
