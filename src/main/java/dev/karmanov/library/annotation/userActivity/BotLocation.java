package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BotLocation {

    /**
     * Уникальное имя действия, с которым связан данный метод.
     * Используется для внутренней маршрутизации.
     */
    String actionName();

    /**
     * Радиус в метрах, в пределах которого геометка считается допустимой.
     * Применяется только при заданных centerLat и centerLon.
     * Значение <= 0 отключает проверку радиуса.
     */
    double withinRadiusMeters() default -1;

    /**
     * Широта центра области проверки (используется с withinRadiusMeters).
     */
    double centerLat() default Double.NaN;

    /**
     * Долгота центра области проверки (используется с withinRadiusMeters).
     */
    double centerLon() default Double.NaN;

    /**
     * Если true — геометка должна быть точной (is_accurate = true).
     * Игнорируются примерные координаты.
     */
    boolean requireAccurateLocation() default false;

    /**
     * Минимально допустимая точность геометки (horizontal_accuracy) в метрах.
     * Геометки с меньшей точностью будут проигнорированы.
     * Значение <= 0 отключает проверку.
     */
    double minAccuracyMeters() default -1;

    /**
     * Максимально допустимое "время жизни" геометки в секундах.
     * Геометки, отправленные слишком давно, игнорируются.
     * Значение <= 0 отключает проверку.
     */
    long maxAgeSeconds() default -1;

    /**
     * Если true — геометка должна быть явно отправлена пользователем.
     * Пересланные сообщения и ответы (reply) не будут обрабатываться.
     */
    boolean requireExplicitLocation() default true;


    /**
     * Приоритет вызова обработчика, если несколько подходят.
     * Меньшее значение означает более высокий приоритет.
     */
    int order() default Integer.MAX_VALUE;
}

