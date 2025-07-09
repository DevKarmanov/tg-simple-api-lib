package dev.karmanov.library.config.botProperties;

import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.List;

@ConfigurationProperties(prefix = "telegram.bot")
public class WebHookBotProperties {
    private String webhook;
    private boolean autoRegister = false;
    private String ip;
    private int maxConnections = 40;
    private boolean dropPendingUpdates = false;
    private String botToken;
    private String secretToken;
    private List<String> allowedUpdates = null;

    @PostConstruct
    public void validate() {
        if (autoRegister && StringUtils.hasText(webhook) && !StringUtils.hasText(botToken)) {
            String msg = "telegram.bot.bot-token must be set when auto-register is enabled";
            LoggerFactory.getLogger(getClass()).error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public boolean isDropPendingUpdates() {
        return dropPendingUpdates;
    }

    public void setDropPendingUpdates(boolean dropPendingUpdates) {
        this.dropPendingUpdates = dropPendingUpdates;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public List<String> getAllowedUpdates() {
        return allowedUpdates;
    }

    public void setAllowedUpdates(List<String> allowedUpdates) {
        this.allowedUpdates = allowedUpdates;
    }
}