package dev.karmanov.library.model.methodHolders.media;

import dev.karmanov.library.model.methodHolders.abstractHolders.FileBoundMethodHolder;

import java.lang.reflect.Method;

public class DocumentMethodHolder extends FileBoundMethodHolder {
    private String[] fileExtensions;
    private String fileNameRegex;

    public DocumentMethodHolder(Method method, String actionName, int order, double minFileSize, double maxFileSize, String[] fileExtensions, String fileNameRegex) {
        super(method, actionName, order, minFileSize, maxFileSize);
        this.fileExtensions = fileExtensions;
        this.fileNameRegex = fileNameRegex;
    }

    public String[] getFileExtensions() {
        return fileExtensions;
    }

    public void setFileExtensions(String[] fileExtensions) {
        this.fileExtensions = fileExtensions;
    }

    public String getFileNameRegex() {
        return fileNameRegex;
    }

    public void setFileNameRegex(String fileNameRegex) {
        this.fileNameRegex = fileNameRegex;
    }
}
