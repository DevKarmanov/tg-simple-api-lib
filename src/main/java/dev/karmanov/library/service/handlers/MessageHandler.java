package dev.karmanov.library.service.handlers;

import dev.karmanov.library.service.state.StateManager;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public interface MessageHandler {
    void handle(Set<String> userAwaitingAction, Update update);
}
