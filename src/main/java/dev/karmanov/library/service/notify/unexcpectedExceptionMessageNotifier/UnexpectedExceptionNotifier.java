package dev.karmanov.library.service.notify.unexcpectedExceptionMessageNotifier;

public interface UnexpectedExceptionNotifier{
    <T extends Exception> void sendUnexpectedExceptionMessage(Long chatId, T exception);
}
