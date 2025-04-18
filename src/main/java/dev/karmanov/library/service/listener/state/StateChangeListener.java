package dev.karmanov.library.service.listener.state;

import dev.karmanov.library.model.user.UserContext;

public interface StateChangeListener {
    void onStateChange(Long userId, UserContext oldState, UserContext newState);
}
