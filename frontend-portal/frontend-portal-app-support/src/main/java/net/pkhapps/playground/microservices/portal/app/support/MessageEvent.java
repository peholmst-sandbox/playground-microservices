package net.pkhapps.playground.microservices.portal.app.support;

import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class for events received by {@link MessageListener}s.
 */
public class MessageEvent implements Serializable {

    private final FrontendId sender;
    private final JsonValue message;

    public MessageEvent(FrontendId sender, JsonValue message) {
        this.sender = Objects.requireNonNull(sender, "sender must not be null");
        this.message = Objects.requireNonNull(message, "message must not be null");
    }

    /**
     * Returns the ID of the frontend that sent the message.
     */
    public FrontendId getSender() {
        return sender;
    }

    /**
     * Returns the message.
     */
    public JsonValue getMessage() {
        return message;
    }
}
