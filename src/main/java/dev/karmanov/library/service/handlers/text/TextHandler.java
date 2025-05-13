package dev.karmanov.library.service.handlers.text;

import dev.karmanov.library.service.handlers.MessageHandler;

/**
 * Handler interface for processing text messages or commands from a Telegram bot.
 * <p>
 * Implementations are expected to handle incoming {@link org.telegram.telegrambots.meta.api.objects.Te}
 * messages
 * </p>
 */
public interface TextHandler extends MessageHandler {
}
