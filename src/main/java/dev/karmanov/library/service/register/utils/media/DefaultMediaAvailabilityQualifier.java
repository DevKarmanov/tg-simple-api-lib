package dev.karmanov.library.service.register.utils.media;

import dev.karmanov.library.model.message.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Default implementation of the {@link MediaQualifier} interface to detect media in messages.
 * <p>
 * This class implements the {@link MediaQualifier} interface by checking if the incoming
 * message contains any type of media (e.g., photo, video, audio, document, etc.).
 * Based on the media type detected, it returns the corresponding {@link MediaType}.
 * </p>
 */
public class DefaultMediaAvailabilityQualifier implements MediaQualifier{

    private static final Logger logger = LoggerFactory.getLogger(DefaultMediaAvailabilityQualifier.class);

    /**
     * Checks if the incoming update contains media and determines the media type.
     * <p>
     * This method inspects the message in the update and returns the corresponding {@link MediaType}.
     * It supports various media types like audio, video, photo, document, sticker, etc. If no media
     * is found, it returns {@code null}.
     * </p>
     *
     * @param update the incoming update containing the message to check for media.
     * @return the {@link MediaType} corresponding to the media in the message, or {@code null} if no media is found.
     */
    @Override
    public MediaType hasMedia(Update update) {
        Message message = update.getMessage();
        logger.debug("Checking for media in message ID: {}", message.getMessageId());

        if (message.hasVoice()) {
            logger.info("Media type detected: VOICE in message ID: {}", message.getMessageId());
            return MediaType.VOICE;
        } else if (message.hasAudio()) {
            logger.info("Media type detected: AUDIO in message ID: {}", message.getMessageId());
            return MediaType.AUDIO;
        } else if (message.hasVideo()) {
            logger.info("Media type detected: VIDEO in message ID: {}", message.getMessageId());
            return MediaType.VIDEO;
        } else if (message.hasPhoto()) {
            logger.info("Media type detected: PHOTO in message ID: {}", message.getMessageId());
            return MediaType.PHOTO;
        } else if (message.hasDocument()) {
            logger.info("Media type detected: DOCUMENT in message ID: {}", message.getMessageId());
            return MediaType.DOCUMENT;
        } else if (message.hasSticker()) {
            logger.info("Media type detected: STICKER in message ID: {}", message.getMessageId());
            return MediaType.STICKER;
        } else {
            logger.debug("No media detected in message ID: {}", message.getMessageId());
            return null;
        }
    }
}
