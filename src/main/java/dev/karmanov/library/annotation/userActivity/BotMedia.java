package dev.karmanov.library.annotation.userActivity;

import dev.karmanov.library.model.message.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BotMedia {
     String actionName();
     MediaType mediaType();
     int order() default Integer.MAX_VALUE;
}


