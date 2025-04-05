/*
 * Copyright (c) 2025 7orivorian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package dev.tori.wraith.bus;

import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.status.IStatusEvent;
import dev.tori.wraith.event.status.StatusEvent;
import dev.tori.wraith.listener.LambdaEventListener;
import dev.tori.wraith.util.IndexedHashSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * General tests for {@link EventBus}.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.1.0
 */
class EventBusTest {

    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new EventBus();
    }

    @Test
    void register() {
        Assertions.assertTrue(eventBus.getListeners().isEmpty(), "The event bus should have an empty listener set initially.");

        eventBus.register(new LambdaEventListener<>(Target.all(), 0, 1, System.out::println));

        Assertions.assertFalse(eventBus.isSorted(), "The event bus should not be sorted initially or after listener registration.");
        Assertions.assertFalse(eventBus.getListeners().isEmpty(), "The event bus should not have an empty listener set after listener registration.");
        Assertions.assertEquals(1, eventBus.getListeners().size(), "The event bus should have a single listener after a single listener is registered.");

        Assertions.assertThrows(NullPointerException.class, () -> eventBus.register(null), "Registering a null listener should throw a NullPointerException.");
    }

    @Test
    void unregister() {
        Assertions.assertTrue(eventBus.getListeners().isEmpty(), "The event bus should have an empty listener set initially.");

        eventBus.register(new LambdaEventListener<>(Target.all(), 0, 1, System.out::println));
        Assertions.assertFalse(eventBus.getListeners().isEmpty(), "The event bus should not have an empty listener set after listener registration.");

        eventBus.unregister(eventBus.getListeners().get(0));
        Assertions.assertTrue(eventBus.getListeners().isEmpty(), "The event bus should have an empty listener set after all listeners are unregistered.");

        Assertions.assertThrows(NullPointerException.class, () -> eventBus.unregister(null), "Unregistering a null listener should throw a NullPointerException.");
    }

    @Test
    void dispatch() {
        eventBus.register(new LambdaEventListener<StatusEvent>(Target.all(), IStatusEvent::terminate));
        Assertions.assertTrue(eventBus.dispatch(new StatusEvent()), "Dispatching a status event that is cancelled during listener invokation should return true.");

        Assertions.assertThrows(NullPointerException.class, () -> eventBus.dispatch(null), "Dispatching a null event should throw a NullPointerException.");
        Assertions.assertThrows(NullPointerException.class, () -> eventBus.dispatch("", null), "Dispatching an event with a null target should throw a NullPointerException.");
        Assertions.assertThrows(NullPointerException.class, () -> eventBus.dispatch(null, null), "Dispatching a null event with a null target should throw a NullPointerException.");

        eventBus.shutdown();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> eventBus.dispatch(""), "Dispatching an event after shutdown should throw an UnsupportedOperationException.");
    }

    @Test
    void getListeners() {
        Assertions.assertTrue(eventBus.getListeners().isEmpty(), "The event bus should have an empty listener set initially.");
        Assertions.assertInstanceOf(IndexedHashSet.class, eventBus.getListeners(), "The event bus's listener set should be an instance of IndexedHashSet.");
    }
}