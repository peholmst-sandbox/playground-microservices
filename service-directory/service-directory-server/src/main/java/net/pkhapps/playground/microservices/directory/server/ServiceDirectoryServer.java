package net.pkhapps.playground.microservices.directory.server;

import net.pkhapps.playground.microservices.directory.server.config.ServiceDirectoryServerProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
@EnableJpaAuditing
public class ServiceDirectoryServer {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public DateTimeProvider dateTimeProvider(Clock clock) {
        return () -> Optional.of(clock.instant());
    }

    @Bean
    @Qualifier("ping")
    public ScheduledExecutorService pingThreadPool(ServiceDirectoryServerProperties properties) {
        // The thread pool is automatically shutdown when the application is terminated
        return Executors.newScheduledThreadPool(properties.getPingThreadPoolSize());
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceDirectoryServer.class, args);
    }
}
