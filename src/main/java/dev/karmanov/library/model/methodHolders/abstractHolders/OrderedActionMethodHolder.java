package dev.karmanov.library.model.methodHolders.abstractHolders;

import java.lang.reflect.Method;

public abstract class OrderedActionMethodHolder extends ActionBaseMethodHolder {
    private int order;

    public OrderedActionMethodHolder(Method method, String actionName, int order) {
        super(method, actionName);
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
