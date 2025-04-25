package dev.karmanov.library.service.register.utils.user;

import dev.karmanov.library.model.methodHolders.SpecialAccessMethodHolder;
import dev.karmanov.library.service.state.StateManager;

public interface RoleChecker {
    boolean userHasAccess(Long userId, StateManager manager, SpecialAccessMethodHolder specialAccessMethodHolders);
}
