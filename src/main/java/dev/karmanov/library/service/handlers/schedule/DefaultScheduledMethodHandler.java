package dev.karmanov.library.service.handlers.schedule;

import dev.karmanov.library.model.methodHolders.ScheduledMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

public class DefaultScheduledMethodHandler implements ScheduledHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultScheduledMethodHandler.class);
    private BotCommandRegister register;
    private Executor methodExecutor;
    private TaskScheduler scheduler;
    private StateManager manager;

    @Autowired(required = false)
    public void setManager(StateManager manager){
        this.manager = manager;
    }

    @Autowired(required = false)
    public void setScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Autowired(required = false)
    public void setRegister(BotCommandRegister register){
        this.register = register;
    }

    @Autowired(required = false)
    public void setMethodExecutor(Executor executor){
        this.methodExecutor = executor;
    }

    @Override
    public void startSchedule() {
        register.getScheduledMethods()
                .stream()
                .sorted(Comparator.comparingInt(ScheduledMethodHolder::getOrder))
                .forEach(this::schedule);
        logger.info("Scheduled tasks started after first message");
    }

    //todo собрать сразу все методы, а потом запланировать их отправку, а не находить их перед отправкой
    private void schedule(ScheduledMethodHolder scheduledMethod) {
        Runnable task = () -> {
            Set<Long> userIds = manager.getAllUserIds();
            Set<Long> selectedUserIds = new HashSet<>();

            for (Long userId : userIds) {
                Set<String> methodRoles = scheduledMethod.getRoles();
                if (methodRoles.isEmpty() || manager.getUserRoles(userId).stream().anyMatch(methodRoles::contains)) {
                   selectedUserIds.add(userId);
                }
            }
            Method method = scheduledMethod.getMethod();
            Object bean = register.getBean(method);
            Method proxy = ReflectionUtils.findMethod(bean.getClass(), method.getName(), method.getParameterTypes());

            methodExecutor.executeMethod(proxy, selectedUserIds);
        };

        if (scheduledMethod.isRunOnStartup()){
            task.run();
        }

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
}
