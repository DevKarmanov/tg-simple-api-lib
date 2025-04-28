package dev.karmanov.library.service.handlers.media.photo;

import dev.karmanov.library.service.handlers.MessageHandler;

/**
 * Handler interface for processing photo messages from a Telegram bot.
 * <p>
 * Implementations are expected to handle incoming {@link org.telegram.telegrambots.meta.api.objects.PhotoSize}
 * messages
 * </p>
 */
public interface PhotoHandler extends MessageHandler {
}
