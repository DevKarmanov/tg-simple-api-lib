package dev.karmanov.library.model.message;

/**
 * Enum representing the types of input data that can be processed.
 * <p>
 * This enum defines different types of input that can be validated or processed, such as text messages or callback data.
 * It helps in distinguishing between various forms of user input and applying appropriate handling or validation logic.
 * </p>
 */
public enum TextType {
    /**
     * Represents input that is associated with callback data, typically used in button presses or callback queries.
     */
    CALLBACK_DATA,

    /**
     * Represents regular text input, such as messages or commands.
     */
    TEXT
}
