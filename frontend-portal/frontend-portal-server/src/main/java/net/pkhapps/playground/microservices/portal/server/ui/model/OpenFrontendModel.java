package net.pkhapps.playground.microservices.portal.server.ui.model;

import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.shared.Registration;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.ServiceDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * TODO Document me!
 */
public class OpenFrontendModel implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenFrontendModel.class);

    private final SerializableSupplier<ServiceDirectory> serviceDirectory;
    @Nullable
    private OpenFrontend focused;
    private final Set<OpenFrontend> openFrontends = new HashSet<>();
    private final Set<OpenListener> openListeners = new HashSet<>();
    private final Set<FocusListener> focusListeners = new HashSet<>();
    private final Set<LeaveListener> leaveListeners = new HashSet<>();
    private final Set<CloseListener> closeListeners = new HashSet<>();

    /**
     * @param serviceDirectory
     */
    public OpenFrontendModel(SerializableSupplier<ServiceDirectory> serviceDirectory) {
        this.serviceDirectory = Objects.requireNonNull(serviceDirectory, "serviceDirectory must not be null");
    }

    private ServiceDirectory getServiceDirectory() {
        return serviceDirectory.get();
    }

    /**
     * @return
     */
    public Optional<OpenFrontend> getFocused() {
        return Optional.ofNullable(focused);
    }

    /**
     * @param uuid
     * @return
     */
    public Optional<OpenFrontend> getByUuid(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        return openFrontends.stream().filter(of -> of.getUuid().equals(uuid)).findAny();
    }

    /**
     * @param frontendId
     * @return
     */
    public Optional<OpenFrontend> getByFrontendId(FrontendId frontendId) {
        Objects.requireNonNull(frontendId, "frontendId must not be null");
        return openFrontends.stream().filter(of -> of.getFrontend().getId().equals(frontendId)).findAny();
    }

    /**
     * @return
     */
    Stream<OpenFrontend> getOpen() {
        return openFrontends.stream();
    }

    /**
     * @param frontendId
     * @return
     */
    public OpenFrontend open(FrontendId frontendId) {
        LOGGER.debug("Opening frontend {}", frontendId);
        var existingFrontend = getByFrontendId(frontendId);
        if (existingFrontend.isPresent()) {
            LOGGER.debug("Frontend {} was already open", frontendId);
            return existingFrontend.get();
        } else {
            var frontendDescriptor = getServiceDirectory().getFrontend(frontendId).orElseThrow(() -> new IllegalArgumentException("No such frontend: " + frontendId));
            var instanceDescriptor = getServiceDirectory().getInstance(frontendId).orElseThrow(() -> new IllegalStateException("No instance found: " + frontendId));
            var openFrontend = new OpenFrontend(instanceDescriptor, frontendDescriptor);
            openFrontends.add(openFrontend);
            Set.copyOf(openListeners).forEach(listener -> listener.onOpened(openFrontend));
            LOGGER.debug("Opened frontend {}", openFrontend);
            return openFrontend;
        }
    }

    /**
     * @param frontendId
     * @return
     */
    public OpenFrontend openAndFocus(FrontendId frontendId) {
        var frontend = open(frontendId);
        focus(frontend);
        return frontend;
    }

    /**
     * @param openFrontend
     */
    public void focus(OpenFrontend openFrontend) {
        if (!openFrontends.contains(openFrontend)) {
            throw new IllegalArgumentException("The frontend is not open");
        }
        if (!openFrontend.equals(focused)) {
            leave();
            LOGGER.debug("Focusing frontend {}", openFrontend);
            focused = openFrontend;
            Set.copyOf(focusListeners).forEach(listener -> listener.onFocused(openFrontend));
            LOGGER.debug("Focused frontend {}", openFrontend);
        }
    }

    /**
     * @param openFrontend
     */
    public void close(OpenFrontend openFrontend) {
        if (openFrontends.remove(openFrontend)) {
            LOGGER.debug("Closing frontend {}", openFrontend);
            if (openFrontend.equals(focused)) {
                leave();
                openFrontends.stream().findAny().ifPresent(this::focus);
            }
            Set.copyOf(closeListeners).forEach(listener -> listener.onClosed(openFrontend));
            LOGGER.debug("Closed frontend {}", openFrontend);
        }
    }

    private void leave() {
        if (focused != null) {
            var old = focused;
            LOGGER.debug("Leaving frontend {}", old);
            focused = null;
            Set.copyOf(leaveListeners).forEach(listener -> listener.onLeave(old));
            LOGGER.debug("Left frontend {}", old);
        }
    }

    /**
     * @param listener
     * @return
     */
    public Registration addOpenListener(OpenListener listener) {
        openListeners.add(Objects.requireNonNull(listener, "listener must not be null"));
        return () -> openListeners.remove(listener);
    }

    /**
     * @param listener
     * @return
     */
    public Registration addFocusListener(FocusListener listener) {
        focusListeners.add(Objects.requireNonNull(listener, "listener must not be null"));
        return () -> focusListeners.remove(listener);
    }

    /**
     * @param listener
     * @return
     */
    public Registration addLeaveListener(LeaveListener listener) {
        leaveListeners.add(Objects.requireNonNull(listener, "listener must not be null"));
        return () -> leaveListeners.remove(listener);
    }

    /**
     * @param listener
     * @return
     */
    public Registration addCloseListener(CloseListener listener) {
        closeListeners.add(Objects.requireNonNull(listener, "listener must not be null"));
        return () -> closeListeners.remove(listener);
    }

    /**
     *
     */
    @FunctionalInterface
    public interface OpenListener extends Serializable {
        void onOpened(OpenFrontend frontend);
    }

    /**
     *
     */
    @FunctionalInterface
    public interface FocusListener extends Serializable {
        void onFocused(OpenFrontend frontend);
    }

    /**
     *
     */
    @FunctionalInterface
    public interface LeaveListener extends Serializable {
        void onLeave(OpenFrontend frontend);
    }

    /**
     *
     */
    @FunctionalInterface
    public interface CloseListener extends Serializable {
        void onClosed(OpenFrontend frontend);
    }
}
