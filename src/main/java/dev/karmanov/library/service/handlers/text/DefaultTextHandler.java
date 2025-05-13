package dev.karmanov.library.service.handlers.text;

import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.TextMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.text.TextQualifier;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

/**
 * Default implementation of {@link TextHandler} that handles text messages or commands from a Telegram bot.
 * <p>
 * This handler processes text commands, verifies if the action is expected, checks if the user has the necessary
 * roles, and executes the appropriate registered bot methods based on the provided text and user permissions.
 * </p>
 */
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

    /**
     * Processes the received text message or command update
     * @param userAwaitingAction the set of expected user actions
     * @param update the Telegram {@link org.telegram.telegrambots.meta.api.objects.Update} containing the text message
     */
    @Override
    public void handle(Set<String> userAwaitingAction, Update update) {
        Message message = update.getMessage();
        String text = message.getText().toLowerCase(Locale.ROOT).strip();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        logger.info("Received text command: {}", text);

        register.getBotTextMethods().stream()
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o -> textTypeTextQualifier.textTypeCheck(o, text, TextType.TEXT))
                .filter(o->roleChecker.userHasAccess(userId,chatId,register.getSpecialAccessMethodHolders(o.getMethod())))
                .sorted(Comparator.comparingInt(TextMethodHolder::getOrder))
                .forEach(o -> {
                    logger.debug("Executing method: {} for command: {}", o.getMethod().getName(), text);
                    methodExecutor.executeMethod(o.getMethod(), update);
                });

        logger.info("Finished processing text command: {}", text);
    }
}
