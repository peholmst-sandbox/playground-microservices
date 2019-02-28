package net.pkhapps.playground.microservices.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@Slf4j
public class TestDataGenerator {

    private final UserDetailsManager userDetailsManager;
    private final ClientRegistrationService clientRegistrationService;
    private final PasswordEncoder passwordEncoder;

    public TestDataGenerator(UserDetailsManager userDetailsManager, ClientRegistrationService clientRegistrationService, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.clientRegistrationService = clientRegistrationService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        createTestUsers();
        createTestClients();
    }

    private void createTestUsers() {
        if (!userDetailsManager.userExists("joecool")) {
            userDetailsManager.createUser(new User("joecool",
                    passwordEncoder.encode("password"),
                    Set.of(new SimpleGrantedAuthority("ROLE_USER"))));
            log.warn("Created test user [joecool]");
        } else {
            log.warn("Test user [joecool] already exists");
        }
    }

    private void createTestClients() {
        var portalServer = new BaseClientDetails();
        portalServer.setClientId("PortalServer");
        portalServer.setClientSecret("this-is-the-portal-server-secret");
        portalServer.setRegisteredRedirectUri(Set.of("http://localhost:8880/login"));
        createTestClient(portalServer);

        var microfrontendSampleServer = new BaseClientDetails();
        microfrontendSampleServer.setClientId("MicrofrontendSampleServer");
        microfrontendSampleServer.setClientSecret("this-is-the-microfrontend-sample-server-secret");
        microfrontendSampleServer.setRegisteredRedirectUri(Set.of("http://localhost:8881/microfrontend-sample/login"));
        createTestClient(microfrontendSampleServer);
    }

    private void createTestClient(BaseClientDetails clientDetails) {
        try {
            clientDetails.setScope(Set.of("profile", "email"));
            clientDetails.setAutoApproveScopes(Set.of("profile", "email"));
            clientDetails.setAuthorizedGrantTypes(Set.of("authorization_code"));
            clientRegistrationService.addClientDetails(clientDetails);
            log.warn("Created test client [{}]", clientDetails.getClientId());
        } catch (ClientAlreadyExistsException ex) {
            log.warn("Test client [{}] already exists", clientDetails.getClientId());
        }
    }
}
