package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for more precise handling of image files with filters.
 * <p>
 * This annotation maps methods to handle image-related actions such as processing photo uploads.
 * It allows specifying the action name, image file size limits (in kilobytes), dimensions, aspect ratio, format, and order of execution.
 * </p>
 * <p><b>Example:</b></p>
 *
 * <pre>
 * {@code
 * @BotPhoto(actionName = "photo-action", minWidth = 100, aspectRatio = "1:1")
 * public void handlePhoto(Update update) { }
 * }
 * </pre>
 *
 * <p>
 * Attributes:
 * </p>
 *
 * <ul>
 *     <li><b>actionName</b>: The action ID (e.g., "photo-action").</li>
 *     <li><b>minFileSize</b>: The minimum file size (in kilobytes) allowed for the image. Defaults to 0 (no minimum).</li>
 *     <li><b>maxFileSize</b>: The maximum file size (in kilobytes) allowed for the image. Defaults to {@link Long#MAX_VALUE}.</li>
 *     <li><b>minWidth</b>: The minimum width (in pixels) of the image. Defaults to 0 (no minimum).</li>
 *     <li><b>minHeight</b>: The minimum height (in pixels) of the image. Defaults to 0 (no minimum).</li>
 *     <li><b>aspectRatio</b>: The aspect ratio of the image (e.g., "16:9"). Default is an empty string (no restriction).</li>
 *     <li><b>format</b>: The format of the image (e.g., "jpg", "png"). Default is an empty string (no restriction).</li>
 *     <li><b>order</b>: The order of execution for the handler method. Handlers with lower order values are executed first (default: {@link Integer#MAX_VALUE}).</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotPhoto {

    /**
     * The action ID for the photo processing (e.g., "photo-action").
     */
    String actionName();

    /**
     * The minimum file size (in kilobytes) allowed for the image. Defaults to 0 (no minimum).
     */
    long minFileSize() default 0;

    /**
     * The maximum file size (in kilobytes) allowed for the image. Defaults to {@link Long#MAX_VALUE}.
     */
    long maxFileSize() default Long.MAX_VALUE;

    /**
     * The minimum width (in pixels) allowed for the image. Defaults to 0 (no minimum).
     */
    int minWidth() default 0;

    /**
     * The minimum height (in pixels) allowed for the image. Defaults to 0 (no minimum).
     */
    int minHeight() default 0;

    /**
     * The aspect ratio of the image (e.g., "16:9"). Defaults to an empty string (no restriction).
     */
    String aspectRatio() default "";

    /**
     * The format of the image (e.g., "jpg", "png"). Defaults to an empty string (no restriction).
     */
    String format() default "";

    /**
     * The order of execution for the photo handler method.
     * Handlers with lower order values are executed first.
     */
    int order() default Integer.MAX_VALUE;
}


