package me.tori.wraith.targetedevent;

import me.tori.wraith.bus.EventBus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 */
public class TargetedEventTest {

    @Test
    public void testTargetedEvent() {
        final EventBus bus = new EventBus();

        TestSubsciber subsciber = new TestSubsciber();
        bus.subscribe(subsciber);

        TestEvent event = new TestEvent("message", ValidListener.class);
        assertFalse(bus.dispatchTargeted(event));
    }
}