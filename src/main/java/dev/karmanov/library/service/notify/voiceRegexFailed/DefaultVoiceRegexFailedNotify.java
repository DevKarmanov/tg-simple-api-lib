package dev.karmanov.library.service.notify.voiceRegexFailed;

import dev.karmanov.library.service.notify.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultVoiceRegexFailedNotify implements VoiceRegexFailedNotify{
    private static final Logger logger = LoggerFactory.getLogger(DefaultVoiceRegexFailedNotify.class);

    private final Notifier notifier;

    public DefaultVoiceRegexFailedNotify(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void sendRegexFailedMessage(Long chatId, String voiceText, String regex) {
        String message = String.format(
                """
                        ⚠️ Sorry, I couldn't recognize your voice message as a valid command.

                        🔊 Your input: "%s"
                        🧪 Expected to match: `%s`""",
                voiceText, regex
        );

        logger.info("Voice regex match failed for chatId={}. VoiceText='{}', Regex='{}'", chatId, voiceText, regex);

        try {
            notifier.sendMessage(chatId, message);
            logger.debug("Regex failure message successfully sent to chatId={}", chatId);
        } catch (Exception e) {
            logger.error("Failed to send regex failure message to chatId={}", chatId, e);
        }
    }
}
