package net.pkhapps.playground.microservices.portal.sample.app2;

import net.pkhapps.playground.microservices.directory.api.*;
import net.pkhapps.playground.microservices.directory.client.EnableServiceDirectoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.security.KeyPairGenerator;

@SpringBootApplication
@EnableServiceDirectoryClient
public class SampleApp2 {

    @Autowired
    ServiceDirectory serviceDirectory;

    @PostConstruct
    public void setUp() throws Exception {
        // TODO Replace with better API
        var keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        var frontendId = new FrontendId("sample-app-2");
        serviceDirectory.registerFrontend(new FrontendRegistration(new FrontendDescriptor(frontendId, "Sample App 2", null, null), keyPair.getPublic()));
        serviceDirectory.registerInstance(new FrontendInstanceRegistration(new FrontendInstanceDescriptor(frontendId, new Version("1.0"), URI.create("http://localhost2:8201"), URI.create("http://localhost2:8201/ping")), keyPair.getPrivate()));
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleApp2.class, args);
    }
}
