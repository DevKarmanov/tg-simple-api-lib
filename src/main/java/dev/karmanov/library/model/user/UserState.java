package dev.karmanov.library.model.user;

/**
 * Enum representing different states of a user in the bot.
 * <p>
 * This enum is used to track the current state of the user during interactions with the bot.
 * Each state represents a different stage in the conversation or interaction process.
 * </p>
 *
 * <p><b>User States:</b></p>
 * <ul>
 * <li>{@code DEFAULT} - Default state when the user is not interacting with the bot.</li>
 * <li>{@code AWAITING_INPUT} - State when the bot is waiting for user input (e.g., text, command).</li>
 * <li>{@code AWAITING_TEXT} - State when the bot is specifically waiting for text input from the user.</li>
 * <li>{@code AWAITING_CALLBACK} - State when the bot is waiting for a callback from the user (e.g., button press).</li>
 * <li>{@code AWAITING_VOICE} - State when the bot is waiting for a voice message from the user.</li>
 * <li>{@code AWAITING_PHOTO} - State when the bot is waiting for a photo from the user.</li>
 * <li>{@code AWAITING_MEDIA} - State when the bot is waiting for any type of media from the user (photo, video, etc.).</li>
 * <li>{@code AWAITING_DOCUMENT} - State when the bot is waiting for a document from the user.</li>
 * </ul>
 */
public enum UserState {
    /**
     * Default state when the user is not interacting with the bot.
     */
    DEFAULT,

    /**
     * State when the bot is specifically waiting for text input from the user.
     */
    AWAITING_TEXT,

    /**
     * State when the bot is waiting for a callback from the user (e.g., button press).
     */
    AWAITING_CALLBACK,

    /**
     * State when the bot is waiting for a voice message from the user.
     */
    AWAITING_VOICE,

    /**
     * State when the bot is waiting for a photo from the user.
     */
    AWAITING_PHOTO,

    /**
     * State when the bot is waiting for any type of media from the user (photo, video, etc.).
     */
    AWAITING_MEDIA,

    /**
     * State when the bot is waiting for a document from the user.
     */
    AWAITING_DOCUMENT,

}


