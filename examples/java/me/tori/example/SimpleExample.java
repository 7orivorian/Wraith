package me.tori.example;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.event.cancelable.Cancelable;
import me.tori.wraith.listener.LambdaEventListener;
import me.tori.wraith.subscriber.Subscriber;

/**
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

        if (!bus.post(event)) {
            // Only log the message if our event isn't canceled
            System.out.println(event.message);
        }

        event = new SimpleEvent("Pie is delicious <3");
        if (!bus.post(event)) {
            System.out.println(event.message);
        }
    }

    private static final class SimpleSubscriber extends Subscriber {

        public SimpleSubscriber() {
            registerListener(
                    new LambdaEventListener<>(SimpleEvent.class, event -> {
                        if (event.message.contains("gross")) {
                            // Pie is not gross, so we cancel this event, preventing it from being written to the console
                            event.cancel();
                        }
                    })
            );
        }
    }

    private static final class SimpleEvent extends Cancelable {

        public String message;

        public SimpleEvent(String message) {
            this.message = message;
        }
    }
}