package dev.karmanov.library.model.methodHolders.abstractHolders;

import java.lang.reflect.Method;

public abstract class ActionBaseMethodHolder extends BaseMethodHolder {
    private String actionName;

    public ActionBaseMethodHolder(Method method, String actionName) {
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
