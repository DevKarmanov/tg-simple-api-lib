package dev.karmanov.library.service.register.utils.user;

import dev.karmanov.library.model.methodHolders.SpecialAccessMethodHolder;
import dev.karmanov.library.service.handlers.text.DefaultTextHandler;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class DefaultRoleChecker implements RoleChecker {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRoleChecker.class);

    @Override
    public boolean userHasAccess(Long userId, StateManager manager, SpecialAccessMethodHolder holder) {
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
            return false;
        }

        boolean hasAccess = holder.getRoles().stream().anyMatch(userRoles::contains);
        if (hasAccess) {
            logger.debug("Access granted: user has required role(s) (userId={}, userRoles={}, requiredRoles={})", userId, userRoles, holder.getRoles());
        } else {
            logger.warn("Access denied: user lacks required roles (userId={}, userRoles={}, requiredRoles={})", userId, userRoles, holder.getRoles());
        }

        return hasAccess;
    }
}

