package dev.karmanov.library.model.user;

import java.util.HashSet;
import java.util.Set;

public class UserContext {
    private Set<UserState> userStates;
    private Set<String> actionData;

    private UserContext(){}

    public static Builder builder() {
        return new Builder();
    }

    public Set<UserState> getUserStates() {
        return userStates;
    }

    public Set<String> getActionData() {
        return actionData;
    }

    public static class Builder {
        private final Set<UserState> userStates = new HashSet<>();
        private final Set<String> actionData = new HashSet<>();

        public Builder addState(UserState userState){
            this.userStates.add(userState);
            return this;
        }

        public Builder addState(Set<UserState> userStates){
            this.userStates.addAll(userStates);
            return this;
        }

        public Builder addActionData(String actionData){
            this.actionData.add(actionData);
            return this;
        }

        public Builder addActionData(Set<String> actionData){
            this.actionData.addAll(actionData);
            return this;
        }

        public UserContext build(){
            UserContext userContext = new UserContext();
            userContext.userStates = this.userStates;
            userContext.actionData = this.actionData;
            return userContext;
        }
    }
}
