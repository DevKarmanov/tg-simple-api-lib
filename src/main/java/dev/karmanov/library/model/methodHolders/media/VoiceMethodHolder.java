package dev.karmanov.library.model.methodHolders.media;

import dev.karmanov.library.model.methodHolders.abstractHolders.OrderedActionMethodHolder;

import java.lang.reflect.Method;

public class VoiceMethodHolder extends OrderedActionMethodHolder {
    private int maxDurationSeconds;
    private int minDurationSeconds;
    private boolean voiceInterpreter;
    private String languageCode;
    private String regex;

    public VoiceMethodHolder(Method method, String actionName, int order, int maxDurationSeconds, int minDurationSeconds, boolean voiceInterpreter, String languageCode, String regex) {
        super(method, actionName, order);
        this.maxDurationSeconds = maxDurationSeconds;
        this.minDurationSeconds = minDurationSeconds;
        this.voiceInterpreter = voiceInterpreter;
        this.languageCode = languageCode;
        this.regex = regex;
    }

    public int getMaxDurationSeconds() {
        return maxDurationSeconds;
    }

    public void setMaxDurationSeconds(int maxDurationSeconds) {
        this.maxDurationSeconds = maxDurationSeconds;
    }

    public int getMinDurationSeconds() {
        return minDurationSeconds;
    }

    public void setMinDurationSeconds(int minDurationSeconds) {
        this.minDurationSeconds = minDurationSeconds;
    }

    public boolean isVoiceInterpreter() {
        return voiceInterpreter;
    }

    public void setVoiceInterpreter(boolean voiceInterpreter) {
        this.voiceInterpreter = voiceInterpreter;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
