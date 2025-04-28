package dev.karmanov.library.service.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

/**
 * General interface for handling incoming updates
 */
public interface MessageHandler {
    /**
     * Handles an incoming update from a Telegram user.
     *
     * @param userAwaitingAction set of actionNames that the user is currently expected to perform
     * @param update the incoming update
     */
    void handle(Set<String> userAwaitingAction, Update update);
}
