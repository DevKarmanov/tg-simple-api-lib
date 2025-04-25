package dev.karmanov.library.service.handlers.denied;

import dev.karmanov.library.model.user.UserState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Set;
import java.util.stream.Collectors;

public class DefaultAccessNotifier implements AccessNotifier {
    private final DefaultAbsSender sender;
    private static final Logger logger = LoggerFactory.getLogger(DefaultAccessNotifier.class);

    public DefaultAccessNotifier(DefaultAbsSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendAccessDeniedMessage(Long chatId, Set<String> currentUserRoles) {
        String message = "❌ You do not have permission to use this command.\n"
                + "Your current roles: " + (currentUserRoles.isEmpty() ? "none" : String.join(", ", currentUserRoles));

        logger.warn("Access denied for user {}. Current roles: {}", chatId, currentUserRoles);
        sendMessage(chatId, message);
    }

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

