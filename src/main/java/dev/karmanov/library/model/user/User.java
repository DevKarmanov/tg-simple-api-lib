package dev.karmanov.library.model.user;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a user in the system, with associated roles and states.
 */
public class User {
    private Long id;
    private final Set<String> roles = new HashSet<>();
    private DefaultUserContext userContext;

    /**
     * Constructs a new User with the given ID and context.
     *
     * @param id the ID of the user
     * @param userContext the context associated with the user
     */
    public User(Long id, DefaultUserContext userContext) {
        this.id = id;
        this.userContext = userContext;
    }

    /**
     * Adds a role to the user.
     *
     * @param role the role to be added
     */
    public void addRole(String role) {
        roles.add(role.toLowerCase(Locale.ROOT).trim());
    }

    /**
     * Adds multiple roles to the user.
     *
     * @param newRoles array of roles to be added
     */
    public void addRoles(String[] newRoles) {
        roles.addAll(Arrays.asList(newRoles));
    }

    /**
     * Removes specified roles from the user.
     *
     * @param rolesToDel array of roles to be removed
     * @return true if roles were removed, false otherwise
     */
    public boolean removeRoles(String[] rolesToDel) {
        Set<String> rolesToRemove = Arrays.stream(rolesToDel)
                .map(role -> role.toLowerCase(Locale.ROOT).trim())
                .collect(Collectors.toSet());
        return roles.removeAll(rolesToRemove);
    }

    /**
     * Clears all roles from the user.
     */
    public void resetRoles() {
        roles.clear();
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DefaultUserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(DefaultUserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) && Objects.equals(roles, user.roles) && Objects.equals(userContext, user.userContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roles, userContext);
    }
}

