package net.pkhapps.playground.microservices.portal.app.support;

import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;

import java.util.Objects;

/**
 * Message event that contains a message as JSON.
 */
public class JsonMessageEvent extends MessageEvent {

    private final JsonValue message;

    /**
     * Creates a new {@code JsonMessageEvent}.
     *
     * @param sender  the sender of the message.
     * @param message the message as JSON.
     */
    public JsonMessageEvent(FrontendId sender, JsonValue message) {
        super(sender);
        this.message = Objects.requireNonNull(message, "message must not be null");
    }

    /**
     * Returns the message.
     */
    public JsonValue getMessage() {
        return message;
    }
}
