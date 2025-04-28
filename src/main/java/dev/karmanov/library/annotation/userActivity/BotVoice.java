package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Handles voice messages in the bot.
 * <p>
 * This annotation is used to define methods that handle voice messages from the user.
 * The method is triggered when the bot receives a voice message that matches the specified conditions,
 * such as the duration of the voice message.
 * </p>
 *
 * <p><b>Example:</b></p>
 *
 * <pre>
 * {@code
 * @BotVoice(actionName = "voice_wait", maxDurationSeconds = 6, minDurationSeconds = 1)
 * public void handleVoice(Update update) {
 *     // Handle voice message
 * }
 * }
 * </pre>
 *
 * <p><b>Properties:</b></p>
 * <ul>
 * <li>{@code actionName} - The identifier for the action that the method represents.
 *      It links the method to the user's state. It should be unique for each voice message handler.</li>
 * <li>{@code maxDurationSeconds} - The maximum duration (in seconds) for the voice message. The method will only be triggered
 *      if the duration of the voice message is less than or equal to this value. The default value is 60 seconds.</li>
 * <li>{@code minDurationSeconds} - The minimum duration (in seconds) for the voice message. The method will only be triggered
 *      if the duration of the voice message is greater than or equal to this value. The default value is 5 seconds.</li>
 * <li>{@code order} - The execution order of the method when multiple methods are triggered.
 *      The default value is {@code Integer.MAX_VALUE}, meaning the method will execute last unless specified otherwise.</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotVoice {

    /**
     * The identifier for the action that the method represents.
     * It links the method to the user's state.
     */
    String actionName();

    /**
     * The maximum duration (in seconds) for the voice message.
     * The method will only be triggered if the duration of the voice message is less than or equal to this value.
     * Default is 60 seconds.
     */
    int maxDurationSeconds() default 60;

    /**
     * The minimum duration (in seconds) for the voice message.
     * The method will only be triggered if the duration of the voice message is greater than or equal to this value.
     * Default is 5 seconds.
     */
    int minDurationSeconds() default 5;

    /**
     * The execution order of the method when multiple methods are triggered.
     * Default value is {@code Integer.MAX_VALUE}, meaning it executes last unless specified otherwise.
     */
    int order() default Integer.MAX_VALUE;
}

