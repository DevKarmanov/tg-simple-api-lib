package dev.karmanov.library.service.register.utils.media.voice.executor;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.function.Function;

public interface InterpreterExecutor {
    <T> void handleInterpretation(
            T media,
            Update update,
            Long chatId,
            Method method,
            Function<T, Boolean> isInterpreter,
            Function<T, String> getRegex,
            Function<T, String> transcriber
    );
}
