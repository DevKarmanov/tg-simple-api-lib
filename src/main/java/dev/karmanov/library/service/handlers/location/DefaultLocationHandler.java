package dev.karmanov.library.service.handlers.location;

import dev.karmanov.library.model.methodHolders.LocationMethodHolder;
import dev.karmanov.library.service.register.BotCommandRegister;
import dev.karmanov.library.service.register.executor.Executor;
import dev.karmanov.library.service.register.utils.user.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.Set;
/**
 * Default implementation of {@link LocationHandler} for processing user location messages.
 * <p>
 * Applies various filters such as accuracy, freshness, radius, and explicitness,
 * and executes matching location-handling methods.
 */
public class DefaultLocationHandler implements LocationHandler{
    private static final int EARTH_RADIUS_METERS = 6_371_000;
    private static final Logger logger = LoggerFactory.getLogger(DefaultLocationHandler.class);
    private Executor methodExecutor;
    private BotCommandRegister register;
    private RoleChecker roleChecker;

    @Autowired(required = false)
    public void setRoleChecker(RoleChecker roleChecker) {
        this.roleChecker = roleChecker;
    }

    @Autowired(required = false)
    public void setMethodExecutor(Executor methodExecutor) {
        this.methodExecutor = methodExecutor;
    }

    @Autowired(required = false)
    public void setRegister(BotCommandRegister register) {
        this.register = register;
    }

    @Override
    public void handle(Set<String> userAwaitingAction, Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();

        Location location = message.getLocation();
        logger.info("Handling location: {}", location);

        register.getLocationMethods().stream()
                .filter(o->userAwaitingAction.contains(o.getActionName()))
                .filter(o->{
                    if (o.isRequireExplicitLocation()){
                        boolean notForwardedFrom = message.getForwardFrom() == null;
                        boolean notForwarded = message.getForwardDate() == null;
                        boolean notReply = message.getReplyToMessage() == null;

                        return notForwarded && notReply && notForwardedFrom;
                    }return true;
                })
                .filter(o->{
                    if (o.isRequireAccurateLocation()){
                        return isAccurateLocation(location.getHorizontalAccuracy(),o.getMinAccuracyMeters());
                    }else return true;
                })
                .filter(o->{
                    if (o.getMaxAgeSeconds()>0){
                        long currentTimeSeconds = System.currentTimeMillis() / 1000L;
                        long ageSeconds = currentTimeSeconds - message.getDate();
                        return ageSeconds <= o.getMaxAgeSeconds();
                    }else return true;
                })
                .filter(o->isWithinRadius(location.getLatitude(),location.getLongitude(),o.getCenterLat(),o.getCenterLon(),o.getWithinRadiusMeters()))
                .filter(o->roleChecker.userHasAccess(userId,chatId,register.getSpecialAccessMethodHolders(o.getMethod())))
                .sorted(Comparator.comparingInt(LocationMethodHolder::getOrder))
                .forEach(o -> {
                    logger.info("Executing method: {} for location: {}", o.getMethod().getName(),location);
                    methodExecutor.executeMethod(o.getMethod(),chatId,update);
                });

    }

    private double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(rLat1) * Math.cos(rLat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }

    public boolean isWithinRadius(double lat, double lon, double centerLat, double centerLon, double radiusMeters) {
        if (Double.isNaN(centerLat) || Double.isNaN(centerLon) || radiusMeters <= 0) {
            return true;
        }
        double dist = distanceMeters(lat, lon, centerLat, centerLon);
        return dist <= radiusMeters;
    }

    public boolean isAccurateLocation(Double horizontalAccuracy, double minAccuracyMeters) {
        if (minAccuracyMeters <= 0) {
            return true;
        }

        if (horizontalAccuracy == null) {
            return false;
        }

        return horizontalAccuracy <= minAccuracyMeters;
    }
}
