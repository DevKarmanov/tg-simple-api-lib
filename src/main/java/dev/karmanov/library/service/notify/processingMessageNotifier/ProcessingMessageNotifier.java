package dev.karmanov.library.service.notify.processingMessageNotifier;

/**
 * Sends a message indicating that the request is being processed.
 */
public interface ProcessingMessageNotifier {
    void sendProcessingMessage(Long chatId);
}

