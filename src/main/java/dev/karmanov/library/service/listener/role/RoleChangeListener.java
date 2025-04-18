package dev.karmanov.library.service.listener.role;

public interface RoleChangeListener {
    void onRoleChange(Long userId, String oldRoles, String newRoles);
}
