package dev.karmanov.library.service.state;

import dev.karmanov.library.model.user.UserState;
import dev.karmanov.library.model.user.UserContext;
import dev.karmanov.library.service.listener.role.RoleChangeNotifier;
import dev.karmanov.library.service.listener.state.StateChangeNotifier;

import java.util.Set;

public interface StateManager extends StateChangeNotifier, RoleChangeNotifier {

    void setNextStep(Long userId, UserContext state);

    void addUserRole(Long userId, String...roles);
    boolean removeUserRole(Long userId, String...roles);

    Set<String> getUserRoles(Long userId);

    Set<Long> getAllUserIds();

    Set<UserState> getStates(Long userId);

    void resetState(Long userId);

    Set<String> getUserAction(Long userId);

    String getDefaultStartActionName();
    void setDefaultStartActionName(String newDefaultActionName);
}
