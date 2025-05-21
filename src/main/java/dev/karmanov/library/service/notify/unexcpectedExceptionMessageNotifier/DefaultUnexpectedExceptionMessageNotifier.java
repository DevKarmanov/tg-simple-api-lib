package dev.karmanov.library.service.notify.unexcpectedExceptionMessageNotifier;

import dev.karmanov.library.service.notify.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Default implementation of {@link UnexpectedExceptionNotifier}.
 * Logs the exception and optionally notifies the user via {@link Notifier}.
 */
public class DefaultUnexpectedExceptionMessageNotifier implements UnexpectedExceptionNotifier{
    private static final Logger logger = LoggerFactory.getLogger(DefaultUnexpectedExceptionMessageNotifier.class);
    private Notifier notifier;

    @Autowired(required = false)
    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }
    public <T extends Exception> void sendUnexpectedExceptionMessage(Long chatId, T exception) {
        logger.error("Произошло непредвиденное исключение для пользователя с chatId={}: {}", chatId, exception.getMessage(), exception);

        String message = "Произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.";

        if (notifier != null) {
            notifier.sendMessage(chatId, message);
        } else {
            logger.warn("Notifier не установлен. Сообщение пользователю не отправлено.");
        }
    }
}
