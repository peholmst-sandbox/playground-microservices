package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ServiceInstance}.
 */
public class ServiceInstanceTest {

    private ServiceInstance instance;

    @Before
    public void setUp() {
        instance = new ServiceInstance(new ServiceId("myservice"),
                new Version("v1"), "My Service", "My Service Description",
                URI.create("http://myservice.foo.bar/api/v1"),
                URI.create("http://myservice.foo.bar/ping/v1"),
                null,
                Instant.now());
    }

    @Test
    public void jsonSerializationRoundTrip() throws Exception {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var json = objectMapper.writeValueAsString(instance);
        System.out.println(json);
        var deserializedInstance = objectMapper.readerFor(ServiceInstance.class).readValue(json);
        assertThat(deserializedInstance).isEqualTo(instance);
    }
}
