package dev.karmanov.library.service.notify.relevantModelErrorNotifier;

import java.util.Map;

/**
 * Sends a message when an error occurs while trying to find a relevant language model.
 */
public interface ExceptionFoundRelevantModelNotifier {
    /**
     * Sends a message when an error occurs while trying to find a relevant language model.
     *
     * @param chatId ID of the user to whom the message should be sent.
     * @param languageModels a map of available language models,
     *                       where the key is the language code (e.g., "ru") and the value is the path to the model (e.g., "path/to/ru/model").
     */
    void sendExceptionFoundRelevantModelMessage(Long chatId, Map<String, String> languageModels);

}

