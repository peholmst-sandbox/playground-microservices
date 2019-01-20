package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ServiceInstanceRegistration}.
 */
public class ServiceInstanceRegistrationTest {

    private static KeyPair KEY_PAIR;
    private ServiceInstanceRegistration registration;

    @BeforeClass
    public static void setUpClass() throws Exception {
        KEY_PAIR = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    }

    @Before
    public void setUp() throws Exception {
        registration = new ServiceInstanceRegistration(new ServiceId("myservice"),
                new Version("v1"), "My Service", "My Service Description",
                URI.create("http://myservice.foo.bar/api/v1"),
                URI.create("http://myservice.foo.bar/ping/v1"),
                null,
                KEY_PAIR.getPrivate());
    }

    @Test
    public void verifySignature_roundTrip_signatureValid() throws Exception {
        assertThat(registration.verifySignature(KEY_PAIR.getPublic())).isTrue();
    }

    @Test
    public void verifySignature_alterRegistrationData_signatureInvalid() throws Exception {
        var serviceUriField = ServiceInstanceRegistration.class.getDeclaredField("serviceUri");
        serviceUriField.setAccessible(true);
        serviceUriField.set(registration, URI.create("http://myservice.evil.player/api/v1"));
        assertThat(registration.verifySignature(KEY_PAIR.getPublic())).isFalse();
    }

    @Test
    public void jsonSerializationRoundTrip() throws Exception {
        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(registration);
        System.out.println(json);
        var deserializedRegistration = objectMapper.readerFor(ServiceInstanceRegistration.class).readValue(json);
        assertThat(deserializedRegistration).isEqualTo(registration);
    }
}