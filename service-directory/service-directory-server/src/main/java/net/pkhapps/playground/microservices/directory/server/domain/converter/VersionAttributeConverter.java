package net.pkhapps.playground.microservices.directory.server.domain.converter;

import net.pkhapps.playground.microservices.directory.api.Version;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class VersionAttributeConverter implements AttributeConverter<Version, String> {

    @Override
    public String convertToDatabaseColumn(Version attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public Version convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Version(dbData);
    }
}
