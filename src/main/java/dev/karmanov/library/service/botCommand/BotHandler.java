package dev.karmanov.library.service.botCommand;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Interface for handling incoming updates from users and delegating the processing to specific handlers.
 * <p>
 * The `handleMessage` method processes updates, checks the user's state,
 * and delegates the handling of the update to the appropriate handler (e.g., `TextHandler`, `PhotoHandler`, etc.).
 * It also manages scheduled tasks and sends notifications in case of unexpected actions.
 * </p>
 */
public interface BotHandler {
    /**
     * Handles incoming updates.
     * <p>
     * This method should be implemented to:
     * - Initialize the user's state if necessary.
     * - Delegate processing of the update to the appropriate handler based on the user's state and the type of update.
     * - Run scheduled tasks, if applicable.
     * - Notify users of unexpected actions if the update does not match the expected flow.
     * </p>
     *
     * @param update the incoming update containing a message or callback query to be processed.
     */
    void handleMessage(Update update);
}
