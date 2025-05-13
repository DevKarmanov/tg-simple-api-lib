package dev.karmanov.library.service.handlers.media;

import dev.karmanov.library.service.handlers.MessageHandler;

/**
 * Handler interface for processing various types of media messages from a Telegram bot.
 * <p>
 * Implementations are expected to handle media updates such as audio, video, documents, and others
 * based on the bot's logic
 * </p>
 */
public interface MediaHandler extends MessageHandler {
}
