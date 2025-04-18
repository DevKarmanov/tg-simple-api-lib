package dev.karmanov.library.service.state;

import dev.karmanov.library.model.user.UserState;
import dev.karmanov.library.model.user.UserContext;
import dev.karmanov.library.service.listener.role.RoleChangeNotifier;
import dev.karmanov.library.service.listener.state.StateChangeNotifier;

import java.util.List;
import java.util.Set;

public interface StateManager extends StateChangeNotifier, RoleChangeNotifier {

    void setState(Long userId, UserContext state);

    void setUserRole(Long userId, String...roles);
    boolean removeUserRole(Long userId, String...roles);

    Set<String> getUserRoles(Long userId);

    Set<Long> getAllUserIds();

    UserState getState(Long userId);

    void resetState(Long userId);

    List<String> getUserAction(Long userId);

    String getDefaultStartActionName();
    void setDefaultStartActionName(String newDefaultActionName);
}
