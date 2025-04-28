package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for scheduling tasks that will be executed periodically or at a specific time.
 * <p>
 * This annotation allows you to define a method that will be executed on a schedule,
 * based on CRON expressions, fixed delays, or fixed rates. The tasks can also be
 * executed once at startup, and filtered based on user roles.
 * </p>
 * <p><b>Example:</b></p>
 *
 * <pre>
 * {@code
 * @BotScheduled(fixedRate = 10000, zone = "Europe/Minsk", runOnStartup = true, roles = {"admin"})
 * public void scheduledMessage(Set<Long> selectedUserIds) {
 *     // Logic for users with "admin" role
 * }
 * }
 * </pre>
 *
 * <p>
 * Attributes:
 * </p>
 *
 * <ul>
 *     <li><b>fixedRate</b>: The frequency, in milliseconds, at which the task should be executed.
 *     For example, 10000 means the task will be executed every 10 seconds.</li>
 *     <li><b>fixedDelay</b>: The delay, in milliseconds, between the completion of one task and the start of the next.
 *     Unlike <b>fixedRate</b>, the next execution will only start after the current execution finishes.</li>
 *     <li><b>cron</b>: The CRON expression used to schedule the task. If provided, it overrides the
 *     <b>fixedRate</b> and <b>fixedDelay</b> attributes.</li>
 *     <li><b>zone</b>: The time zone in which the task will run (e.g., "Europe/Minsk"). Defaults to "UTC".</li>
 *     <li><b>runOnStartup</b>: If set to true, the task will run once immediately after the application starts.</li>
 *     <li><b>roles</b>: A list of roles that users must have to trigger the task. The task will only be executed for users with
 *     one of the specified roles. For example, if roles = {"admin"}, the task will run only for users with the "admin" role.</li>
 *     <li><b>order</b>: The order in which the task will be executed. Lower values indicate higher priority. Defaults to
 *     {@link Integer#MAX_VALUE} (lowest priority).</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotScheduled {

    /**
     * The frequency, in milliseconds, at which the task should be executed.
     * For example, 10000 means the task will be executed every 10 seconds.
     */
    long fixedRate() default -1;

    /**
     * The delay, in milliseconds, between the completion of one task and the start of the next.
     * Unlike <b>fixedRate</b>, the next execution will only start after the current execution finishes.
     */
    long fixedDelay() default -1;

    /**
     * The CRON expression used to schedule the task. If provided, it overrides
     * the <b>fixedRate</b> and <b>fixedDelay</b> attributes.
     */
    String cron() default "";

    /**
     * The time zone in which the task will run. Defaults to "UTC".
     */
    String zone() default "UTC";

    /**
     * If set to true, the task will be executed immediately upon application startup.
     */
    boolean runOnStartup() default false;

    /**
     * A list of roles that users must have to trigger the task.
     * The task will only be executed for users with one of the specified roles.
     */
    String[] roles() default {"user"};

    /**
     * The order in which the task will be executed.
     * Lower values indicate higher priority. Defaults to {@link Integer#MAX_VALUE} (lowest priority).
     */
    int order() default Integer.MAX_VALUE;
}

