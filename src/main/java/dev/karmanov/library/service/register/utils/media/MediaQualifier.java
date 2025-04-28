package dev.karmanov.library.service.register.utils.media;

import dev.karmanov.library.model.message.MediaType;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Interface for detecting media types in incoming messages.
 * <p>
 * This interface defines a method for checking whether a message contains any media,
 * such as audio, video, photo, document, voice, or sticker. Implementations should
 * define the logic for identifying and returning the corresponding media type.
 * </p>
 */
public interface MediaQualifier {

    /**
     * Checks if the incoming update contains any media.
     * <p>
     * This method inspects the message contained in the update and returns the corresponding
     * media type if one is found. If no media is detected, it returns {@code null}.
     * </p>
     *
     * @param update the incoming update containing the message to check for media.
     * @return the {@link MediaType} if media is detected, or {@code null} if no media is found.
     */
    MediaType hasMedia(Update update);
}
