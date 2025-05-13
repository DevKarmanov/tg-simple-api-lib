package dev.karmanov.library.service.register.utils.media.voice;

import org.telegram.telegrambots.meta.api.objects.Voice;

/**
 * Interface for converting audio (voice) messages into text.
 * <p>
 * This interface defines a method for transcribing audio messages into text.
 * </p>
 */
public interface AudioTranscribe {

    /**
     * Converts a voice message to text.
     *
     * @param voice       The voice message to be transcribed.
     * @param languageCode The language code used for transcription (e.g., "en" for English).
     * @param chatId      The chatID of the user who sent the voice message.
     * @return The transcribed text from the voice message.
     */
    String voiceToText(Voice voice, String languageCode, Long chatId);
}

