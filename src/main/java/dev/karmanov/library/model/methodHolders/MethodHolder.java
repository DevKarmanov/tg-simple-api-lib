package dev.karmanov.library.model.methodHolders;

import java.lang.reflect.Method;

public abstract class MethodHolder {
    private Method method;

    public MethodHolder(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
