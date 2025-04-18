package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotPhoto {
    String actionName();
    long minFileSize() default 0;
    long maxFileSize() default Long.MAX_VALUE;
    int order() default Integer.MAX_VALUE;
    int minWidth() default 0;
    int minHeight() default 0;
    String aspectRatio() default "";
    String format() default "";
}

