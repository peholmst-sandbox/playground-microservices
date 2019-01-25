package net.pkhapps.playground.microservices.portal.server;

import net.pkhapps.playground.microservices.directory.client.EnableServiceDirectoryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceDirectoryClient
public class FrontendPortalServer {

    public static void main(String[] args) {
        SpringApplication.run(FrontendPortalServer.class, args);
    }
}
