package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotVoice {
    String actionName();
    int maxDurationSeconds() default 60;
    int minDurationSeconds() default 5;

    int order() default Integer.MAX_VALUE;
}
