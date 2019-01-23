package net.pkhapps.playground.microservices.directory.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@ConfigurationProperties("service-directory.server")
@Validated
@Component
public class ServiceDirectoryServerProperties {

    @NotNull
    private Duration pingInterval = Duration.ofSeconds(15);

    @NotNull
    private Duration pingTimeout = Duration.ofMillis(500);

    @Min(1)
    private int pingThreadPoolSize = 4;

    public Duration getPingInterval() {
        return pingInterval;
    }

    public void setPingInterval(Duration pingInterval) {
        this.pingInterval = pingInterval;
    }

    public Duration getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(Duration pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public int getPingThreadPoolSize() {
        return pingThreadPoolSize;
    }

    public void setPingThreadPoolSize(int pingThreadPoolSize) {
        this.pingThreadPoolSize = pingThreadPoolSize;
    }
}
