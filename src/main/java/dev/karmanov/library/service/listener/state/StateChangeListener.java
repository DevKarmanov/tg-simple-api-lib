package dev.karmanov.library.service.listener.state;

import dev.karmanov.library.model.user.DefaultUserContext;
import dev.karmanov.library.model.user.UserContext;

/**
 * Interface for listening to state changes of users.
 * <p>
 * Implement this interface to define custom logic that will be executed when a user's state is updated.
 * </p>
 */
public interface StateChangeListener {
    /**
     * Called when a user's state is changed.
     * <p>
     * This method will be triggered whenever a user's state is updated, providing both the old and new state.
     * </p>
     *
     * @param userId the ID of the user whose state has changed.
     * @param oldState the previous state of the user.
     * @param newState the new state of the user.
     */
    void onStateChange(Long userId, UserContext oldState, UserContext newState);
}
