package dev.karmanov.library.service.handlers.media.sticker;

import dev.karmanov.library.model.methodHolders.media.StickerMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

public class DefaultStickerHandler implements StickerHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultStickerHandler.class);
    private final Executor methodExecutor;
    private final BotCommandRegister register;
    private final RoleChecker roleChecker;

    public DefaultStickerHandler(Executor methodExecutor, BotCommandRegister register, RoleChecker roleChecker) {
        this.methodExecutor = methodExecutor;
        this.register = register;
        this.roleChecker = roleChecker;
    }

    @Override
    public void handle(Set<String> userAwaitingAction, Update update) {
        Message message = update.getMessage();
        Sticker sticker = message.getSticker();

        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        register.getBotStickerMethods().stream()
                .peek(o -> logger.debug("Checking method: {}", o.getMethod().getName()))
                .filter(o -> {
                    boolean match = userAwaitingAction.contains(o.getActionName());
                    logger.debug("Action name match for '{}': {}", o.getMethod().getName(), match);
                    return match;
                })
                .filter(o -> {
                    boolean access = roleChecker.userHasAccess(userId, chatId, register.getSpecialAccessMethodHolders(o.getMethod()));
                    logger.debug("User access for '{}': {}", o.getMethod().getName(), access);
                    return access;
                })
                .sorted(Comparator.comparingInt(StickerMethodHolder::getOrder))
                .filter(o -> {
                    String[] filterEmojis = o.getEmoji();
                    String stickerEmoji = sticker.getEmoji();

                    if (filterEmojis.length == 0) {
                        logger.debug("No emoji filter for '{}'", o.getMethod().getName());
                        return true;
                    }

                    if (stickerEmoji == null) {
                        logger.debug("Sticker has no emoji, emoji filter failed for '{}'", o.getMethod().getName());
                        return false;
                    }

                    boolean match = Arrays.asList(filterEmojis).contains(stickerEmoji);
                    logger.debug("Emoji filter for '{}': {} (sticker emoji: '{}', allowed: {})",
                            o.getMethod().getName(), match, stickerEmoji, Arrays.toString(filterEmojis));
                    return match;
                })
                .filter(o -> {
                    if (!o.isCustomEmoji()) {
                        logger.debug("Custom emoji not required for '{}'", o.getMethod().getName());
                        return true;
                    }
                    boolean isCustom = isCustomEmoji(sticker);
                    logger.debug("Custom emoji check for '{}': {}", o.getMethod().getName(), isCustom);
                    if (isCustom) {
                        return true;
                    }
                    boolean maybeCustom = sticker.getSetName() == null;
                    if (maybeCustom) {
                        logger.debug("Sticker with no setName might be custom for '{}'", o.getMethod().getName());
                    }
                    return maybeCustom;
                })
                .filter(o -> {
                    if (o.getPackageName().length == 0) {
                        logger.debug("No package name filter for '{}'", o.getMethod().getName());
                        return true;
                    }
                    boolean match = sticker.getSetName() != null && Arrays.stream(o.getPackageName()).anyMatch(p -> p.equals(sticker.getSetName()));
                    logger.debug("Package name filter for '{}': {} (sticker set: '{}', allowed: {})",
                            o.getMethod().getName(), match, sticker.getSetName(), Arrays.toString(o.getPackageName()));
                    return match;
                })
                .filter(o -> {
                    if (o.getType().length == 0) {
                        logger.debug("No type filter for '{}'", o.getMethod().getName());
                        return true;
                    }
                    String stickerType = sticker.getType();
                    boolean match = stickerType != null && Arrays.stream(o.getType()).anyMatch(t -> t.name().equalsIgnoreCase(stickerType));
                    logger.debug("Type filter for '{}': {} (sticker type: '{}', allowed: {})",
                            o.getMethod().getName(), match, stickerType, Arrays.toString(o.getType()));
                    return match;
                })
                .filter(o -> {
                    if (o.getFileUniqueId().length == 0) {
                        logger.debug("No fileUniqueId filter for '{}'", o.getMethod().getName());
                        return true;
                    }
                    String fileUniqueId = sticker.getFileUniqueId();
                    boolean match = Arrays.asList(o.getFileUniqueId()).contains(fileUniqueId);
                    logger.debug("FileUniqueId filter for '{}': {} (fileUniqueId: '{}', allowed: {})",
                            o.getMethod().getName(), match, fileUniqueId, Arrays.toString(o.getFileUniqueId()));
                    return match;
                })
                .filter(o -> {
                    int width = sticker.getWidth();
                    int height = sticker.getHeight();
                    boolean match =
                            (o.getMinWidth() == -1 || width >= o.getMinWidth()) &&
                                    (o.getMinHeight() == -1 || height >= o.getMinHeight()) &&
                                    (o.getMaxWidth() == -1 || width <= o.getMaxWidth()) &&
                                    (o.getMaxHeight() == -1 || height <= o.getMaxHeight());
                    logger.debug("Size filter for '{}': {} (sticker size: {}x{}, constraints: minW={}, minH={}, maxW={}, maxH={})",
                            o.getMethod().getName(), match, width, height,
                            o.getMinWidth(), o.getMinHeight(), o.getMaxWidth(), o.getMaxHeight());
                    return match;
                })
                .forEach(o -> {
                    logger.info("Executing method: {} for sticker ID: {}", o.getMethod().getName(), sticker.getFileId());
                    methodExecutor.executeMethod(o.getMethod(), chatId, update);
                });

    }


    private boolean isCustomEmoji(Sticker sticker){
        return sticker.getCustomEmojiId()!=null;
    }
}
