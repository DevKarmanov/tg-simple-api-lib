package dev.karmanov.library.service.register.executor;

import java.lang.reflect.Method;

public interface Executor {
    void executeMethod(Method method, Object... args);
}
