package dev.karmanov.library.service.listener.state;

public interface StateChangeNotifier {
    /**
     * Adds a {@link StateChangeListener} to receive notifications about state changes for users.
     * <p>
     * This method allows external components to register their interest in state changes,
     * enabling them to react when a user's state is updated.
     * </p>
     *
     * @param listener the {@link StateChangeListener} to be added
     */
    void addStateChangeListener(StateChangeListener listener);

    /**
     * Removes a registered {@link StateChangeListener} from the notification list.
     * <p>
     * This method allows external components to unregister themselves from receiving state change notifications,
     * which can be useful for preventing memory leaks or for managing lifecycle events.
     * </p>
     *
     * @param listener the {@link StateChangeListener} to be removed
     */
    void removeStateChangeListener(StateChangeListener listener);

}
