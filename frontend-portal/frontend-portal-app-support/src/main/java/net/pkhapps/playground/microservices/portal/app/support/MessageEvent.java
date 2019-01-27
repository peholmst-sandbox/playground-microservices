package net.pkhapps.playground.microservices.portal.app.support;

import net.pkhapps.playground.microservices.directory.api.FrontendId;

import java.io.Serializable;

/**
 * Base class for events received by {@link MessageListener}s.
 */
public abstract class MessageEvent implements Serializable {

    private final FrontendId sender;

    protected MessageEvent(FrontendId sender) {
        this.sender = sender;
    }

    /**
     * Returns the ID of the frontend that sent the message.
     */
    public FrontendId getSender() {
        return sender;
    }
}
