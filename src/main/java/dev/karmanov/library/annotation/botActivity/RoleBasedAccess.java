package dev.karmanov.library.annotation.botActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to restrict access to a handler method based on user roles.
 * <p>
 * This annotation can be applied to methods to define which roles are allowed to access
 * the specific handler. If the user does not have one of the specified roles, access to
 * the method will be denied.
 * </p>
 * <p>
 * By default, all users are initialized with the role "user" upon their first request.
 * You can specify which roles should have access to the method by passing the roles as
 * an array of strings to the {@code roles} attribute
 * </p>
 *
 * <pre>
 * Example:
 *
 * {@code
 * @BotCallBack(actionName = "admin-action", callbackName = "admin-callback")
 * @RoleBasedAccess(roles = {"admin"})
 * public void adminOnly(Update update) { }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleBasedAccess {

    /**
     * Specifies the roles that are allowed to access the annotated method.
     * <p>
     * By default, the role "user" is given access to the method.
     * </p>
     */
    String[] roles() default {"user"};
}
