package dev.karmanov.library.service.register.utils.text;

import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.TextMethodHolder;

/**
 * Interface for validating and checking if the provided input matches a predefined condition based on type and content.
 * <p>
 * This interface defines a method to assess whether the given input satisfies specific criteria
 * such as type and content, which can be customized for various use cases like command processing,
 * callback handling, or other types of input validation.
 * </p>
 */
public interface TextQualifier {

    /**
     * Validates whether the provided input matches the specified condition based on type and content.
     * <p>
     * This method allows for checking if the input conforms to certain rules, such as matching a
     * specified type or pattern, ensuring that it meets the expected criteria for further processing.
     * </p>
     *
     * @param textHolder the object containing the predefined condition or rule to check against.
     * @param text the input text to be validated.
     * @param textType the type of the input to ensure it matches the expected condition.
     * @return {@code true} if the input satisfies the condition, {@code false} otherwise.
     */
    boolean textTypeCheck(TextMethodHolder textHolder, String text, TextType textType);
}

