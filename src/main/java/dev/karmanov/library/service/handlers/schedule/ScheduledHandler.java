package dev.karmanov.library.service.handlers.schedule;

/**
 * Interface for handling scheduled tasks in a bot framework.
 * <p>
 * When a scheduled task is executed, the list of user IDs (`selectedUserIds`), which corresponds to
 * users who meet the role requirements defined in the task's configuration, is passed as a parameter to
 * the method being invoked. This ensures that the task is executed only for the relevant users based on
 * their roles.
 * </p>
 */
public interface ScheduledHandler {
    /**
     * Starts scheduling tasks defined in the system.
     * <p>
     * This method iterates through the registered scheduled methods and schedules them based on
     * their configuration, which may include cron expressions, fixed rate, or fixed delay.
     * The tasks are then executed according to the defined schedule.
     * </p>
     * <p>
     * The list of user IDs (`selectedUserIds`), representing users with the required roles, will be passed
     * as a parameter to the scheduled method. This allows the method to be executed specifically for the
     * relevant users.
     * </p>
     */
    void startSchedule();
}
