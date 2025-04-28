package dev.karmanov.library.service.listener.role;

/**
 * Interface for notifying about role changes of users.
 * <p>
 * This interface allows adding and removing listeners that will be notified when a user's role is changed.
 * </p>
 */
public interface RoleChangeNotifier {
    /**
     * Adds a listener to be notified when a user's role changes.
     * <p>
     * The listener will be triggered whenever a role change occurs.
     * </p>
     *
     * @param listener the listener to be added.
     */
    void addRoleChangeListener(RoleChangeListener listener);

    /**
     * Removes a previously added role change listener.
     * <p>
     * The listener will no longer be notified when a role change occurs.
     * </p>
     *
     * @param listener the listener to be removed.
     */
    void removeRoleChangeListener(RoleChangeListener listener);
}
