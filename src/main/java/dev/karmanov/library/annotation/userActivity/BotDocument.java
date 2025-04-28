package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to handle document processing in a bot.
 * <p>
 * This annotation maps methods to handle document-related actions such as processing uploaded files.
 * It allows specifying the action name, supported file extensions, file size limits in kilobytes, file name pattern,
 * and the order of execution.
 * </p>
 * <p><b>Example:</b></p>
 *
 * <pre>
 * {@code
 * @BotDocument(actionName = "doc-wait", fileNameRegex = ".*", fileExtensions = "pdf")
 * public void handleDocument(Update update) { }
 * }
 * </pre>
 *
 * <p>
 * Attributes:
 * </p>
 *
 * <ul>
 *     <li><b>actionName</b>: The action ID (e.g., "doc-wait").</li>
 *     <li><b>fileExtensions</b>: An array of expected file extensions (e.g., "pdf", "docx"). Default is {"doc", "docx", "pdf"}.</li>
 *     <li><b>minFileSize</b>: The minimum file size in kilobytes (default: 0). Can be used to restrict file uploads to a certain size.</li>
 *     <li><b>maxFileSize</b>: The maximum file size in kilobytes (default: {@link Long#MAX_VALUE}).</li>
 *     <li><b>fileNameRegex</b>: A regular expression to match the file name against. Used to filter files by name.</li>
 *     <li><b>order</b>: The order of execution for the handler method. Lower values are executed first (default: {@link Integer#MAX_VALUE}).</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotDocument {

    /**
     * The action ID for the document processing (e.g., "doc-wait").
     */
    String actionName();

    /**
     * An array of expected file extensions for the document (e.g., "pdf", "docx").
     * Defaults to {"doc", "docx", "pdf"}.
     */
    String[] fileExtensions() default {"doc", "docx", "pdf"};

    /**
     * The minimum file size (in kilobytes) allowed for the document. Defaults to 0 (no minimum).
     */
    long minFileSize() default 0;

    /**
     * The maximum file size (in kilobytes) allowed for the document. Defaults to {@link Long#MAX_VALUE}.
     */
    long maxFileSize() default Long.MAX_VALUE;

    /**
     * The regular expression to match against the document's file name.
     */
    String fileNameRegex();

    /**
     * The order of execution for the document handler method.
     * Handlers with lower order values are executed first.
     */
    int order() default Integer.MAX_VALUE;
}

