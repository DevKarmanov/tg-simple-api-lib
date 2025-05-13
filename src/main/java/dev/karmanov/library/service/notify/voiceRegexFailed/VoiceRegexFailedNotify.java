package dev.karmanov.library.service.notify.voiceRegexFailed;

/**
 * Sends a message when the transcribed voice text does not match the expected regex pattern.
 */
public interface VoiceRegexFailedNotify {

    void sendRegexFailedMessage(Long chatId, String voiceText, String regex);
}

