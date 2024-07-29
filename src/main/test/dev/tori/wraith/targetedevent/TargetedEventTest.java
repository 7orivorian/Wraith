/*
 * Copyright (c) 2021-2024 7orivorian.
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

package dev.tori.wraith.targetedevent;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.status.StatusEvent;
import dev.tori.wraith.event.targeted.IClassTargetingEvent;
import dev.tori.wraith.listener.EventListener;
import dev.tori.wraith.listener.Listener;
import dev.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 */
public class TargetedEventTest {

    @Test
    public void testTargetedEventWithLowerPriority() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new MyListener(0),
                    new OtherListener(1)
            );
        }});

        TestEvent event = new TestEvent(MyListener.class);
        assertFalse(bus.dispatch(event));
    }

    @Test
    public void testTargetedEventWithHigherPriority() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new MyListener(1),
                    new OtherListener(0)
            );
        }});

        TestEvent event = new TestEvent(MyListener.class);
        assertFalse(bus.dispatch(event));
    }

    @Test
    public void testNestedClassTargetNestedListener() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new MyParentListener.MyNestedListener(0)
            );
        }});

        TestEvent event = new TestEvent(MyParentListener.MyNestedListener.class);
        bus.dispatch(event);

        // Dispatched to nested target
        assertEquals(2, event.getValue());
    }

    @Test
    public void parentListenerNestedTarget() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new MyParentListener(0)
            );
        }});

        TestEvent event = new TestEvent(MyParentListener.MyNestedListener.class);
        bus.dispatch(event);

        // Not dispatched to parent of nested target
        assertEquals(0, event.getValue());
    }

    @Test
    public void testParentOfNestedClassTarget() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new MyParentListener.MyNestedListener(0)
            );
        }});

        TestEvent event = new TestEvent(MyParentListener.class);
        bus.dispatch(event);

        // Dispatched to nested listener if the parent is targeted
        assertEquals(2, event.getValue());
    }

    static class MyListener extends EventListener<TestEvent> {

        public MyListener(int priority) {
            super(TestEvent.class, priority);
        }

        @Override
        public void invoke(TestEvent event) {
            event.setSuppressed(false);
        }
    }

    static class OtherListener extends EventListener<TestEvent> {

        public OtherListener(int priority) {
            super(TestEvent.class, priority);
        }

        @Override
        public void invoke(TestEvent event) {
            event.suppress();
        }
    }

    static class MyParentListener extends EventListener<TestEvent> {

        public MyParentListener(int priority) {
            super(TestEvent.class, priority);
        }

        @Override
        public void invoke(TestEvent event) {
            event.setValue(1);
        }

        static class MyNestedListener extends MyParentListener {

            public MyNestedListener(int priority) {
                super(priority);
            }

            @Override
            public void invoke(TestEvent event) {
                event.setValue(2);
            }
        }
    }

    static class TestEvent extends StatusEvent implements IClassTargetingEvent {

        private final Class<? extends Listener<?>> targetClass;
        private int value;

        public TestEvent(Class<? extends Listener<?>> target) {
            this.targetClass = target;
            this.value = 0;
        }

        @Override
        public Class<? extends Listener<?>> getTargetClass() {
            return targetClass;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}