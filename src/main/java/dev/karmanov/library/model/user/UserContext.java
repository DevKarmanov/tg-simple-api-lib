package dev.karmanov.library.model.user;

import java.util.Set;

public interface UserContext {
    Set<UserState> getUserStates();
    Set<String> getActionData();

}
