package dev.karmanov.library.service.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.karmanov.library.config.botProperties.WebHookBotProperties;
import dev.karmanov.library.dto.WebhookRegistrationRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class WebHookInitializer {
    private static final Logger log = LoggerFactory.getLogger(WebHookInitializer.class);
    private final WebHookBotProperties webHookBotProperties;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper;

    public WebHookInitializer(WebHookBotProperties webHookBotProperties, ObjectMapper mapper) {
        this.webHookBotProperties = webHookBotProperties;
        this.mapper = mapper;
    }

    public void registerWebhook() {
        if (!webHookBotProperties.isAutoRegister()) {
            log.info("Webhook auto-registration is disabled");
            return;
        }

        String webhook = webHookBotProperties.getWebhook();
        if (webhook == null || webhook.isBlank()) {
            log.warn("Webhook not set. Skipping webhook registration");
            return;
        }

        try {
            WebhookRegistrationRequestDTO dto = WebhookRegistrationRequestDTO.builder()
                    .url(webhook)
                    .ip(webHookBotProperties.getIp())
                    .maxConnections(webHookBotProperties.getMaxConnections())
                    .dropPendingUpdates(webHookBotProperties.isDropPendingUpdates())
                    .secretToken(webHookBotProperties.getSecretToken())
                    .allowedUpdates(webHookBotProperties.getAllowedUpdates())
                    .build();

            String jsonBody = mapper.writeValueAsString(dto);

            String uri = String.format("https://api.telegram.org/bot%s/setWebhook", webHookBotProperties.getBotToken());
            log.debug("Request uri: {}", uri);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                log.info("Webhook registered successfully: {}", response.body());
            } else {
                log.warn("Failed to register webhook. Status: {}, Response: {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.error("Exception while registering webhook", e);
        }
    }

}
