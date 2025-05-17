package dev.karmanov.library.service.state;

import dev.karmanov.library.model.user.UserState;
import dev.karmanov.library.model.user.DefaultUserContext;
import dev.karmanov.library.service.listener.role.RoleChangeNotifier;
import dev.karmanov.library.service.listener.state.StateChangeNotifier;

import java.util.Set;

/**
 * Interface for managing user states and roles in a bot system.
 * <p>
 * This interface defines methods for managing user states, roles, and actions. It provides
 * functionality for setting the next step (state) for a user, adding/removing user roles,
 * resetting user states, and managing the default start action name. It also allows listeners
 * to be notified of state or role changes.
 * </p>
 */
public interface StateManager extends StateChangeNotifier, RoleChangeNotifier {
    /**
     * Sets the next step (state) for the specified user.
     *
     * @param userId the ID of the user
     * @param state the new user state
     */
    void setNextStep(Long userId, DefaultUserContext state);

    /**
     * Adds roles to the specified user.
     *
     * @param userId the ID of the user
     * @param roles the roles to add
     */
    void addUserRole(Long userId, String...roles);

    /**
     * Removes roles from the specified user.
     *
     * @param userId the ID of the user
     * @param roles the roles to remove
     * @return true if roles were successfully removed, false otherwise
     */
    boolean removeUserRole(Long userId, String...roles);

    /**
     * Gets the roles associated with the specified user.
     *
     * @param userId the ID of the user
     * @return a set of roles associated with the user
     */
    Set<String> getUserRoles(Long userId);

    /**
     * Gets all user IDs in the system.
     *
     * @return a set of all user IDs
     */
    Set<Long> getAllUserIds();

    /**
     * Gets the states associated with the specified user.
     *
     * @param userId the ID of the user
     * @return a set of states associated with the user
     */
    Set<UserState> getStates(Long userId);

    /**
     * Resets the state of the user to the default state.
     *
     * @param userId the ID of the user
     */
    void resetState(Long userId);

    /**
     * Gets the action data (actionNames) that are expected from the specified user.
     *
     * @param userId the ID of the user
     * @return a set of action data associated with the user
     */
    Set<String> getUserAction(Long userId);

    /**
     * Gets the default start action name.
     *
     * @return the default start action name
     */
    String getDefaultStartActionName();

    /**
     * Sets a new default start action name.
     *
     * @param newDefaultActionName the new default start action name
     */
    void setDefaultStartActionName(String newDefaultActionName);
}
