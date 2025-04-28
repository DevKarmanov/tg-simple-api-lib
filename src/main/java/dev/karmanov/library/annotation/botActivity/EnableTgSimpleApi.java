package dev.karmanov.library.annotation.botActivity;

import dev.karmanov.library.service.register.TgSimpleApiRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Annotation to enable the TgSimpleApi library functionality in an application.
 * <p><b>Note:</b> This annotation imports {@link TgSimpleApiRegistrar},
 * which handles the registration of the necessary components.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TgSimpleApiRegistrar.class)
public @interface EnableTgSimpleApi {
}
