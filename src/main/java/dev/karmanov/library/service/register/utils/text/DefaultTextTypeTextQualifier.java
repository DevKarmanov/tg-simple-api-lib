package dev.karmanov.library.service.register.utils.text;

import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.TextMethodHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTextTypeTextQualifier implements TextQualifier {
    private static final Logger logger = LoggerFactory.getLogger(DefaultTextTypeTextQualifier.class);
    @Override
    public boolean textTypeCheck(TextMethodHolder textHolder, String text, TextType textType) {
        logger.debug("Checking text: '{}' against textHolder: '{}' with type: {}", text, textHolder.getText(), textType);

        boolean result = textHolder.getTextType().equals(textType) &&
                (textHolder.isRegex() ? text.matches(textHolder.getText()) : text.equals(textHolder.getText()));

        logger.debug("Text check result for '{}': {}", text, result);
        return result;
    }
}
