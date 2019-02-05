package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ServiceInstanceDescriptor}.
 */
public class ServiceDescriptorInstanceTest {

    private ServiceInstanceDescriptor instance;

    @Before
    public void setUp() {
        instance = new ServiceInstanceDescriptor(new ServiceId("myservice"),
                URI.create("http://myservice.foo.bar/api/v1"),
                URI.create("http://myservice.foo.bar/ping/v1"));
    }

    @Test
    public void jsonSerializationRoundTrip() throws Exception {
        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(instance);
        System.out.println(json);
        var deserializedInstance = objectMapper.readerFor(ServiceInstanceDescriptor.class).readValue(json);
        assertThat(deserializedInstance).isEqualTo(instance);
    }
}
