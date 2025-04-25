package dev.karmanov.library.model.methodHolders.media;

import dev.karmanov.library.model.methodHolders.abstractHolders.FileBoundMethodHolder;

import java.lang.reflect.Method;

public class PhotoMethodHolder extends FileBoundMethodHolder {
    private double minFileSize;
    private double maxFileSize;
    private int order;
    private int minWidth;
    private int minHeight;
    private String aspectRatio;
    private String format;

    public PhotoMethodHolder(Method method, String actionName, double minFileSize, double maxFileSize, int order, int minWidth, int minHeight, String aspectRatio, String format) {
        super(method, actionName,order,minFileSize,maxFileSize);
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.aspectRatio = aspectRatio;
        this.format = format;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
