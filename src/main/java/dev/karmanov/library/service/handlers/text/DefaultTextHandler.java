package dev.karmanov.library.service.handlers.text;

import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.TextMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.text.TextQualifier;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import dev.karmanov.library.service.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class DefaultTextHandler implements TextHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultTextHandler.class);

    private BotCommandRegister register;
    private TextQualifier textTypeTextQualifier;
    private Executor methodExecutor;
    private RoleChecker roleChecker;

    @Autowired(required = false)
    public void setRoleChecker(RoleChecker roleChecker) {
        this.roleChecker = roleChecker;
    }

    @Autowired(required = false)
    public void setTextTypeQualifier(TextQualifier textQualifier){
        this.textTypeTextQualifier = textQualifier;
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
    public void handle(List<String> userAwaitingAction, Update update, StateManager manager) {
        String text = update.getMessage().getText().toLowerCase(Locale.ROOT).strip();
        Long userId = update.getMessage().getFrom().getId();
        logger.info("Received text command: {}", text);

        register.getBotTextMethods().stream()
                .filter(o->roleChecker.userHasAccess(userId,manager,register.getSpecialAccessMethodHolders(o.getMethod())))
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o -> textTypeTextQualifier.textTypeCheck(o, text, TextType.TEXT))
                .sorted(Comparator.comparingInt(TextMethodHolder::getOrder))
                .forEach(o -> {
                    logger.debug("Executing method: {} for command: {}", o.getMethod().getName(), text);
                    methodExecutor.executeMethod(o.getMethod(), update);
                });

        logger.info("Finished processing text command: {}", text);
    }
}
