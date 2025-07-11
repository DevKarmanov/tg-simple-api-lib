package dev.karmanov.library.service.handlers.media.voice;

import dev.karmanov.library.model.methodHolders.media.VoiceMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.utils.media.voice.AudioTranscribe;
import dev.karmanov.library.service.register.utils.media.voice.executor.InterpreterExecutor;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.util.Comparator;
import java.util.Set;

/**
 * Default implementation of {@link VoiceHandler} that handles voice messages from a Telegram bot.
 */
public class DefaultVoiceHandler implements VoiceHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultVoiceHandler.class);
    private final BotCommandRegister register;
    private final RoleChecker roleChecker;
    private final AudioTranscribe audioTranscribe;
    private final InterpreterExecutor interpreterExecutor;

    public DefaultVoiceHandler(BotCommandRegister register, RoleChecker roleChecker, AudioTranscribe audioTranscribe, InterpreterExecutor interpreterExecutor) {
        this.register = register;
        this.roleChecker = roleChecker;
        this.audioTranscribe = audioTranscribe;
        this.interpreterExecutor = interpreterExecutor;
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
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o->voiceDuration >= o.getMinDurationSeconds() && voiceDuration <= o.getMaxDurationSeconds())
                .filter(o->roleChecker.userHasAccess(userId,chatId,register.getSpecialAccessMethodHolders(o.getMethod())))
                .sorted(Comparator.comparingInt(VoiceMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for voice: {}", o.getMethod().getName(), voice);
                    interpreterExecutor.handleInterpretation(
                            voice,
                            update,
                            chatId,
                            o.getMethod(),
                            v -> o.isVoiceInterpreter(),
                            v -> o.getRegex(),
                            v -> audioTranscribe.voiceToText(v, o.getLanguageCode(), chatId)
                    );
                });
    }
}
