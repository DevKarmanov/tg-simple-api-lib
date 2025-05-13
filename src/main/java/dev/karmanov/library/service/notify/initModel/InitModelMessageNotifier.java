package dev.karmanov.library.service.notify.initModel;

/**
 * Sends a notification to the user indicating that the language model is being initialized.
 */
public interface InitModelMessageNotifier {
    void sendInitModelMessage(Long chatId);
}
