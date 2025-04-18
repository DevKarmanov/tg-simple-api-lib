package dev.karmanov.library.model.methodHolders;

import java.lang.reflect.Method;

public abstract class ActionMethodHolder extends MethodHolder{
    private String actionName;

    public ActionMethodHolder(Method method, String actionName) {
        super(method);
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
