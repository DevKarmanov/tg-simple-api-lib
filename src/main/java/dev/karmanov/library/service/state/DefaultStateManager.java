package dev.karmanov.library.service.state;

import dev.karmanov.library.model.user.User;
import dev.karmanov.library.model.user.UserState;
import dev.karmanov.library.model.user.UserContext;
import dev.karmanov.library.service.listener.role.RoleChangeListener;
import dev.karmanov.library.service.listener.state.StateChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultStateManager implements StateManager {
    private String DefaultStartActionName = "start-command-method";
    private static final Logger logger = LoggerFactory.getLogger(DefaultStateManager.class);
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final List<StateChangeListener> stateChangeListeners = new ArrayList<>();
    private final List<RoleChangeListener> roleChangeListeners = new ArrayList<>();

    @Override
    public void setState(Long userId, UserContext state) {
        User user = users.get(userId);
        UserContext oldState = null;
        if (user!=null){
            oldState = user.getUserStates();
            user.setUserStates(state);
        }else {
            String role = "user";
            User newUser = new User(userId,state);
            newUser.addRole(role);
            users.put(userId,newUser);
            for (RoleChangeListener listener: roleChangeListeners){
                listener.onRoleChange(userId,null,role);
            }
        }
        logger.debug("Set the state for user {}: {} ", userId, state);
        for (StateChangeListener listener: stateChangeListeners){
            listener.onStateChange(userId,oldState,state);
        }
    }

    @Override
    public void setUserRole(Long userId, String... roles) {
        User user = users.get(userId);
        String oldRoles = (user != null) ? String.join(", ", user.getRoles()) : null;

        if (user != null) {
            user.addRoles(roles);
        } else {
            logger.warn("User {} not found. Roles not set.", userId);
        }

        String newRoles = String.join(", ", roles);
        logger.debug("Set roles for user {}. Old roles: {}, New roles: {}", userId, oldRoles, newRoles);

        roleChangeListeners.forEach(listener -> listener.onRoleChange(userId, oldRoles, newRoles));
    }


    @Override
    public boolean removeUserRole(Long userId, String... rolesToRemove) {
        if (rolesToRemove == null || rolesToRemove.length == 0) {
            logger.debug("No roles specified for removal for user {}", userId);
            return false;
        }

        User user = users.get(userId);
        if (user == null) {
            logger.debug("User {} not found. No roles removed.", userId);
            return false;
        }

        String oldRoles = getUserRoles(userId).toString();

        boolean removed = user.removeRoles(rolesToRemove);

        String newRoles = getUserRoles(userId).toString();

        logger.debug("Removed roles {} for user {}. Old roles: {}, New roles: {}",
                Arrays.toString(rolesToRemove), userId, oldRoles, newRoles);

        roleChangeListeners.forEach(listener -> listener.onRoleChange(userId, oldRoles, newRoles));

        return removed;
    }


    @Override
    public Set<String> getUserRoles(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .map(User::getRoles)
                .orElse(null);
    }

    @Override
    public Set<Long> getAllUserIds() {
        Set<Long> userIds = new HashSet<>();
        users.forEach((k,v)->userIds.add(v.getId()));
        return userIds;
    }

    @Override
    public UserState getState(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .map(User::getUserStates)
                .map(UserContext::getUserState)
                .orElse(null);
    }

    @Override
    public void resetState(Long userId) {
        setState(userId,new UserContext(UserState.DEFAULT, Collections.singletonList("/start")));
        setUserRole(userId,"user");
    }

    @Override
    public List<String> getUserAction(Long userId) {
        return users.get(userId).getUserStates().getActionData();
    }

    @Override
    public String getDefaultStartActionName(){
        return DefaultStartActionName;
    }

    @Override
    public void setDefaultStartActionName(String newDefaultActionName){
        this.DefaultStartActionName = newDefaultActionName;
    }

    @Override
    public void addStateChangeListener(StateChangeListener listener) {
        stateChangeListeners.add(listener);
    }

    @Override
    public void removeStateChangeListener(StateChangeListener listener) {
        stateChangeListeners.remove(listener);
    }

    @Override
    public void addRoleChangeListener(RoleChangeListener listener) {
        roleChangeListeners.add(listener);
    }

    @Override
    public void removeRoleChangeListener(RoleChangeListener listener) {
        roleChangeListeners.remove(listener);
    }
}

