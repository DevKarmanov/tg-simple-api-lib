package dev.karmanov.library.service.handlers.media.document;

import dev.karmanov.library.model.methodHolders.media.DocumentMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

/**
 * Default implementation of {@link DocumentHandler}.
 * <p>
 * Responsible for handling incoming documents in a Telegram bot.
 * It retrieves the list of registered document-handling methods from {@link BotCommandRegister},
 * filters them based on the user's awaiting actions, access rights, document properties,
 * sorts them by their defined order,
 * and then executes the matching methods via the {@link Executor}.
 * </p>
 */
public class DefaultDocumentHandler implements DocumentHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultDocumentHandler.class);
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
     * Handles an incoming document update
     * @param userAwaitingAction set of actionNames the user is currently expected to perform
     * @param update the Telegram {@link org.telegram.telegrambots.meta.api.objects.Update} containing the document
     */
    @Override
    public void handle(Set<String> userAwaitingAction, Update update) {
        Message message = update.getMessage();
        Document document = message.getDocument();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        logger.info("Handling document: {}", document);

        double fileSize = document.getFileSize() / 1024.0;

        register.getDocumentMethods().stream()
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o->document.getFileName().matches(o.getFileNameRegex()))
                .filter(o->fileSize >= o.getMinFileSize() && fileSize <= o.getMaxFileSize())
                .filter(o-> Arrays.stream(o.getFileExtensions()).anyMatch(ex->ex.toLowerCase(Locale.ROOT).strip().equals(document.getFileName().substring(document.getFileName().lastIndexOf('.') + 1).toLowerCase(Locale.ROOT))))
                .filter(o->roleChecker.userHasAccess(userId,chatId,register.getSpecialAccessMethodHolders(o.getMethod())))
                .sorted(Comparator.comparingInt(DocumentMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for document: {}", o.getMethod().getName(), document);
                    methodExecutor.executeMethod(o.getMethod(), update);
                });
    }
}
