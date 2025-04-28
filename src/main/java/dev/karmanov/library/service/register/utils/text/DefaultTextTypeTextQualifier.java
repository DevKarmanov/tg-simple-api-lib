package dev.karmanov.library.service.register.utils.text;

import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.TextMethodHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link TextQualifier} for checking if the input matches predefined conditions.
 * <p>
 * This class provides the logic for validating the input against specified rules, considering type and content
 * criteria. It supports exact matches or regular expression checks based on the configuration of the provided condition.
 * </p>
 */
public class DefaultTextTypeTextQualifier implements TextQualifier {
    private static final Logger logger = LoggerFactory.getLogger(DefaultTextTypeTextQualifier.class);

    /**
     * Validates whether the provided input matches the condition specified in the {@link TextMethodHolder}.
     * <p>
     * This method checks the type and content of the input against the predefined rules. Depending on the configuration,
     * it can perform an exact match or apply a regular expression match to verify the input's validity.
     * </p>
     *
     * @param textHolder the object containing the predefined condition or rule to check against.
     * @param text the input text to be validated.
     * @param textType the type of the input to ensure it matches the expected condition.
     * @return {@code true} if the input satisfies the condition, {@code false} otherwise.
     */
    @Override
    public boolean textTypeCheck(TextMethodHolder textHolder, String text, TextType textType) {
        logger.debug("Checking text: '{}' against textHolder: '{}' with type: {}", text, textHolder.getText(), textType);

        boolean result = textHolder.getTextType().equals(textType) &&
                (textHolder.isRegex() ? text.matches(textHolder.getText()) : text.equals(textHolder.getText()));

        logger.debug("Text check result for '{}': {}", text, result);
        return result;
    }
}
