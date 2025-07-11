package dev.karmanov.library.service.handlers.media.video;

import dev.karmanov.library.model.methodHolders.media.VideoMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.utils.media.voice.AudioTranscribe;
import dev.karmanov.library.service.register.utils.media.voice.executor.InterpreterExecutor;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

public class DefaultVideoHandler implements VideoHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultVideoHandler.class);
    private final BotCommandRegister register;
    private final RoleChecker roleChecker;
    private final AudioTranscribe audioTranscribe;
    private final InterpreterExecutor interpreterExecutor;

    public DefaultVideoHandler(BotCommandRegister register,
                               RoleChecker roleChecker,
                               AudioTranscribe audioTranscribe,
                               InterpreterExecutor interpreterExecutor) {
        this.register = register;
        this.roleChecker = roleChecker;
        this.audioTranscribe = audioTranscribe;
        this.interpreterExecutor = interpreterExecutor;
    }

    @Override
    public void handle(Set<String> userAwaitingAction, Update update) {
        Message message = update.getMessage();
        Video video = message.getVideo();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        int duration = video.getDuration();
        int width = video.getWidth();
        int height = video.getHeight();
        long fileSize = video.getFileSize();

        String extension;
        String fileName = video.getFileName();
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf('.') + 1)
                    .toLowerCase(Locale.ROOT)
                    .strip();
        } else {
            extension = "unknown";
        }

        logger.info("Handling video: {}", video);

        register.getBotVideoMethods().stream()
                .filter(o -> userAwaitingAction.contains(o.getActionName()))
                .filter(o -> roleChecker.userHasAccess(userId, chatId, register.getSpecialAccessMethodHolders(o.getMethod())))
                .sorted(Comparator.comparingInt(VideoMethodHolder::getOrder))
                .filter(o -> duration >= o.getMinDurationSeconds() && duration <= o.getMaxDurationSeconds())
                .filter(o -> resolutionCheck(o.getMinResolution(), o.getMaxResolution(), width, height))
                .filter(o -> {
                    double minFileSizeBytes = o.getMinFileSize() * 1024;
                    double maxFileSizeBytes = o.getMaxFileSize() * 1024;
                    return fileSize >= minFileSizeBytes && fileSize <= maxFileSizeBytes;
                })
                .filter(o -> Arrays.stream(o.getFormat())
                        .map(f -> f.toLowerCase(Locale.ROOT).strip())
                        .anyMatch(f -> f.equals(extension)))
                .forEach(o->{
                    logger.info("Executing method: {} for video: {}", o.getMethod().getName(), video);
                    interpreterExecutor.handleInterpretation(
                            video,
                            update,
                            chatId,
                            o.getMethod(),
                            v -> o.isTextInterpreter(),
                            v -> o.getRegex(),
                            v -> audioTranscribe.voiceToText(v, o.getLanguageCode(), chatId)
                    );
                });

    }

    private boolean resolutionCheck(String minResolution, String maxResolution, int currentWidth, int currentHeight) {
        if (minResolution == null || maxResolution == null ||
                minResolution.isBlank() || maxResolution.isBlank()) {
            logger.debug("Resolution check skipped: minResolution='{}', maxResolution='{}'", minResolution, maxResolution);
            return true;
        }

        String[] minParts = minResolution.split("x");
        int minW = Integer.parseInt(minParts[0]);
        int minH = Integer.parseInt(minParts[1]);

        String[] maxParts = maxResolution.split("x");
        int maxW = Integer.parseInt(maxParts[0]);
        int maxH = Integer.parseInt(maxParts[1]);

        int currMin = Math.min(currentWidth, currentHeight);
        int currMax = Math.max(currentWidth, currentHeight);

        int rangeMin = Math.min(minW, minH);
        int rangeMax = Math.max(maxW, maxH);

        boolean result = currMin >= rangeMin && currMax <= rangeMax;

        logger.debug("Resolution check: [{}x{}] → current=[{}x{}], min=[{}x{}], max=[{}x{}], result={}",
                currentWidth, currentHeight, currMin, currMax, rangeMin, rangeMin, rangeMax, rangeMax, result);

        return result;
    }
}
