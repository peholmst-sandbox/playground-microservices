package net.pkhapps.playground.microservices.directory.server.domain.converter;

import net.pkhapps.playground.microservices.directory.api.ServiceId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ServiceIdAttributeConverter implements AttributeConverter<ServiceId, String> {

    @Override
    public String convertToDatabaseColumn(ServiceId attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public ServiceId convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new ServiceId(dbData);
    }
}
