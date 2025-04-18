package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotScheduled {

    int order() default Integer.MAX_VALUE;
    String cron() default "";                    // CRON-выражение
    long fixedDelay() default -1;                // Задержка между выполнениями (в мс)
    long fixedRate() default -1;                 // Интервал между стартами (в мс)
    String zone() default "UTC";                 // Часовой пояс
    boolean runOnStartup() default false;        // Выполнить один раз при запуске
    String[] roles() default {"user"};
}
