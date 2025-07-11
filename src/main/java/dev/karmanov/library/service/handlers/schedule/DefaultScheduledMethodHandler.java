package dev.karmanov.library.service.handlers.schedule;

import dev.karmanov.library.model.methodHolders.ScheduledMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Default implementation of {@link ScheduledHandler} that manages and schedules tasks in a Telegram bot.
 * <p>
 * This handler schedules tasks based on cron expressions, fixed rate, or fixed delay.
 * It also ensures that only the users with appropriate roles will be selected for task execution.
 * </p>
 */
public class DefaultScheduledMethodHandler implements ScheduledHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultScheduledMethodHandler.class);
    private final BotCommandRegister register;
    private final ExecutorService executorService;
    private final Executor methodExecutor;
    private final TaskScheduler scheduler;
    private final StateManager manager;

    public DefaultScheduledMethodHandler(BotCommandRegister register, ExecutorService executorService, Executor methodExecutor, TaskScheduler scheduler, StateManager manager) {
        this.register = register;
        this.executorService = executorService;
        this.methodExecutor = methodExecutor;
        this.scheduler = scheduler;
        this.manager = manager;
    }


    /**
     * Starts the scheduling of tasks based on the defined scheduled methods.
     * <p>
     * This method reads the scheduled methods, sorts them by order, and schedules them to run according to the configuration
     * (cron expression, fixed rate, or fixed delay)
     * </p>
     */
    @Override
    public void startSchedule() {
        register.getScheduledMethods()
                .stream()
                .sorted(Comparator.comparingInt(ScheduledMethodHolder::getOrder))
                .forEach(this::schedule);

        register.getScheduledMethods()
                .stream()
                .filter(ScheduledMethodHolder::isRunOnStartup)
                .forEach(scheduledMethod -> {
                    Runnable task = getTask(scheduledMethod);
                    executorService.execute(task);
                });
        logger.info("Scheduled tasks started after first message");
    }

    private void schedule(ScheduledMethodHolder scheduledMethod) {
        Runnable task = getTask(scheduledMethod);

        if (!scheduledMethod.getCron().isBlank()) {
            scheduler.schedule(
                    task,
                    new CronTrigger(scheduledMethod.getCron(), ZoneId.of(scheduledMethod.getZone()))
            );
            return;
        }

        if (scheduledMethod.getFixedRate() > 0) {
            scheduler.scheduleAtFixedRate(
                    task,
                    Instant.now(),
                    Duration.ofMillis(scheduledMethod.getFixedRate())
            );
            return;
        }

        if (scheduledMethod.getFixedDelay() > 0) {
            scheduler.scheduleWithFixedDelay(
                    task,
                    Instant.now(),
                    Duration.ofMillis(scheduledMethod.getFixedDelay())
            );
        }

    }

    private Runnable getTask(ScheduledMethodHolder scheduledMethod) {
        return () -> {
            Set<Long> userIds = manager.getAllUserIds();
            Set<Long> selectedUserIds = new HashSet<>();

            Set<String> methodRoles = scheduledMethod.getRoles();
            String methodName = scheduledMethod.getMethod().getName();

            logger.info("⏰ Scheduled method '{}' triggered at {}", methodName, Instant.now());
            logger.info("📋 Found userIds: {}", userIds);
            logger.info("🔐 Required roles: {}", methodRoles);

            for (Long userId : userIds) {
                Set<String> userRoles = manager.getUserRoles(userId);
                if (methodRoles.isEmpty() || userRoles.stream().anyMatch(methodRoles::contains)) {
                    selectedUserIds.add(userId);
                    logger.info("✅ User {} matches roles {} -> selected", userId, userRoles);
                } else {
                    logger.info("❌ User {} roles {} do not match required {}", userId, userRoles, methodRoles);
                }
            }

            logger.info("📨 Final selectedUserIds for method '{}': {}", methodName, selectedUserIds);

            try {
                Method method = scheduledMethod.getMethod();
                methodExecutor.executeMethod(method, selectedUserIds);
            } catch (Exception e) {
                logger.error("💥 Error while executing scheduled method '{}': {}", methodName, e.getMessage(), e);
            }
        };
    }

}
