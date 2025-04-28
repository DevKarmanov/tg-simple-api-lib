package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to handle callback queries in a bot.
 * <p>
 * This annotation is used to map callback query handlers to specific actions and callback names.
 * It allows specifying the action name, callback names (either as exact names or regular expressions),
 * and the order of execution
 * </p>
 *
 * <p><b>Example:</b></p>
 *
 * <pre>
 * {@code
 * @BotCallBack(actionName = "create", callbackName = {"^.*ad$", "^read.*"}, isRegex = true)
 * public void handleCallback(Update update) { }
 * }
 * </pre>
 *
 * <p>
 * Attributes:
 * </p>
 *
 * <ul>
 *     <li><b>actionName</b>: The action ID (e.g., "create", "read").</li>
 *     <li><b>callbackName</b>: An array of callback names or regular expressions to match against the callback data.</li>
 *     <li><b>isRegex</b>: Whether to treat the callback names as regular expressions for matching (default: false).</li>
 *     <li><b>order</b>: The order in which the callback handler is executed. Handlers with lower order values are executed first (default: {@link Integer#MAX_VALUE}).</li>
 * </ul>
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BotCallBack {

    /**
     * The action ID for the callback (e.g., "create", "update").
     */
    String actionName();

    /**
     * An array of callback names or regular expressions to match against the callback data.
     */
    String[] callbackName();

    /**
     * Whether the callback names should be treated as regular expressions.
     */
    boolean isRegex() default false;

    /**
     * The order of execution for the callback handler.
     */
    int order() default Integer.MAX_VALUE;
}




