package dev.karmanov.library.service.notify.unexpectedAction;

import dev.karmanov.library.model.user.UserState;
import dev.karmanov.library.service.notify.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class DefaultUnexpectedActionNotifier implements UnexpectedActionNotifier {
    private static final Logger logger = LoggerFactory.getLogger(DefaultUnexpectedActionNotifier.class);

    private final Notifier notifier;

    public DefaultUnexpectedActionNotifier(Notifier notifier) {
        this.notifier = notifier;
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
        notifier.sendMessage(chatId,message);
    }
}
