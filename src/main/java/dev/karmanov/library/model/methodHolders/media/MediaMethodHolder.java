package dev.karmanov.library.model.methodHolders.media;

import dev.karmanov.library.model.message.MediaType;
import dev.karmanov.library.model.methodHolders.abstractHolders.OrderedActionMethodHolder;

import java.lang.reflect.Method;

public class MediaMethodHolder extends OrderedActionMethodHolder {
    private MediaType mediaType;
    private int order;

    public MediaMethodHolder(Method method, String actionName, MediaType mediaType, int order) {
        super(method, actionName,order);
        this.mediaType = mediaType;
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
