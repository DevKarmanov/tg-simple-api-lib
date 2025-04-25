package dev.karmanov.library.model.methodHolders.media;

import dev.karmanov.library.model.methodHolders.abstractHolders.OrderedActionMethodHolder;

import java.lang.reflect.Method;

public class VoiceMethodHolder extends OrderedActionMethodHolder {
    private int maxDurationSeconds;
    private int minDurationSeconds;

    public VoiceMethodHolder(Method method, String actionName, int order, int maxDurationSeconds, int minDurationSeconds) {
        super(method, actionName, order);
        this.maxDurationSeconds = maxDurationSeconds;
        this.minDurationSeconds = minDurationSeconds;
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
}
