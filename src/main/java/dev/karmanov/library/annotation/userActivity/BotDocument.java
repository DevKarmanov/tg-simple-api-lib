package dev.karmanov.library.annotation.userActivity;

import dev.karmanov.library.model.message.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotDocument {
    String actionName();
    String[] fileExtensions() default {"doc","docx","pdf"};
    long minFileSize() default 0;
    long maxFileSize() default Long.MAX_VALUE;
    String fileNameRegex();

    int order() default Integer.MAX_VALUE;
}
