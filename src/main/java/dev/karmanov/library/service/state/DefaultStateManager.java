package dev.karmanov.library.service.state;

import dev.karmanov.library.model.user.User;
import dev.karmanov.library.model.user.UserState;
import dev.karmanov.library.model.user.UserContext;
import dev.karmanov.library.service.listener.role.RoleChangeListener;
import dev.karmanov.library.service.listener.state.StateChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the StateManager interface that manages user states and roles.
 */
public class DefaultStateManager implements StateManager {
    private String DefaultStartActionName = "start-command-method";
    private static final Logger logger = LoggerFactory.getLogger(DefaultStateManager.class);
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final List<StateChangeListener> stateChangeListeners = new ArrayList<>();
    private final List<RoleChangeListener> roleChangeListeners = new ArrayList<>();

    /**
     * Sets the next step (state) for the specified user.
     *
     * @param userId the ID of the user
     * @param state the new user state
     */
    @Override
    public void setNextStep(Long userId, UserContext state) {
        User user = users.get(userId);
        UserContext oldContext = null;
        if (user != null) {
            oldContext = user.getUserContext();
            user.setUserContext(state);
        } else {
            String role = "user";
            User newUser = new User(userId, state);
            newUser.addRole(role);
            users.put(userId, newUser);
            for (RoleChangeListener listener : roleChangeListeners) {
                listener.onRoleChange(userId, null, role);
            }
        }
        logger.debug("Set the state for user {}: {}", userId, state);
        for (StateChangeListener listener : stateChangeListeners) {
            listener.onStateChange(userId, oldContext, state);
        }
    }

    /**
     * Adds roles to the specified user.
     *
     * @param userId the ID of the user
     * @param roles roles to add
     */
    @Override
    public void addUserRole(Long userId, String... roles) {
        User user = users.get(userId);
        String oldRoles = (user != null) ? String.join(", ", user.getRoles()) : null;

        if (user != null) {
            user.addRoles(roles);
        } else {
            logger.warn("User {} not found. Roles not set.", userId);
        }

        String newRoles = String.join(", ", roles);
        logger.debug("Set roles for user {}. Old roles: {}, New roles: {}", userId, oldRoles, newRoles);

        roleChangeListeners.forEach(listener -> listener.onRoleChange(userId, oldRoles, newRoles));
    }

    /**
     * Removes roles from the specified user.
     *
     * @param userId the ID of the user
     * @param rolesToRemove roles to remove
     * @return true if roles were removed, false otherwise
     */
    @Override
    public boolean removeUserRole(Long userId, String... rolesToRemove) {
        if (rolesToRemove == null || rolesToRemove.length == 0) {
            logger.debug("No roles specified for removal for user {}", userId);
            return false;
        }

        User user = users.get(userId);
        if (user == null) {
            logger.debug("User {} not found. No roles removed.", userId);
            return false;
        }

        String oldRoles = getUserRoles(userId).toString();

        boolean removed = user.removeRoles(rolesToRemove);

        String newRoles = getUserRoles(userId).toString();

        logger.debug("Removed roles {} for user {}. Old roles: {}, New roles: {}",
                Arrays.toString(rolesToRemove), userId, oldRoles, newRoles);

        roleChangeListeners.forEach(listener -> listener.onRoleChange(userId, oldRoles, newRoles));

        return removed;
    }

    /**
     * Gets the roles for the specified user.
     *
     * @param userId the ID of the user
     * @return the roles of the user
     */
    @Override
    public Set<String> getUserRoles(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .map(User::getRoles)
                .orElse(null);
    }

    /**
     * Gets all user IDs.
     *
     * @return a set of user IDs
     */
    @Override
    public Set<Long> getAllUserIds() {
        Set<Long> userIds = new HashSet<>();
        users.forEach((k, v) -> userIds.add(v.getId()));
        return userIds;
    }

    /**
     * Gets the states for the specified user.
     *
     * @param userId the ID of the user
     * @return a set of user states
     */
    @Override
    public Set<UserState> getStates(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .map(User::getUserContext)
                .map(UserContext::getUserStates)
                .orElse(null);
    }

    /**
     * Resets the state of the user to the default state.
     *
     * @param userId the ID of the user
     */
    @Override
    public void resetState(Long userId) {
        setNextStep(userId, UserContext.builder()
                .addState(UserState.DEFAULT)
                .addActionData("/start")
                .build());
        addUserRole(userId, "user");
    }

    /**
     * Gets the action data for the specified user.
     *
     * @param userId the ID of the user
     * @return the action data of the user
     */
    @Override
    public Set<String> getUserAction(Long userId) {
        return users.get(userId).getUserContext().getActionData();
    }

    /**
     * Gets the default start action name.
     *
     * @return the default start action name
     */
    @Override
    public String getDefaultStartActionName() {
        return DefaultStartActionName;
    }

    /**
     * Sets a new default start action name.
     *
     * @param newDefaultActionName the new default start action name
     */
    @Override
    public void setDefaultStartActionName(String newDefaultActionName) {
        this.DefaultStartActionName = newDefaultActionName;
    }

    /**
     * Adds a listener to be notified when a state change occurs.
     *
     * @param listener the state change listener
     */
    @Override
    public void addStateChangeListener(StateChangeListener listener) {
        stateChangeListeners.add(listener);
    }

    /**
     * Removes a state change listener.
     *
     * @param listener the state change listener to remove
     */
    @Override
    public void removeStateChangeListener(StateChangeListener listener) {
        stateChangeListeners.remove(listener);
    }

    /**
     * Adds a listener to be notified when a role change occurs.
     *
     * @param listener the role change listener
     */
    @Override
    public void addRoleChangeListener(RoleChangeListener listener) {
        roleChangeListeners.add(listener);
    }

    /**
     * Removes a role change listener.
     *
     * @param listener the role change listener to remove
     */
    @Override
    public void removeRoleChangeListener(RoleChangeListener listener) {
        roleChangeListeners.remove(listener);
    }
}


