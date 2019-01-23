package net.pkhapps.playground.microservices.directory.server.domain.converter;

import net.pkhapps.playground.microservices.directory.api.FrontendId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FrontendIdAttributeConverter implements AttributeConverter<FrontendId, String> {

    @Override
    public String convertToDatabaseColumn(FrontendId attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public FrontendId convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new FrontendId(dbData);
    }
}
