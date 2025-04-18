package dev.karmanov.library.annotation.botActivity;

import dev.karmanov.library.service.register.TgSimpleApiRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TgSimpleApiRegistrar.class)
public @interface EnableTgSimpleApi {
}
