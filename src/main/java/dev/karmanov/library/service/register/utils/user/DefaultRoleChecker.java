package dev.karmanov.library.service.register.utils.user;

import dev.karmanov.library.model.methodHolders.SpecialAccessMethodHolder;
import dev.karmanov.library.service.handlers.denied.AccessNotifier;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Set;

/**
 * Default implementation of the {@link RoleChecker} interface for verifying user access.
 * <p>
 * This class implements role-based access control, determining whether a user has
 * the required roles to perform certain actions. It interacts with a state manager to
 * retrieve user roles and uses an access notifier to send denial messages if the user lacks
 * the required roles.
 * </p>
 */
public class DefaultRoleChecker implements RoleChecker {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRoleChecker.class);
    private AccessNotifier accessNotifier;
    private StateManager manager;

    @Autowired(required = false)
    public void setManager(StateManager manager){
        this.manager = manager;
    }

    @Autowired(required = false)
    public void setAccessNotifier(AccessNotifier accessNotifier) {
        this.accessNotifier = accessNotifier;
    }

    /**
     * Checks whether a user has access to a specific method based on their roles.
     * <p>
     * The method checks if the user possesses any of the roles specified in the {@link SpecialAccessMethodHolder} object.
     * If no roles are required for the method, access is granted automatically. If the user does not have the necessary roles,
     * an access denied notification is sent.
     * </p>
     *
     * @param userId the ID of the user whose roles are being checked
     * @param chatId the ID of the chat, required for sending notifications
     * @param holder the {@link SpecialAccessMethodHolder} object containing the method and the roles allowed to access it
     * @return {@code true} if the user has any of the required roles for the method, {@code false} otherwise
     */
    @Override
    public boolean userHasAccess(Long userId, Long chatId, SpecialAccessMethodHolder holder) {
        if (holder == null) {
            logger.debug("Access granted: method is not registered as a special access method (userId={})", userId);
            return true;
        }

        if (holder.getRoles() == null || holder.getRoles().isEmpty()) {
            logger.debug("Access granted: no roles required for method (userId={})", userId);
            return true;
        }

        Set<String> userRoles = manager.getUserRoles(userId);
        if (userRoles == null || userRoles.isEmpty()) {
            logger.warn("Access denied: user has no roles (userId={}, requiredRoles={})", userId, holder.getRoles());
            accessNotifier.sendAccessDeniedMessage(chatId, Collections.emptySet());

            return false;
        }

        boolean hasAccess = holder.getRoles().stream().anyMatch(userRoles::contains);
        if (hasAccess) {
            logger.debug("Access granted: user has required role(s) (userId={}, userRoles={}, requiredRoles={})",
                    userId, userRoles, holder.getRoles());
        } else {
            logger.warn("Access denied: user lacks required roles (userId={}, userRoles={}, requiredRoles={})",
                    userId, userRoles, holder.getRoles());
            accessNotifier.sendAccessDeniedMessage(chatId, userRoles);
        }

        return hasAccess;
    }
}

