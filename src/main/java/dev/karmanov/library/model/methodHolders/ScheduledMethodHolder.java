package dev.karmanov.library.model.methodHolders;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ScheduledMethodHolder extends MethodHolder {
    private int order;
    private String cron;
    private long fixedDelay;
    private long fixedRate;
    private String zone;
    private boolean runOnStartup;
    private String[] roles;

    public ScheduledMethodHolder(Method method, int order, String cron, long fixedDelay, long fixedRate, String zone, boolean runOnStartup, String[] roles) {
        super(method);
        this.order = order;
        this.cron = cron;
        this.fixedDelay = fixedDelay;
        this.fixedRate = fixedRate;
        this.zone = zone;
        this.runOnStartup = runOnStartup;
        this.roles = roles;
    }

    public void setRoles(String[] roles){
        this.roles = roles;
    }

    public Set<String> getRoles(){
        return new HashSet<>(Arrays.asList(roles));
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public long getFixedDelay() {
        return fixedDelay;
    }

    public void setFixedDelay(long fixedDelay) {
        this.fixedDelay = fixedDelay;
    }

    public long getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(long fixedRate) {
        this.fixedRate = fixedRate;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isRunOnStartup() {
        return runOnStartup;
    }

    public void setRunOnStartup(boolean runOnStartup) {
        this.runOnStartup = runOnStartup;
    }
}
