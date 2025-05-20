package dev.karmanov.library.service.handlers.media;

import dev.karmanov.library.model.message.MediaType;
import dev.karmanov.library.model.methodHolders.media.MediaMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.media.MediaQualifier;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.Set;

/**
 * Default implementation of {@link MediaHandler} that handles media messages from a Telegram bot.
 * <p>
 * This handler determines the type of media in the incoming update, checks user roles,
 * verifies expected actions, and executes the appropriate registered bot methods based on media type
 * and user permissions.
 * </p>
 */
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

    /**
     * Processes a received media message
     * @param userAwaitingAction the set of expected user actions
     * @param update the Telegram {@link org.telegram.telegrambots.meta.api.objects.Update} containing the media message
     */
    @Override
    public void handle(Set<String> userAwaitingAction, Update update) {
        MediaType type = mediaAvailabilityQualifier.hasMedia(update);
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        logger.info("Handling media message of type: {}", type);

        register.getBotMediaMethods().stream()
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o -> o.getMediaType().equals(type))
                .filter(o->roleChecker.userHasAccess(userId,chatId,register.getSpecialAccessMethodHolders(o.getMethod())))
                .sorted(Comparator.comparingInt(MediaMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for media type: {}", o.getMethod().getName(), type);
                    methodExecutor.executeMethod(o.getMethod(),chatId,update);
                });
    }
}
