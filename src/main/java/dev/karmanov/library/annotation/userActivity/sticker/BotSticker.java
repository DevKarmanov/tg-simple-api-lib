package dev.karmanov.library.annotation.userActivity.sticker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotSticker {
    String actionName();
    String[] emoji() default {};
    String[] packageName() default {};
    StickerType[] type() default {};
    boolean customEmoji() default false;
    String[] fileUniqueId() default {};
    int minWidth() default -1;
    int minHeight() default -1;
    int maxWidth() default -1;
    int maxHeight() default -1;
    int order() default Integer.MAX_VALUE;
}
