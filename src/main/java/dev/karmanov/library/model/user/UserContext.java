package dev.karmanov.library.model.user;

import java.util.List;
import java.util.Objects;

public class UserContext {
    private UserState userState;
    private List<String> actionData;

    public UserContext(UserState userState, List<String> actionData) {
        this.userState = userState;
        this.actionData = actionData;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public List<String> getActionData() {
        return actionData;
    }

    public void setActionData(List<String> actionData) {
        this.actionData = actionData;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserContext that = (UserContext) object;
        return userState == that.userState && Objects.equals(actionData, that.actionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userState, actionData);
    }
}
