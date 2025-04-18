package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BotCallBack {
    String actionName();
    String[] callbackName();
    boolean isRegex() default false;
    int order() default Integer.MAX_VALUE;
}



