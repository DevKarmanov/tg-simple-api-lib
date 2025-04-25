package dev.karmanov.library.service.handlers.denied;

import dev.karmanov.library.model.user.UserState;

import java.util.Set;

public interface AccessNotifier {
    void sendAccessDeniedMessage(Long chatId, Set<String> currentUserRoles);
    void sendUnexpectedActionMessage(Long chatId, Set<UserState> currentUserStates);
}
