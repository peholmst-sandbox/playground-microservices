package net.pkhapps.playground.microservices.portal.server.ui.model;

import com.vaadin.flow.shared.Registration;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendDescriptor;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * TODO Document me
 */
public class OpenFrontend implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenFrontend.class);

    private final UUID uuid;
    private final FrontendInstanceDescriptor instance;
    private final FrontendDescriptor frontend;
    private final Origin origin;
    private final Set<MessageListener> messageListeners = new HashSet<>();
    private final Set<NotificationListener> notificationListeners = new HashSet<>();

    private List<Consumer<MessageListener>> messagesSentBeforeInitialized = new ArrayList<>();
    private boolean initialized = false;

    public OpenFrontend(FrontendInstanceDescriptor instance, FrontendDescriptor frontend) {
        this.uuid = UUID.randomUUID();
        this.instance = Objects.requireNonNull(instance, "instance must not be null");
        this.frontend = Objects.requireNonNull(frontend, "frontend must not be null");
        if (!instance.getResourceId().equals(frontend.getId())) {
            throw new IllegalArgumentException("Instance has different resource ID than frontend");
        }
        this.origin = new Origin(instance.getClientUri());
    }

    public UUID getUuid() {
        return uuid;
    }

    public FrontendInstanceDescriptor getInstance() {
        return instance;
    }

    public FrontendDescriptor getFrontend() {
        return frontend;
    }

    /**
     * Returns the origin of the frontend. This is used to make sure that messages sent from the portal to the frontend
     * arrive at the correct destination.
     */
    public Origin getOrigin() {
        return origin;
    }

    public void notifyUser() {
        // TODO Queue these as well?
        Set.copyOf(notificationListeners).forEach(NotificationListener::onNotification);
    }

    public void sendMessage(FrontendId sender, JsonValue message) {
        Objects.requireNonNull(sender, "sender must not be null");
        Objects.requireNonNull(message, "message must not be null");
        if (!initialized) {
            LOGGER.debug("OpenFrontend {} is not initialized, queuing message", uuid);
            messagesSentBeforeInitialized.add(listener -> listener.onMessage(sender, message));
        } else {
            Set.copyOf(messageListeners).forEach(listener -> listener.onMessage(sender, message));
        }
    }

    public Registration addMessageListener(MessageListener messageListener) {
        messageListeners.add(Objects.requireNonNull(messageListener, "messageListener must not be null"));
        return () -> messageListeners.remove(messageListener);
    }

    public Registration addNotificationListener(NotificationListener notificationListener) {
        notificationListeners.add(Objects.requireNonNull(notificationListener, "notificationListener must not be null"));
        return () -> notificationListeners.remove(notificationListener);
    }

    public void initialize() {
        LOGGER.debug("Initializing OpenFrontend {}", uuid);
        this.initialized = true;
        if (messagesSentBeforeInitialized.size() > 0) {
            LOGGER.debug("Sending queued messages");
            Set.copyOf(messageListeners).forEach(listener -> messagesSentBeforeInitialized.forEach(action -> action.accept(listener)));
            messagesSentBeforeInitialized.clear();
        }
    }

    @FunctionalInterface
    public interface MessageListener extends Serializable {
        void onMessage(FrontendId sender, JsonValue message);
    }

    @FunctionalInterface
    public interface NotificationListener extends Serializable {
        void onNotification();
    }

    @Override
    public String toString() {
        return String.format("%s(uuid: [%s], instance: [%s]", getClass().getSimpleName(), uuid, instance);
    }
}
