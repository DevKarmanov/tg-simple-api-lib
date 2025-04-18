package dev.karmanov.library.model.user;

import java.util.*;
import java.util.stream.Collectors;

public class User {
    private Long id;
    private final Set<String> roles = new HashSet<>();
    private UserContext userContext;

    public User(Long id, UserContext userContext) {
        this.id = id;
        this.userContext = userContext;
    }

    public void addRole(String role){
        roles.add(role.toLowerCase(Locale.ROOT).trim());
    }

    public void addRoles(String[] newRoles){
        roles.addAll(Arrays.asList(newRoles));
    }

    public boolean removeRoles(String[] rolesToDel) {

        Set<String> rolesToRemove = Arrays.stream(rolesToDel)
                .map(role -> role.toLowerCase(Locale.ROOT).trim())
                .collect(Collectors.toSet());

        return roles.removeAll(rolesToRemove);
    }


    public void resetRoles(){
        roles.clear();
    }

    public Set<String> getRoles(){
        return roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserContext getUserStates() {
        return userContext;
    }

    public void setUserStates(UserContext userContext) {
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
