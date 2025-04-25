package dev.karmanov.library.model.methodHolders;

import dev.karmanov.library.model.methodHolders.abstractHolders.BaseMethodHolder;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

public class SpecialAccessMethodHolder extends BaseMethodHolder {
    private Set<String> roles;

    public SpecialAccessMethodHolder(Method method, Set<String> roles) {
        super(method);
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SpecialAccessMethodHolder that = (SpecialAccessMethodHolder) object;
        return Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roles);
    }
}
