package dev.karmanov.library.service.notify.unexcpectedExceptionMessageNotifier;

/**
 * Notifies the user about unexpected exceptions that occurred during bot execution.
 */
public interface UnexpectedExceptionNotifier {
    /**
     * Sends a message to the user about an unexpected exception.
     *
     * @param chatId    the chat ID of the user
     * @param exception the occurred exception
     */
    <T extends Exception> void sendUnexpectedExceptionMessage(Long chatId, T exception);
}