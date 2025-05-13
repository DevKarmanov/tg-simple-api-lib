package dev.karmanov.library.service.notify.unexpectedAction;

import dev.karmanov.library.model.user.UserState;

import java.util.Set;

public interface UnexpectedActionNotifier {
    /**
     * Sends a notification to a user when they perform an unexpected action
     * that the bot was not awaiting.
     *
     * @param chatId the ID of the chat to send the message to
     * @param currentUserStates the set of states the user is currently in
     */
    void sendUnexpectedActionMessage(Long chatId, Set<UserState> currentUserStates);
}
