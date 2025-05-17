package dev.karmanov.library.model.user;

import java.util.Set;

public interface UserContextBuilder {
    UserContextBuilder addState(UserState userState);
    UserContextBuilder addState(Set<UserState> userStates);
    UserContextBuilder addActionData(String actionData);
    UserContextBuilder addActionData(Set<String> actionData);
    UserContext build();
}
