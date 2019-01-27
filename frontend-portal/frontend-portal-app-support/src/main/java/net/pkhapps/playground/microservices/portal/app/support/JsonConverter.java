package net.pkhapps.playground.microservices.portal.app.support;

import elemental.json.JsonValue;

import java.io.Serializable;

/**
 * Interface used by {@link PortalSupport} to convert between JSON values and POJOs.
 *
 * @param <M> the POJO type.
 */
public interface JsonConverter<M> extends Serializable {

    /**
     * Checks if this converter can convert to the given JSON value to a POJO.
     *
     * @param jsonValue the JSON value to convert.
     * @return true if the JSON value can be converted, false otherwise.
     */
    boolean supportsJson(JsonValue jsonValue);

    /**
     * Checks if this converter can convert the given POJO to a JSON value.
     *
     * @param pojo the POJO value to convert.
     * @return true if the POJO can be converted, false otherwise.
     */
    boolean supportsPojo(Object pojo);

    /**
     * Converts the given JSON value to a POJO.
     *
     * @param jsonValue the JSON value to convert.
     * @return the converted POJO.
     * @throws IllegalArgumentException if the JSON value cannot be converted to a POJO.
     */
    M convertToPojo(JsonValue jsonValue);

    /**
     * Converts the given POJO to a JSON value.
     *
     * @param pojo the POJO to convert.
     * @return the converted POJO.
     * @throws IllegalArgumentException if the POJO cannot be converted to a JSON value.
     */
    JsonValue convertToJson(M pojo);
}
