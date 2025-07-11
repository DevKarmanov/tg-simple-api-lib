package dev.karmanov.library.model.methodHolders.media;

import dev.karmanov.library.model.methodHolders.abstractHolders.FileBoundMethodHolder;

import java.lang.reflect.Method;

public class VideoMethodHolder extends FileBoundMethodHolder {
    private int minDurationSeconds;
    private int maxDurationSeconds;
    private String minResolution;
    private String maxResolution;
    private String[] format;
    private boolean textInterpreter;
    private String languageCode;
    private String regex;

    public VideoMethodHolder(Method method, String actionName, int order, double minFileSize, double maxFileSize, int minDurationSeconds, int maxDurationSeconds, String minResolution, String maxResolution, String[] format, boolean textInterpreter, String languageCode, String regex) {
        super(method, actionName, order, minFileSize, maxFileSize);
        this.minDurationSeconds = minDurationSeconds;
        this.maxDurationSeconds = maxDurationSeconds;
        this.minResolution = minResolution;
        this.maxResolution = maxResolution;
        this.format = format;
        this.textInterpreter = textInterpreter;
        this.languageCode = languageCode;
        this.regex = regex;
    }

    public int getMinDurationSeconds() {
        return minDurationSeconds;
    }

    public void setMinDurationSeconds(int minDurationSeconds) {
        this.minDurationSeconds = minDurationSeconds;
    }

    public int getMaxDurationSeconds() {
        return maxDurationSeconds;
    }

    public void setMaxDurationSeconds(int maxDurationSeconds) {
        this.maxDurationSeconds = maxDurationSeconds;
    }

    public String getMinResolution() {
        return minResolution;
    }

    public void setMinResolution(String minResolution) {
        this.minResolution = minResolution;
    }

    public String getMaxResolution() {
        return maxResolution;
    }

    public void setMaxResolution(String maxResolution) {
        this.maxResolution = maxResolution;
    }

    public String[] getFormat() {
        return format;
    }

    public void setFormat(String[] format) {
        this.format = format;
    }

    public boolean isTextInterpreter() {
        return textInterpreter;
    }

    public void setTextInterpreter(boolean textInterpreter) {
        this.textInterpreter = textInterpreter;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
