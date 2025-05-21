package dev.karmanov.library.annotation.userActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BotLocation {

    /**
     * Unique action name associated with this method.
     * Used for internal routing.
     */
    String actionName();

    /**
     * Radius in meters within which the location is considered valid.
     * Applied only if centerLat and centerLon are specified.
     * A value <= 0 disables radius check.
     */
    double withinRadiusMeters() default -1;

    /**
     * Latitude of the center point for radius check (used with withinRadiusMeters).
     */
    double centerLat() default Double.NaN;

    /**
     * Longitude of the center point for radius check (used with withinRadiusMeters).
     */
    double centerLon() default Double.NaN;

    /**
     * If true, the location must be accurate (is_accurate = true).
     * Approximate coordinates will be ignored.
     */
    boolean requireAccurateLocation() default false;

    /**
     * Minimum acceptable horizontal accuracy (in meters).
     * Locations with lower accuracy will be ignored.
     * A value <= 0 disables this check.
     */
    double minAccuracyMeters() default -1;

    /**
     * Maximum allowed age of the location in seconds.
     * Older locations will be ignored.
     * A value <= 0 disables this check.
     */
    long maxAgeSeconds() default -1;

    /**
     * If true, the location must be explicitly sent by the user.
     * Forwarded messages and replies will be ignored.
     */
    boolean requireExplicitLocation() default true;

    /**
     * Handler execution priority if multiple handlers match.
     * Lower values indicate higher priority.
     */
    int order() default Integer.MAX_VALUE;
}
