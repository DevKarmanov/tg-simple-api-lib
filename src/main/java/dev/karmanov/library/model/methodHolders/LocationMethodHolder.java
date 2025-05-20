package dev.karmanov.library.model.methodHolders;

import dev.karmanov.library.model.methodHolders.abstractHolders.OrderedActionMethodHolder;

import java.lang.reflect.Method;

public class LocationMethodHolder extends OrderedActionMethodHolder {
    private double withinRadiusMeters;
    private double centerLat;
    private double centerLon;
    private  boolean requireAccurateLocation;
    private double minAccuracyMeters;
    private long maxAgeSeconds;
    private boolean requireExplicitLocation;

    public LocationMethodHolder(Method method, String actionName, int order, double withinRadiusMeters, double centerLat, double centerLon, boolean requireAccurateLocation, double minAccuracyMeters, long maxAgeSeconds, boolean requireExplicitLocation) {
        super(method, actionName, order);
        this.withinRadiusMeters = withinRadiusMeters;
        this.centerLat = centerLat;
        this.centerLon = centerLon;
        this.requireAccurateLocation = requireAccurateLocation;
        this.minAccuracyMeters = minAccuracyMeters;
        this.maxAgeSeconds = maxAgeSeconds;
        this.requireExplicitLocation = requireExplicitLocation;
    }

    public double getWithinRadiusMeters() {
        return withinRadiusMeters;
    }

    public void setWithinRadiusMeters(double withinRadiusMeters) {
        this.withinRadiusMeters = withinRadiusMeters;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(double centerLat) {
        this.centerLat = centerLat;
    }

    public double getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(double centerLon) {
        this.centerLon = centerLon;
    }

    public boolean isRequireAccurateLocation() {
        return requireAccurateLocation;
    }

    public void setRequireAccurateLocation(boolean requireAccurateLocation) {
        this.requireAccurateLocation = requireAccurateLocation;
    }

    public double getMinAccuracyMeters() {
        return minAccuracyMeters;
    }

    public void setMinAccuracyMeters(double minAccuracyMeters) {
        this.minAccuracyMeters = minAccuracyMeters;
    }

    public long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public void setMaxAgeSeconds(long maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
    }

    public boolean isRequireExplicitLocation() {
        return requireExplicitLocation;
    }

    public void setRequireExplicitLocation(boolean requireExplicitLocation) {
        this.requireExplicitLocation = requireExplicitLocation;
    }
}
