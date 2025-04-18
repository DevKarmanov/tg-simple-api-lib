package dev.karmanov.library.service.register.utils.media;

import dev.karmanov.library.model.message.MediaType;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MediaQualifier {
    MediaType hasMedia(Update update);
}
