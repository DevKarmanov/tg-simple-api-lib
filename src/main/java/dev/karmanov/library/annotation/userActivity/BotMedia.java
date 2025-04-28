package dev.karmanov.library.annotation.userActivity;

import dev.karmanov.library.model.message.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to handle media file processing in a bot.
 * <p>
 * This annotation maps methods to handle media file-related actions such as processing uploaded media files.
 * It allows specifying the action name, the type of media (e.g., photo, video), and the order of execution.
 * </p>
 * <p><b>Example:</b></p>
 *
 * <pre>
 * {@code
 * @BotMedia(actionName = "media-action", mediaType = MediaType.PHOTO)
 * public void handleMedia(Update update) { }
 * }
 * </pre>
 *
 * <p>
 * Attributes:
 * </p>
 *
 * <ul>
 *     <li><b>actionName</b>: The action ID (e.g., "media-action").</li>
 *     <li><b>mediaType</b>: The type of the media file (e.g., {@link MediaType#PHOTO}).</li>
 *     <li><b>order</b>: The order of execution for the handler method. Handlers with lower order values are executed first (default: {@link Integer#MAX_VALUE}).</li>
 * </ul>
 * @see MediaType
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BotMedia {

     /**
      * The action ID for the media processing (e.g., "media-action").
      */
     String actionName();

     /**
      * The type of media file being handled (e.g., {@link MediaType#PHOTO}, {@link MediaType#VIDEO}).
      */
     MediaType mediaType();

     /**
      * The order of execution for the media handler method.
      * Handlers with lower order values are executed first.
      */
     int order() default Integer.MAX_VALUE;
}



