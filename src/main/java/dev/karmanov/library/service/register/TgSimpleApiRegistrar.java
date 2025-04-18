package dev.karmanov.library.service.register;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

public class TgSimpleApiRegistrar implements ImportSelector {
    @NonNull
    @Override
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        return new String[]{"dev.karmanov.library.config.TgSimpleApiConfig"};
    }
}
