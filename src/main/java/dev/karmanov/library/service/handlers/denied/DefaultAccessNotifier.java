package dev.karmanov.library.service.handlers.denied;

import dev.karmanov.library.model.user.UserState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link AccessNotifier}.
 * <p>
 * Sends notifications to users when they attempt unauthorized actions
 * or perform unexpected actions in a Telegram bot.
 * </p>
 */
public class DefaultAccessNotifier implements AccessNotifier {
    private final DefaultAbsSender sender;
    private static final Logger logger = LoggerFactory.getLogger(DefaultAccessNotifier.class);

    public DefaultAccessNotifier(DefaultAbsSender sender) {
        this.sender = sender;
    }

    /**
     * Sends an "access denied" message to the specified chat.
     * <p>
     * The message informs the user that they do not have permission to execute
     * the requested command and lists their current roles.
     * </p>
     *
     * @param chatId the ID of the chat to send the message to
     * @param currentUserRoles the set of roles the user currently has
     */
    @Override
    public void sendAccessDeniedMessage(Long chatId, Set<String> currentUserRoles) {
        String message = "❌ You do not have permission to use this command.\n"
                + "Your current roles: " + (currentUserRoles.isEmpty() ? "none" : String.join(", ", currentUserRoles));

        logger.warn("Access denied for user {}. Current roles: {}", chatId, currentUserRoles);
        sendMessage(chatId, message);
    }

    /**
     * Sends a message to the specified chat when the user performs an unexpected action.
     * <p>
     * The message informs the user about their current state(s) to help them understand
     * the bot's expected behavior.
     * </p>
     *
     * @param chatId the ID of the chat to send the message to
     * @param currentUserStates the set of states the user is currently in
     */
    @Override
    public void sendUnexpectedActionMessage(Long chatId, Set<UserState> currentUserStates) {
        String states = currentUserStates.isEmpty()
                ? "unknown"
                : currentUserStates.stream()
                .map(UserState::name)
                .collect(Collectors.joining(", "));

        String message = "⚠️ I wasn't expecting that action.\n"
                + "Your current state: " + states;

        logger.warn("Unexpected action from user {}. Current states: {}", chatId, currentUserStates);
        sendMessage(chatId, message);
    }

    private void sendMessage(Long chatId, String text) {
        try {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId.toString());
            msg.setText(text);
            sender.execute(msg);
        } catch (Exception e) {
            logger.error("Failed to send message to user {}: {}", chatId, e.getMessage(), e);
        }
    }
}

