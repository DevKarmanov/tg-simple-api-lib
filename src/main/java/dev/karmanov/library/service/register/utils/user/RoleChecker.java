package dev.karmanov.library.service.register.utils.user;

import dev.karmanov.library.model.methodHolders.SpecialAccessMethodHolder;
import dev.karmanov.library.service.state.StateManager;

/**
 * Interface for checking whether a user has the required access based on their roles.
 * <p>
 * This interface defines the contract for verifying if a user possesses the necessary roles
 * to access a specific method or functionality. It provides a mechanism for role-based access control
 * in the system.
 * </p>
 */
public interface RoleChecker {

    /**
     * Checks if the user has access to a method based on their roles.
     *
     * @param userId the ID of the user whose roles are being checked
     * @param chatId the ID of the chat, required for sending notifications
     * @param specialAccessMethodHolders an object containing the method and the roles that are allowed to access it
     * @return {@code true} if the user has the required roles for access, {@code false} otherwise
     */
    boolean userHasAccess(Long userId, Long chatId, SpecialAccessMethodHolder specialAccessMethodHolders);
}
