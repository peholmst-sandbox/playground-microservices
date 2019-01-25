package net.pkhapps.playground.microservices.portal.server.ui;

import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;

import java.io.Serializable;

interface NavigationListener extends Serializable {

    default void onEnter(FrontendInstanceDescriptor frontend) {
        // NOP
    }

    default void onNotify(FrontendInstanceDescriptor frontend) {
        // NOP
    }

    default void onLeave(FrontendInstanceDescriptor frontend) {
        // NOP
    }

    default void onClose(FrontendInstanceDescriptor frontend) {
        // NOP
    }
}
