package net.pkhapps.playground.microservices.directory.sample.client;

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
public class SampleClient {

    @Autowired
    ServiceDirectory serviceDirectory;

    @PostConstruct
    void registerSomeStuff() throws Exception {
        var keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        serviceDirectory.registerService(new ServiceRegistration(new ServiceDescriptor(new ServiceId("customerService"), "Customer Service", null, null), keyPair.getPublic()));
        serviceDirectory.registerInstance(new ServiceInstanceRegistration(new ServiceInstanceDescriptor(new ServiceId("customerService"), new Version("v1"),
                URI.create("http://localhost:8100/api/customers/v1"), URI.create("http://localhost:8100/api/ping")), keyPair.getPrivate()));
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleClient.class, args);
    }
}
