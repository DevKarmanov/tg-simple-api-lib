package dev.karmanov.library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebhookRegistrationRequestDTO {
        @JsonProperty("url")
        private final String url;

        @JsonProperty("ip_address")
        private final String ip;

        @JsonProperty("max_connections")
        private final int maxConnections;

        @JsonProperty("drop_pending_updates")
        private final boolean dropPendingUpdates;

        @JsonProperty("secret_token")
        private final String secretToken;

        @JsonProperty("allowed_updates")
        private final List<String> allowedUpdates;

        private WebhookRegistrationRequestDTO(Builder builder) {
                this.url = builder.url;
                this.ip = builder.ip;
                this.maxConnections = builder.maxConnections;
                this.dropPendingUpdates = builder.dropPendingUpdates;
                this.secretToken = builder.secretToken;
                this.allowedUpdates = builder.allowedUpdates;
        }

        public static Builder builder() {
                return new Builder();
        }

        public static class Builder {
                private String url;
                private String ip;
                private int maxConnections;
                private boolean dropPendingUpdates;
                private String secretToken;
                private List<String> allowedUpdates;

                public Builder url(String url) {
                        this.url = url;
                        return this;
                }

                public Builder ip(String ip) {
                        this.ip = ip;
                        return this;
                }

                public Builder maxConnections(int maxConnections) {
                        this.maxConnections = maxConnections;
                        return this;
                }

                public Builder dropPendingUpdates(boolean dropPendingUpdates) {
                        this.dropPendingUpdates = dropPendingUpdates;
                        return this;
                }

                public Builder secretToken(String secretToken) {
                        this.secretToken = secretToken;
                        return this;
                }

                public Builder allowedUpdates(List<String> allowedUpdates) {
                        this.allowedUpdates = allowedUpdates;
                        return this;
                }

                public WebhookRegistrationRequestDTO build() {
                        return new WebhookRegistrationRequestDTO(this);
                }
        }

        public String getUrl() {
                return url;
        }

        public String getIp() {
                return ip;
        }

        public int getMaxConnections() {
                return maxConnections;
        }

        public boolean isDropPendingUpdates() {
                return dropPendingUpdates;
        }

        public String getSecretToken() {
                return secretToken;
        }

        public List<String> getAllowedUpdates() {
                return allowedUpdates;
        }
}

