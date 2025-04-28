package dev.karmanov.library.annotation.userActivity;

import dev.karmanov.library.service.state.StateManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Handles text messages in the bot.
 * <p>
 * This annotation is used to define methods that handle specific text-based commands or messages from the user.
 * The method is triggered when the bot receives a message that matches the specified {@code text} or regular expression (if {@code isRegex} is true).
 * The {@code actionName} links the method to a specific action in the user state manager.
 * </p>
 *
 * <p><b>Example:</b></p>
 *
 * <pre>
 * {@code
 * @BotText(text = "/start")
 * public void startCommand(Update update) {
 *     // Handle /start command
 * }
 * }
 * </pre>
 *
 * <p><b>Properties:</b></p>
 * <ul>
 * <li>{@code actionName} - The identifier for the action that the method represents. It links the method to the user's state.
 *      By default, the value is "start-command-method". If multiple methods share the same action name, they will be grouped under the same action name in the state manager.</li>
 * <li>{@code text} - The exact text message that will trigger this method. This can be a simple string (e.g., "/start") or a regular expression if {@code isRegex} is set to true.</li>
 * <li>{@code order} - The execution order of the method when multiple methods are triggered. The default value is {@code Integer.MAX_VALUE}, meaning the method will execute last unless specified otherwise.</li>
 * <li>{@code isRegex} - Indicates whether the {@code text} parameter should be treated as a regular expression.
 *      If true, the bot will match the text using regular expressions; otherwise, it will match the exact string.</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotText {

    /**
     * The identifier for the action that the method represents.
     * It links the method to the user's state.
     * Default is "start-command-method".
     */
    String actionName() default "start-command-method";

    /**
     * The exact text message that triggers the method.
     * Can be a string (e.g., "/start") or a regular expression if {@code isRegex} is true.
     */
    String text() default "/start";

    /**
     * The execution order of the method when multiple methods are triggered.
     * Default value is {@code Integer.MAX_VALUE}, meaning it executes last unless specified otherwise.
     */
    int order() default Integer.MAX_VALUE;

    /**
     * Whether to treat {@code text} as a regular expression.
     * If true, the bot will match the text using regular expressions; otherwise, it will match the exact string.
     */
    boolean isRegex() default false;
}




