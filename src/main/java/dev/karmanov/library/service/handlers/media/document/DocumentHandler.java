package dev.karmanov.library.service.handlers.media.document;

import dev.karmanov.library.service.handlers.MessageHandler;

/**
 * Interface for handling incoming document messages in a Telegram bot.
 * <p>
 * Implementations are expected to handle incoming {@link org.telegram.telegrambots.meta.api.objects.Document}
 * messages
 * </p>
 */
public interface DocumentHandler extends MessageHandler {
}
