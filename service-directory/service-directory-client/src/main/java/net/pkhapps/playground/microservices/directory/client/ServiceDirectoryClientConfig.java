package net.pkhapps.playground.microservices.directory.client;

import net.pkhapps.playground.microservices.directory.api.ServiceDirectory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO Document me!
 */
@Configuration
@EnableConfigurationProperties
class ServiceDirectoryClientConfig {

    @Bean
    ServiceDirectoryClientProperties serviceDirectoryClientProperties() {
        return new ServiceDirectoryClientProperties();
    }

    @Bean
    ServiceDirectoryCache serviceDirectoryCache() {
        return new ServiceDirectoryCache();
    }

    @Bean
    ServiceDirectory serviceDirectory() {
        return new ServiceDirectoryImpl(serviceDirectoryCache(), serviceDirectoryClient());
    }

    @Bean
    ServiceDirectoryClient serviceDirectoryClient() {
        return new ServiceDirectoryClient(serviceDirectoryClientProperties().getUri(),
                serviceDirectoryClientProperties().getConnectTimeout(),
                serviceDirectoryClientProperties().getReadTimeout());
    }

    @Bean
    CacheSynchronizeWorker cacheSynchronizeWorker() {
        return new CacheSynchronizeWorker(serviceDirectoryClient(), serviceDirectoryCache(),
                serviceDirectoryClientProperties().getCacheSyncInterval());
    }
}
