package net.pkhapps.playground.microservices.microfrontendsample;

import net.pkhapps.playground.microservices.portal.support.config.EnablePortalSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePortalSupport
public class MicrofrontendSampleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrofrontendSampleServerApplication.class, args);
    }
}
