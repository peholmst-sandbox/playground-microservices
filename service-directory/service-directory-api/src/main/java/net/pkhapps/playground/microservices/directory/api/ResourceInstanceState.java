package net.pkhapps.playground.microservices.directory.api;

/**
 * Enumeration of the states a resource instance can be in.
 */
public enum ResourceInstanceState {
    /**
     * The instance is up and is responding to pings.
     */
    UP,
    /**
     * The instance is down and is not responding to pings.
     */
    DOWN,
    /**
     * The state of the instance is currently not clear. This could happen if the service directory allows a certain
     * number of pings to be missed before a resource instance is declared being down.
     */
    INDETERMINATE
}
