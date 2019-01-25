package net.pkhapps.playground.microservices.portal.server.ui;

import com.vaadin.flow.shared.Registration;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

class NavigationModel implements Serializable {

    private final Set<NavigationListener> listeners = new HashSet<>();
    private FrontendInstanceDescriptor selected;
    private Set<FrontendInstanceDescriptor> openFrontends = new HashSet<>();

    Optional<FrontendInstanceDescriptor> getSelected() {
        return Optional.ofNullable(selected);
    }

    Stream<FrontendInstanceDescriptor> getOpen() {
        return openFrontends.stream();
    }

    void open(FrontendInstanceDescriptor descriptor) {
        if (selected == descriptor) {
            return;
        }
        leave();
        openFrontends.add(descriptor);
        selected = descriptor;
        listeners.forEach(listener -> listener.onEnter(descriptor));
    }

    private void leave() {
        if (selected != null) {
            var oldSelected = selected;
            selected = null;
            listeners.forEach(listener -> listener.onLeave(oldSelected));
        }
    }

    void close(FrontendInstanceDescriptor descriptor) {
        openFrontends.remove(descriptor);
        if (Objects.equals(selected, descriptor)) {
            leave();
            openFrontends.stream().findAny().ifPresent(this::open);
        }
        listeners.forEach(listener -> listener.onClose(descriptor));
    }

    void notify(FrontendInstanceDescriptor descriptor) {
        listeners.forEach(listener -> listener.onNotify(descriptor));
    }

    Registration addListener(NavigationListener listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }
}
