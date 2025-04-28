package dev.karmanov.library.service.register;

import dev.karmanov.library.config.TgSimpleApiConfig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * {@link ImportSelector} implementation that registers the {@link TgSimpleApiConfig}
 * configuration class for the TgSimpleApi library when {@code @EnableTgSimpleApi} is used.
 * <p>
 * This class is responsible for importing the necessary configuration into the application
 * context, ensuring that the required beans are available for the functionality of TgSimpleApi.
 * </p>
 */
public class TgSimpleApiRegistrar implements ImportSelector {
    @NonNull
    @Override
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        return new String[]{"dev.karmanov.library.config.TgSimpleApiConfig"};
    }
}
