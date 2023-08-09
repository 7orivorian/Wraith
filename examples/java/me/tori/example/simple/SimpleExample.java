package me.tori.example.simple;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.event.cancelable.Cancelable;
import me.tori.wraith.listener.LambdaEventListener;
import me.tori.wraith.subscriber.Subscriber;

/**
 * One-class example
 * <p>Last updated for version <b>3.0.0</b>
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 */
class SimpleExample {

    public static void main(String[] args) {
        // Create an event bus
        EventBus bus = new EventBus();

        // Create our subscriber
        SimpleSubscriber subscriber = new SimpleSubscriber();

        // Subscribe to the event bus
        bus.subscribe(subscriber);

        SimpleEvent event;

        // Create a simple event
        // This event will be canceled, see the lambda in SimpleSubscriber to know why ;P
        event = new SimpleEvent("Pie is gross!");

        if (!bus.dispatch(event)) {
            // Only log the message if our event isn't canceled
            System.out.println(event.getMessage());
        }

        event = new SimpleEvent("Pie is delicious <3");
        if (!bus.dispatch(event)) {
            System.out.println(event.getMessage());
        }
    }

    private static final class SimpleSubscriber extends Subscriber {

        public SimpleSubscriber() {
            registerListener(
                    new LambdaEventListener<>(SimpleEvent.class, event -> {
                        if (event.getMessage().contains("gross")) {
                            // Pie is not gross, so we cancel this event, preventing it from being written to the console
                            event.cancel();
                        }
                    })
            );
        }
    }

    private static final class SimpleEvent extends Cancelable {

        private final String message;

        public SimpleEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}