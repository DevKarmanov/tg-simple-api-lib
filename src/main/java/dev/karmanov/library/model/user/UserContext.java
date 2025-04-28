package dev.karmanov.library.model.user;

import java.util.HashSet;
import java.util.Set;

/**
 * User context that stores information about the user's current states and associated action data.
 * <p>
 * This class is used to store information about which user states are active and which action data
 * (such as actionName) is expected in the interaction with the bot.
 * </p>
 */
public class UserContext {
    /**
     * A set of user states.
     * Contains the active states of the user, such as waiting for text input, voice messages, etc.
     */
    private Set<UserState> userStates;

    /**
     * A set of action data (actionName).
     * Contains the set of expected actionName values at this moment.
     */
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

        /**
         * Adds a user state to the context.
         * @param userState the user state
         * @return the Builder instance
         */
        public Builder addState(UserState userState){
            this.userStates.add(userState);
            return this;
        }

        /**
         * Adds multiple user states to the context.
         * @param userStates the set of user states
         * @return the Builder instance
         */
        public Builder addState(Set<UserState> userStates){
            this.userStates.addAll(userStates);
            return this;
        }

        /**
         * Adds an action data (actionName) to the context.
         * @param actionData the action data (actionName)
         * @return the Builder instance
         */
        public Builder addActionData(String actionData){
            this.actionData.add(actionData);
            return this;
        }

        /**
         * Adds multiple action data (actionName) to the context.
         * @param actionData the set of action data (actionName)
         * @return the Builder instance
         */
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

