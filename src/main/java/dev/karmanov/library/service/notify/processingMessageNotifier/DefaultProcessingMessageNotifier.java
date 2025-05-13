package dev.karmanov.library.service.notify.processingMessageNotifier;

import dev.karmanov.library.service.notify.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultProcessingMessageNotifier implements ProcessingMessageNotifier {
    private static final Logger logger = LoggerFactory.getLogger(DefaultProcessingMessageNotifier.class);

    private Notifier notifier;

    @Autowired(required = false)
    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void sendProcessingMessage(Long chatId) {
        String processingMessage = "⏳ Please wait... Your request is being processed.";

        logger.info("Sending processing message to chatId={}: {}", chatId, processingMessage);

        try {
            notifier.sendMessage(chatId, processingMessage);
            logger.debug("Processing message successfully sent to chatId={}", chatId);
        } catch (Exception e) {
            logger.error("Failed to send processing message to chatId={}", chatId, e);
        }
    }
}

