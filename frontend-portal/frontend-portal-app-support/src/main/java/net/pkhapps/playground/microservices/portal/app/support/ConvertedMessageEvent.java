package net.pkhapps.playground.microservices.portal.app.support;

import net.pkhapps.playground.microservices.directory.api.FrontendId;

import java.util.Objects;

public class ConvertedMessageEvent<M> extends MessageEvent {

    private final M message;

    public ConvertedMessageEvent(FrontendId sender, M message) {
        super(sender);
        this.message = Objects.requireNonNull(message, "message must not be null");
    }

    public M getMessage() {
        return message;
    }

    boolean supports(MessageListener<?> listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        var messageType = message.getClass();
        return false;
    }

    public static void main(String[] args) {
        var event = new ConvertedMessageEvent<>(new FrontendId("sender"), "This is my message");
        var listener = new MessageListener<ConvertedMessageEvent<String>>() {

            @Override
            public void onMessageReceived(ConvertedMessageEvent<String> event) {

            }
        };

        event.supports(listener);
    }
}
