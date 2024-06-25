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

package me.tori.wraith.targetedevent;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.event.cancelable.CancelableEvent;
import me.tori.wraith.event.targeted.IClassTargetingEvent;
import me.tori.wraith.listener.EventListener;
import me.tori.wraith.listener.Listener;
import me.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 */
public class TargetedEventTest {

    @Test
    public void testTargetedEvent() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new MyListener(),
                    new OtherListener()
            );
        }});

        TestEvent event = new TestEvent(MyListener.class);
        assertFalse(bus.dispatchTargeted(event));
    }

    public static class MyListener extends EventListener<TestEvent> {

        public MyListener() {
            super(TestEvent.class);
        }

        @Override
        public void invoke(TestEvent event) {

        }
    }

    public static class OtherListener extends EventListener<TestEvent> {

        public OtherListener() {
            super(TestEvent.class);
        }

        @Override
        public void invoke(TestEvent event) {
            event.cancel();
        }
    }

    public static class TestEvent extends CancelableEvent implements IClassTargetingEvent {

        private final Class<? extends Listener<?>> targetClass;

        public TestEvent(Class<? extends Listener<?>> target) {
            this.targetClass = target;
        }

        @Override
        public Class<? extends Listener<?>> getTargetClass() {
            return targetClass;
        }
    }
}