package dev.karmanov.library.service.handlers.media.voice;

import dev.karmanov.library.model.methodHolders.media.VoiceMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.util.Comparator;
import java.util.Set;

/**
 * Default implementation of {@link VoiceHandler} that handles voice messages from a Telegram bot.
 * <p>
 * This handler checks user roles, verifies if the action is expected, and
 * executes appropriate registered bot methods.
 * </p>
 */
public class DefaultVoiceHandler implements VoiceHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultVoiceHandler.class);
    private Executor methodExecutor;
    private BotCommandRegister register;
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

    /**
     * Processes a received voice message update
     * @param userAwaitingAction the set of expected user actions
     * @param update the Telegram {@link org.telegram.telegrambots.meta.api.objects.Update} containing the voice message
     */
    @Override
    public void handle(Set<String> userAwaitingAction, Update update) {
        Message message = update.getMessage();
        Voice voice = message.getVoice();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        int voiceDuration = voice.getDuration();

        logger.info("Handling voice: {}", voice);

        register.getVoiceMethods().stream()
                .filter(o->roleChecker.userHasAccess(userId,chatId,register.getSpecialAccessMethodHolders(o.getMethod())))
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o->voiceDuration >= o.getMinDurationSeconds() && voiceDuration <= o.getMaxDurationSeconds())
                .sorted(Comparator.comparingInt(VoiceMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for voice: {}", o.getMethod().getName(), voice);
                    methodExecutor.executeMethod(o.getMethod(), update);
                });
    }
}
