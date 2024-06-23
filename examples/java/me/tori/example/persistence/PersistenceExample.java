package me.tori.example.persistence;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.bus.IEventBus;
import me.tori.wraith.listener.LambdaEventListener;
import me.tori.wraith.subscriber.Subscriber;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.2.0
 */
class PersistenceExample {

    private static final IEventBus EVENT_BUS = new EventBus();
    private static final Subscriber SUBSCRIBER = new Subscriber() {{
        // Register a listener that prints a single
        // String event & is then removed from the event bus
        registerListener(new LambdaEventListener<>(String.class, null, IEventBus.DEFAULT_PRIORITY, 1, System.out::println));
    }};

    public static void main(String[] args) {
        // Subscribe our subscriber. This will never be removed from the event bus.
        EVENT_BUS.subscribe(SUBSCRIBER);

        // After this event is dispatched, and it's listener invoked,
        // the listener will be removed since it's set to only persist once.
        EVENT_BUS.dispatch("Hello world!");

        // You can still dispatch events to remaining listeners.
        EVENT_BUS.dispatch("I wont be printed :(");
    }
}