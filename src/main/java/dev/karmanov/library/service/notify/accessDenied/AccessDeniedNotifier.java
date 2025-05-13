package dev.karmanov.library.service.notify.accessDenied;

import java.util.Set;

public interface AccessDeniedNotifier {

    /**
     * Sends a notification to a user when they attempt to perform an action
     * they do not have permission for.
     *
     * @param chatId the ID of the chat to send the message to
     * @param currentUserRoles the set of roles currently assigned to the user
     */
    void sendAccessDeniedMessage(Long chatId, Set<String> currentUserRoles);
}
