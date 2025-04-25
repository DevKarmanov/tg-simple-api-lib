package dev.karmanov.library.model.methodHolders.abstractHolders;

import java.lang.reflect.Method;

public abstract class FileBoundMethodHolder extends OrderedActionMethodHolder {
    private double minFileSize;
    private double maxFileSize;

    public FileBoundMethodHolder(Method method, String actionName, int order, double minFileSize, double maxFileSize) {
        super(method, actionName, order);
        this.minFileSize = minFileSize;
        this.maxFileSize = maxFileSize;
    }

    public double getMinFileSize() {
        return minFileSize;
    }

    public void setMinFileSize(double minFileSize) {
        this.minFileSize = minFileSize;
    }

    public double getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(double maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
