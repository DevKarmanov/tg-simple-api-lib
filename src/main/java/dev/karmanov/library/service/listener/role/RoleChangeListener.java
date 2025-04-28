package dev.karmanov.library.service.listener.role;

/**
 * Interface for listening to role changes of users.
 * <p>
 * Implement this interface to define custom logic for handling role changes when a user's role is updated.
 * </p>
 */
public interface RoleChangeListener {

    /**
     * Called when a user's role is changed.
     * <p>
     * This method will be invoked with the old and new roles for the specified user.
     * </p>
     *
     * @param userId the ID of the user whose role has been changed.
     * @param oldRole the user's previous role.
     * @param newRole the user's new role.
     */
    void onRoleChange(Long userId, String oldRole, String newRole);
}

