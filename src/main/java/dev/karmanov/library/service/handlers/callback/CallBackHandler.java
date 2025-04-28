package dev.karmanov.library.service.handlers.callback;

import dev.karmanov.library.service.handlers.MessageHandler;

/**
 * Interface for handling callback queries from a Telegram bot.
 * <p>
 * Implementations are expected to handle incoming {@link org.telegram.telegrambots.meta.api.objects.CallbackQuery}
 * messages
 * </p>
 */
public interface CallBackHandler extends MessageHandler {
}
