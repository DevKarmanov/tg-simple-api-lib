package dev.karmanov.library.model.message;

/**
 * Enum representing different types of media that can be processed by the bot.
 * <p><b>Media Types:</b></p>
 * <ul>
 * <li>{@code PHOTO} - Represents an image file (photo).</li>
 * <li>{@code VIDEO} - Represents a video file.</li>
 * <li>{@code AUDIO} - Represents an audio file.</li>
 * <li>{@code DOCUMENT} - Represents a generic document file.</li>
 * <li>{@code VOICE} - Represents a voice message file.</li>
 * <li>{@code STICKER} - Represents a sticker file.</li>
 * </ul>
 */
public enum MediaType {
    /**
     * Represents a photo media type.
     */
    PHOTO,

    /**
     * Represents a video media type.
     */
    VIDEO,

    /**
     * Represents an audio media type.
     */
    AUDIO,

    /**
     * Represents a document media type.
     */
    DOCUMENT,

    /**
     * Represents a voice message media type.
     */
    VOICE,

    /**
     * Represents a sticker media type.
     */
    STICKER
}

