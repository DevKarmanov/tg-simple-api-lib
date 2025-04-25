package dev.karmanov.library.model.methodHolders.abstractHolders;

import java.lang.reflect.Method;

public abstract class BaseMethodHolder {
    private Method method;

    public BaseMethodHolder(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
