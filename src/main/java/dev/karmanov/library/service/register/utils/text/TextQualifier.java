package dev.karmanov.library.service.register.utils.text;

import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.TextMethodHolder;

public interface TextQualifier {
    boolean textTypeCheck(TextMethodHolder textHolder, String text, TextType textType);
}
