package dev.karmanov.library.model.methodHolders;

import dev.karmanov.library.model.message.MediaType;

import java.lang.reflect.Method;

public class MediaMethodHolder extends ActionMethodHolder {
    private MediaType mediaType;
    private int order;

    public MediaMethodHolder(Method method, String actionName, MediaType mediaType, int order) {
        super(method, actionName);
        this.mediaType = mediaType;
        this.order = order;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
