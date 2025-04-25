package dev.karmanov.library.service.handlers.media.photo;

import dev.karmanov.library.model.methodHolders.media.PhotoMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DefaultPhotoHandler implements PhotoHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultPhotoHandler.class);
    private Executor methodExecutor;
    private BotCommandRegister register;
    private final DefaultAbsSender sender;
    private RoleChecker roleChecker;

    public DefaultPhotoHandler(DefaultAbsSender sender) {
        this.sender = sender;
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

    @Override
    public void handle(Set<String> userAwaitingAction, Update update, StateManager manager) {
        Message message = update.getMessage();
        Long userId = update.getMessage().getFrom().getId();
        List<PhotoSize> photos = message.getPhoto();

        PhotoSize largestPhoto = photos.get(photos.size() - 1);
        logger.info("Handling photo with file ID: {}", largestPhoto.getFileId());

        register.getBotPhotoMethods().stream()
                .filter(o->roleChecker.userHasAccess(userId,manager,register.getSpecialAccessMethodHolders(o.getMethod())))
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o -> {
                    double fileSize = largestPhoto.getFileSize() / 1024.0;
                    int fileWidth = largestPhoto.getWidth();
                    int fileHeight = largestPhoto.getHeight();

                    logger.debug("Photo size: {} KB, Width: {}, Height: {}", fileSize, fileWidth, fileHeight);

                    int gcd = gcd(fileWidth, fileHeight);
                    int simplifiedWidth = fileWidth / gcd;
                    int simplifiedHeight = fileHeight / gcd;
                    String aspectRatio = simplifiedWidth + ":" + simplifiedHeight;

                    String filePath;
                    GetFile getFileMethod = new GetFile();
                    getFileMethod.setFileId(largestPhoto.getFileId());

                    try {
                        File file = sender.execute(getFileMethod);
                        filePath = file.getFilePath();
                        logger.debug("Retrieved file path: {}", filePath);
                    } catch (TelegramApiException e) {
                        logger.error("Failed to retrieve file for photo ID: {}", largestPhoto.getFileId(), e);
                        throw new RuntimeException(e);
                    }

                    int lastDotIndex = filePath.lastIndexOf(".");
                    String format = "";
                    if (lastDotIndex != -1) {
                        format = filePath.substring(lastDotIndex + 1).toLowerCase(Locale.ROOT).strip();
                    }

                    boolean matchesSize = fileSize >= o.getMinFileSize() && fileSize <= o.getMaxFileSize();
                    boolean matchesDimensions = fileWidth >= o.getMinWidth() && fileHeight >= o.getMinHeight();
                    boolean matchesAspectRatio = o.getAspectRatio().isEmpty() || aspectRatio.equals(o.getAspectRatio());
                    boolean matchesFormat = o.getFormat().isEmpty() || format.equals(o.getFormat());

                    logger.debug("Checking matches for method: {} - Size: {}, Dimensions: {}, Aspect Ratio: {}, Format: {}",
                            o.getMethod().getName(), matchesSize, matchesDimensions, matchesAspectRatio, matchesFormat);

                    return matchesSize && matchesDimensions && matchesAspectRatio && matchesFormat;
                })
                .sorted(Comparator.comparingInt(PhotoMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for photo ID: {}", o.getMethod().getName(), largestPhoto.getFileId());
                    methodExecutor.executeMethod(o.getMethod(), update);
                });
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
