package dev.karmanov.library.service.handlers;

import dev.karmanov.library.service.state.StateManager;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface MessageHandler {
    void handle(List<String> userAwaitingAction, Update update, StateManager manager);
}
