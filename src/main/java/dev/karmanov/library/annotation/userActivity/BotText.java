package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface BotText {
    String actionName() default "start-command-method";
    String text() default "/start";
    int order() default Integer.MAX_VALUE;
    boolean isRegex() default false;
}



