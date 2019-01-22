package net.pkhapps.playground.microservices.directory.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.Duration;

/**
 * TODO Document me!
 */
@ConfigurationProperties("service-directory.client")
@Validated
class ServiceDirectoryClientProperties {

    @NotNull
    private URI uri;

    @NotNull
    private Duration cacheSyncInterval = Duration.ofSeconds(30);

    @NotNull
    private Duration connectTimeout = Duration.ofMillis(500);

    @NotNull
    private Duration readTimeout = Duration.ofSeconds(10);

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Duration getCacheSyncInterval() {
        return cacheSyncInterval;
    }

    public void setCacheSyncInterval(Duration cacheSyncInterval) {
        this.cacheSyncInterval = cacheSyncInterval;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }
}
