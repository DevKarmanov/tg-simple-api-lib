package dev.karmanov.library.service.register.utils.media.voice.executor;


import dev.karmanov.library.service.notify.processingMessageNotifier.ProcessingMessageNotifier;
import dev.karmanov.library.service.notify.voiceRegexFailed.VoiceRegexFailedNotify;
import dev.karmanov.library.service.register.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.function.Function;


public class DefaultInterpreterExecutor implements InterpreterExecutor{
    private final Executor methodExecutor;
    private final ProcessingMessageNotifier notifier;
    private final ExecutorService executorService;
    private final VoiceRegexFailedNotify voiceRegexFailedNotify;
    private static final Logger logger = LoggerFactory.getLogger(DefaultInterpreterExecutor.class);

    public DefaultInterpreterExecutor(Executor methodExecutor,
                                      ProcessingMessageNotifier notifier,
                                      ExecutorService executorService,
                                      VoiceRegexFailedNotify voiceRegexFailedNotify) {
        this.methodExecutor = methodExecutor;
        this.notifier = notifier;
        this.executorService = executorService;
        this.voiceRegexFailedNotify = voiceRegexFailedNotify;
    }

    @Override
    public <T> void handleInterpretation(
            T media,
            Update update,
            Long chatId,
            Method method,
            Function<T, Boolean> isInterpreter,
            Function<T, String> getRegex,
            Function<T, String> transcriber
    ) {
        logger.info("Executing method: {} for media: {}", method.getName(), media);

        if (isInterpreter.apply(media)) {
            notifier.sendProcessingMessage(chatId);
            Future<String> future = executorService.submit(() -> transcriber.apply(media));
            try {
                String resultText = future.get();
                logger.debug("Transcribed result: {}", resultText);
                String regex = getRegex.apply(media);

                if (resultText.matches(regex)) {
                    execute(method, chatId, update, resultText);
                } else {
                    voiceRegexFailedNotify.sendRegexFailedMessage(chatId, resultText, regex);
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while transcribing media", e);
            }
        } else {
            execute(method, chatId, update);
        }
    }

    private void execute(Method method, Long chatId, Update update, String... args) {
        methodExecutor.executeMethod(method, chatId, update, args.length > 0 ? args[0] : null);
    }

}
