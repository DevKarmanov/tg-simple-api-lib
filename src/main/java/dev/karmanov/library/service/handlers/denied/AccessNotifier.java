package dev.karmanov.library.service.handlers.denied;

import dev.karmanov.library.model.user.UserState;

import java.util.Set;

/**
 * Interface for notifying users about access control and action validation results.
 * <p>
 * Implementations of this interface are responsible for sending messages
 * to users when access is denied or an unexpected action is performed.
 * </p>
 */
public interface AccessNotifier {

    /**
     * Sends a notification to a user when they attempt to perform an action
     * they do not have permission for.
     *
     * @param chatId the ID of the chat to send the message to
     * @param currentUserRoles the set of roles currently assigned to the user
     */
    void sendAccessDeniedMessage(Long chatId, Set<String> currentUserRoles);

    /**
     * Sends a notification to a user when they perform an unexpected action
     * that the bot was not awaiting.
     *
     * @param chatId the ID of the chat to send the message to
     * @param currentUserStates the set of states the user is currently in
     */
    void sendUnexpectedActionMessage(Long chatId, Set<UserState> currentUserStates);
}
