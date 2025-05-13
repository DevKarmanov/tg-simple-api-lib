package dev.karmanov.library.service.notify;

/**
 * Interface for sending messages to users.
 */
public interface Notifier {
    void sendMessage(Long chatId, String text);
}

