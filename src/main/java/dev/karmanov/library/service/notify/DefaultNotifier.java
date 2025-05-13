package dev.karmanov.library.service.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Default implementation of the {@link Notifier} interface for sending messages to users.
 * <p>
 * This class uses the {@link AbsSender} from the Telegram API to send messages.
 * </p>
 *
 * @see Notifier
 * @see AbsSender
 */
public class DefaultNotifier implements Notifier {
    private final AbsSender sender;

    private static final Logger logger = LoggerFactory.getLogger(DefaultNotifier.class);

    /**
     * Constructor for DefaultNotifier.
     *
     * @param sender The {@link AbsSender} instance used to send messages.
     */
    public DefaultNotifier(AbsSender sender) {
        this.sender = sender;
    }

    /**
     * Sends a message to the user.
     *
     * @param chatId The ID of the user to whom the message will be sent.
     * @param text   The content of the message to be sent.
     */
    @Override
    public void sendMessage(Long chatId, String text) {
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


