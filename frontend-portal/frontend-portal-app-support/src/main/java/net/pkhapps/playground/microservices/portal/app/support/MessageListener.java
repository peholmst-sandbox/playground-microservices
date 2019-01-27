package net.pkhapps.playground.microservices.portal.app.support;

import java.io.Serializable;

/**
 * TODO Document me!
 *
 * @param <E>
 */
@FunctionalInterface
public interface MessageListener<E extends MessageEvent> extends Serializable {

    /**
     * @param event
     */
    void onMessageReceived(E event);
}
