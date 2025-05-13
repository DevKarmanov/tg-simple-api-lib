package dev.karmanov.library.service.notify.initModel;

import dev.karmanov.library.service.notify.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultInitModelMessageNotifier implements InitModelMessageNotifier {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInitModelMessageNotifier.class);
    private Notifier notifier;

    @Autowired(required = false)
    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void sendInitModelMessage(Long chatId) {
        String message = "🤖 Initializing the language model... Please wait a moment";

        logger.info("Sending init model message to chatId={}: {}", chatId, message);

        try {
            notifier.sendMessage(chatId, message);
            logger.debug("Init model message successfully sent to chatId={}", chatId);
        } catch (Exception e) {
            logger.error("Failed to send init model message to chatId={}", chatId, e);
        }
    }
}

