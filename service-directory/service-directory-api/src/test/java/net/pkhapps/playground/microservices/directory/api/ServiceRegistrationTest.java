package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ServiceRegistration}.
 */
public class ServiceRegistrationTest {

    private static KeyPair KEY_PAIR;
    private ServiceRegistration registration;

    @BeforeClass
    public static void setUpClass() throws Exception {
        KEY_PAIR = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    }

    @Before
    public void setUp() {
        registration = new ServiceRegistration(new ServiceDescriptor(new ServiceId("myservice"), "My Service"),
                KEY_PAIR.getPublic());
    }

    @Test
    public void initialState() throws Exception {
        assertThat(registration.getDescriptor().getId()).isEqualTo(new ServiceId("myservice"));
        assertThat(registration.getPublicKey()).isEqualTo(KEY_PAIR.getPublic());
    }

    @Test
    public void jsonSerializationRoundTrip() throws Exception {
        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(registration);
        System.out.println(json);
        var deserializedRegistration = objectMapper.readerFor(ServiceRegistration.class).readValue(json);
        assertThat(deserializedRegistration).isEqualTo(registration);
    }
}
