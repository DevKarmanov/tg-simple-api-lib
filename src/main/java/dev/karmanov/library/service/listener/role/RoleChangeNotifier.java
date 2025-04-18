package dev.karmanov.library.service.listener.role;

public interface RoleChangeNotifier {
    void addRoleChangeListener(RoleChangeListener listener);
    void removeRoleChangeListener(RoleChangeListener listener);

}
