package dev.karmanov.library.service.notify.relevantModelErrorNotifier;

import dev.karmanov.library.service.notify.Notifier;
import dev.karmanov.library.service.notify.processingMessageNotifier.DefaultProcessingMessageNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class DefaultExceptionFoundRelevantModelNotifier implements ExceptionFoundRelevantModelNotifier {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionFoundRelevantModelNotifier.class);
    private Notifier notifier;

    @Autowired(required = false)
    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void sendExceptionFoundRelevantModelMessage(Long chatId, Map<String, String> languageModels) {
        String message = "⚠️ An error occurred while trying to find a relevant model. Please try again later.";
        logger.warn("Failed to find a relevant model for user with chatId {}. Available models: {}", chatId, languageModels);

        if (notifier != null) {
            notifier.sendMessage(chatId, message);
        } else {
            logger.error("Notifier is not set. Unable to send error message to chatId {}", chatId);
        }
    }
}

