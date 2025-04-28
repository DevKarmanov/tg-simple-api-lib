package dev.karmanov.library.service.handlers.callback;

import dev.karmanov.library.model.message.TextType;
import dev.karmanov.library.model.methodHolders.TextMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.text.TextQualifier;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.Set;
/**
 * Default implementation of {@link CallBackHandler} that handles callback queries from a Telegram bot.
 * <p>
 * This handler performs the following steps:
 * <ul>
 *     <li>Retrieves registered callback-handling methods from {@link BotCommandRegister}.</li>
 *     <li>Checks user access rights using {@link RoleChecker}.</li>
 *     <li>Verifies if the action matches the user's expected actions.</li>
 *     <li>Validates the callback data format using {@link TextQualifier}.</li>
 *     <li>Sorts matching methods by their execution order.</li>
 *     <li>Executes each matching method via the {@link Executor}.</li>
 * </ul>
 * </p>
 */
public class DefaultCallBackHandler implements CallBackHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultCallBackHandler.class);
    private Executor methodExecutor;
    private BotCommandRegister register;
    private TextQualifier textTypeTextQualifier;

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

    @Autowired(required = false)
    public void setTextTypeQualifier(TextQualifier textQualifier){
        this.textTypeTextQualifier = textQualifier;
    }

    /**
     * Handles the incoming callback query
     * @param userAwaitingAction set of actionNames that the user is currently expected to perform
     * @param update the Telegram {@link org.telegram.telegrambots.meta.api.objects.Update} containing the callback query
     */
    @Override
    public void handle(Set<String> userAwaitingAction, Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callBackName = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Long userId = callbackQuery.getFrom().getId();
        logger.info("Handling callback with name: {}", callBackName);

        register.getBotTextMethods().stream()
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o -> textTypeTextQualifier.textTypeCheck(o, callBackName, TextType.CALLBACK_DATA))
                .filter(o->roleChecker.userHasAccess(userId,chatId,register.getSpecialAccessMethodHolders(o.getMethod())))
                .sorted(Comparator.comparingInt(TextMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for callback: {}", o.getMethod().getName(), callBackName);
                    methodExecutor.executeMethod(o.getMethod(), update);
                });
    }
}
