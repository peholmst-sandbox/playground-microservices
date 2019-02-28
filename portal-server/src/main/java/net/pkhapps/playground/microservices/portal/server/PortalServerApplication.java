package net.pkhapps.playground.microservices.portal.server;

import net.pkhapps.playground.microservices.portal.support.config.EnablePortalSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePortalSupport
public class PortalServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalServerApplication.class, args);
    }
}
