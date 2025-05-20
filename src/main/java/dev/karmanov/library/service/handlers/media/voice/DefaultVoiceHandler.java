package dev.karmanov.library.service.handlers.media.voice;

import dev.karmanov.library.model.methodHolders.media.VoiceMethodHolder;
import dev.karmanov.library.service.notify.processingMessageNotifier.ProcessingMessageNotifier;
import dev.karmanov.library.service.notify.voiceRegexFailed.VoiceRegexFailedNotify;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.media.voice.AudioTranscribe;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Default implementation of {@link VoiceHandler} that handles voice messages from a Telegram bot.
 */
public class DefaultVoiceHandler implements VoiceHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultVoiceHandler.class);
    private Executor methodExecutor;
    private BotCommandRegister register;
    private RoleChecker roleChecker;
    private AudioTranscribe audioTranscribe;
    private ProcessingMessageNotifier notifier;
    private ExecutorService executorService;
    private VoiceRegexFailedNotify voiceRegexFailedNotify;

    @Autowired(required = false)
    public void setVoiceRegexFailedNotify(VoiceRegexFailedNotify voiceRegexFailedNotify) {
        this.voiceRegexFailedNotify = voiceRegexFailedNotify;
    }

    @Autowired(required = false)
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

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
    public void setAudioTranscribe(AudioTranscribe audioTranscribe) {
        this.audioTranscribe = audioTranscribe;
    }

    @Autowired(required = false)
    public void setProcessingMessageNotifier(ProcessingMessageNotifier notifier) {
        this.notifier = notifier;
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
                    if (o.isVoiceInterpreter()){
                        notifier.sendProcessingMessage(chatId);
                        Future<String> future = executorService.submit(() -> audioTranscribe.voiceToText(voice, o.getLanguageCode(),chatId));
                        try {
                            String resultText = future.get();
                            String regex = o.getRegex();

                            if (resultText.matches(regex)){
                                methodExecutor.executeMethod(o.getMethod(),chatId,update,resultText);
                            }else {
                                voiceRegexFailedNotify.sendRegexFailedMessage(chatId,resultText,regex);
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            logger.error("Error while transcribing audio", e);
                        }

                    }else{
                        methodExecutor.executeMethod(o.getMethod(),chatId,update);
                    }
                });
    }
}
