package dev.karmanov.library.service.notify.accessDenied;

import dev.karmanov.library.service.notify.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class DefaultAccessDeniedNotifier implements AccessDeniedNotifier{
    private Notifier notifier;

    @Autowired(required = false)
    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    private static final Logger logger = LoggerFactory.getLogger(DefaultAccessDeniedNotifier.class);

    @Override
    public void sendAccessDeniedMessage(Long chatId, Set<String> currentUserRoles) {
        String message = "❌ You do not have permission to use this command.\n"
                + "Your current roles: " + (currentUserRoles.isEmpty() ? "none" : String.join(", ", currentUserRoles));

        logger.warn("Access notify for user {}. Current roles: {}", chatId, currentUserRoles);
        notifier.sendMessage(chatId,message);
    }
}
