package dev.karmanov.library.service.register.utils.media;

import dev.karmanov.library.model.message.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DefaultMediaAvailabilityQualifier implements MediaQualifier{

    private static final Logger logger = LoggerFactory.getLogger(DefaultMediaAvailabilityQualifier.class);
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
