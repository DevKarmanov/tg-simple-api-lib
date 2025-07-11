package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotVideo {
    String actionName();
    int minDurationSeconds() default  5;
    int maxDurationSeconds() default  300;
    String minResolution() default  "";
    String maxResolution() default  "";
    long minFileSize() default 100;
    long maxFileSize() default 100_000;
    String[] format() default {"mp4", "mov"};
    boolean textInterpreter() default false;
    String languageCode() default "en";
    String regex() default ".*";
    int order() default 1;
}
