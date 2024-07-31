/*
 * Copyright (c) 2024 7orivorian.
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

package dev.tori.wraith.openeventbus.registration;

import dev.tori.wraith.bus.OpenEventBus;
import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.status.IStatusEvent;
import dev.tori.wraith.event.status.StatusEvent;
import dev.tori.wraith.listener.LambdaEventListener;
import dev.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class RegistrationTest {

    @Test
    public void testResistration() {
        final OpenEventBus bus = new OpenEventBus();
        final LambdaEventListener<StatusEvent> listener = new LambdaEventListener<StatusEvent>(
                Target.fine(StatusEvent.class),
                IStatusEvent::terminate
        );

        bus.register(listener);
        Assertions.assertTrue(bus.dispatch(new StatusEvent()));

        bus.unregister(listener);
        Assertions.assertFalse(bus.dispatch(new StatusEvent()));
    }

    @Test
    public void testSubscription() {
        final OpenEventBus bus = new OpenEventBus();
        final Subscriber subscriber = new Subscriber() {{
            registerListener(new LambdaEventListener<StatusEvent>(Target.fine(StatusEvent.class), IStatusEvent::terminate));
        }};

        bus.subscribe(subscriber);
        Assertions.assertTrue(bus.dispatch(new StatusEvent()));

        bus.unsubscribe(subscriber);
        Assertions.assertFalse(bus.dispatch(new StatusEvent()));
    }

    @Test
    public void testLateSubscription() {
        final String expectedMessage = "Hello world!";

        final OpenEventBus bus = new OpenEventBus();
        final Subscriber subscriber = new Subscriber();

        bus.subscribe(subscriber);

        subscriber.registerListener(new LambdaEventListener<MyEvent>(Target.fine(MyEvent.class), event -> event.setMessage(expectedMessage)));

        MyEvent event = new MyEvent(null);
        bus.dispatch(event);

        Assertions.assertEquals(expectedMessage, event.message());
    }

    static final class MyEvent {

        private String message;

        MyEvent(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}