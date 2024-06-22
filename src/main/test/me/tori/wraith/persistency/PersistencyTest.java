package me.tori.wraith.persistency;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.event.cancelable.CancelableEvent;
import me.tori.wraith.listener.EventListener;
import me.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.1.0
 */
public class PersistencyTest {

    @Test
    public void testEventPersistency() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListener(new MyListener(3));
        }});

        Assertions.assertTrue(bus.dispatch(new MyEvent()));
        Assertions.assertTrue(bus.dispatch(new MyEvent()));
        Assertions.assertTrue(bus.dispatch(new MyEvent()));

        Assertions.assertFalse(bus.dispatch(new MyEvent()));
    }

    @Test
    public void testIndefiniteEvent() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListener(new MyListener(0)); // <= 0 means persist indefinitely
        }});

        for (int i = 0; i < 1_000_000; i++) {
            Assertions.assertTrue(bus.dispatch(new MyEvent()));
        }
    }

    public static class MyListener extends EventListener<MyEvent> {

        public MyListener(int persists) {
            super(MyEvent.class, null, 0, persists);
        }

        @Override
        public void invoke(MyEvent event) {
            event.cancel();
        }
    }

    public static class MyEvent extends CancelableEvent {

    }
}