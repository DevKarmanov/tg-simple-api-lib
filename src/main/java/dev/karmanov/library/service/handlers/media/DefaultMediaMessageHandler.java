package dev.karmanov.library.service.handlers.media;

import dev.karmanov.library.model.message.MediaType;
import dev.karmanov.library.model.methodHolders.media.MediaMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.media.MediaQualifier;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.Set;

public class DefaultMediaMessageHandler implements MediaHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMediaMessageHandler.class);

    private MediaQualifier mediaAvailabilityQualifier;
    private BotCommandRegister register;

    private Executor methodExecutor;

    private RoleChecker roleChecker;

    @Autowired(required = false)
    public void setRoleChecker(RoleChecker roleChecker) {
        this.roleChecker = roleChecker;
    }

    @Autowired(required = false)
    public void setRegister(BotCommandRegister register){
        this.register = register;
    }

    @Autowired(required = false)
    public void setMethodExecutor(Executor executor){
        this.methodExecutor = executor;
    }

    @Autowired(required = false)
    public void setMediaAvailabilityQualifier(MediaQualifier qualifier){
        this.mediaAvailabilityQualifier = qualifier;
    }
    @Override
    public void handle(Set<String> userAwaitingAction, Update update, StateManager manager) {
        MediaType type = mediaAvailabilityQualifier.hasMedia(update);
        Long userId = update.getMessage().getFrom().getId();
        logger.info("Handling media message of type: {}", type);

        register.getBotMediaMethods().stream()
                .filter(o->roleChecker.userHasAccess(userId,manager,register.getSpecialAccessMethodHolders(o.getMethod())))
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o -> o.getMediaType().equals(type))
                .sorted(Comparator.comparingInt(MediaMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for media type: {}", o.getMethod().getName(), type);
                    methodExecutor.executeMethod(o.getMethod(), update);
                });
    }
}
