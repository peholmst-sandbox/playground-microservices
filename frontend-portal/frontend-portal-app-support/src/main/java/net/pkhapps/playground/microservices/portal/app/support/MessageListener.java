package net.pkhapps.playground.microservices.portal.app.support;

import java.io.Serializable;

/**
 * TODO Document me!
 */
@FunctionalInterface
public interface MessageListener extends Serializable {

    /**
     * @param event
     */
    void onMessageReceived(MessageEvent event);
}
