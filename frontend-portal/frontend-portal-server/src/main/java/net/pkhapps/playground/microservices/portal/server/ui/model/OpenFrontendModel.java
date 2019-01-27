package net.pkhapps.playground.microservices.portal.server.ui.model;

import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.shared.Registration;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ServiceDirectory;
import net.pkhapps.playground.microservices.portal.server.ui.NavigationListener;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * TODO Document me!
 */
public class OpenFrontendModel implements Serializable {

    private final SerializableSupplier<ServiceDirectory> serviceDirectory;
    private OpenFrontend selected;
    private Set<OpenFrontend> openFrontends = new HashSet<>();

    public OpenFrontendModel(SerializableSupplier<ServiceDirectory> serviceDirectory) {
        this.serviceDirectory = Objects.requireNonNull(serviceDirectory, "serviceDirectory must not be null");
    }

    private ServiceDirectory getServiceDirectory() {
        return serviceDirectory.get();
    }

    public Optional<OpenFrontend> getSelected() {
        return Optional.ofNullable(selected);
    }

    public Optional<OpenFrontend> getByUuid(UUID uuid) {
        return Optional.empty();
    }

    Stream<FrontendInstanceDescriptor> getOpen() {
        return openFrontends.stream();
    }

    public OpenFrontend open(FrontendId frontendId) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public OpenFrontend openAndFocus(FrontendId frontendId) {
        throw new UnsupportedOperationException("not implemented yet");
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
