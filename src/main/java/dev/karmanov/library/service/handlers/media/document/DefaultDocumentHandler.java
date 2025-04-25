package dev.karmanov.library.service.handlers.media.document;

import dev.karmanov.library.model.methodHolders.media.DocumentMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

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

    @Override
    public void handle(Set<String> userAwaitingAction, Update update, StateManager manager) {
        Document document = update.getMessage().getDocument();
        Long userId = update.getMessage().getFrom().getId();
        logger.info("Handling document: {}", document);

        double fileSize = document.getFileSize() / 1024.0;

        register.getDocumentMethods().stream()
                .filter(o->roleChecker.userHasAccess(userId,manager,register.getSpecialAccessMethodHolders(o.getMethod())))
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o->document.getFileName().matches(o.getFileNameRegex()))
                .filter(o->fileSize >= o.getMinFileSize() && fileSize <= o.getMaxFileSize())
                .filter(o-> Arrays.stream(o.getFileExtensions()).anyMatch(ex->ex.toLowerCase(Locale.ROOT).strip().equals(document.getFileName().substring(document.getFileName().lastIndexOf('.') + 1).toLowerCase(Locale.ROOT))))
                .sorted(Comparator.comparingInt(DocumentMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for document: {}", o.getMethod().getName(), document);
                    methodExecutor.executeMethod(o.getMethod(), update);
                });
    }
}
