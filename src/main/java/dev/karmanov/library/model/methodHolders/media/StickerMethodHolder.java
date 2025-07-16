package dev.karmanov.library.model.methodHolders.media;

import dev.karmanov.library.annotation.userActivity.sticker.StickerType;
import dev.karmanov.library.model.methodHolders.abstractHolders.OrderedActionMethodHolder;

import java.lang.reflect.Method;

public class StickerMethodHolder extends OrderedActionMethodHolder {
    private final String[] emoji;
    private final String[] packageName;
    private final StickerType[] type;
    private final boolean customEmoji;
    private final String[] fileUniqueId;
    private final int minWidth;
    private final int minHeight;
    private final int maxWidth;
    private final int maxHeight;

    public StickerMethodHolder(Method method, String actionName, int order, String[] emoji, String[] packageName, StickerType[] type, boolean customEmoji, String[] fileUniqueId, int minWidth, int minHeight, int maxWidth, int maxHeight) {
        super(method, actionName, order);
        this.emoji = emoji;
        this.packageName = packageName;
        this.type = type;
        this.customEmoji = customEmoji;
        this.fileUniqueId = fileUniqueId;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public String[] getEmoji() {
        return emoji;
    }

    public String[] getPackageName() {
        return packageName;
    }

    public StickerType[] getType() {
        return type;
    }

    public boolean isCustomEmoji() {
        return customEmoji;
    }

    public String[] getFileUniqueId() {
        return fileUniqueId;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }
}
