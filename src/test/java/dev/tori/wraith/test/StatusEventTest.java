/*
 * Copyright (c) 2024-2025 7orivorian.
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

package dev.tori.wraith.test;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.status.StatusEvent;
import dev.tori.wraith.listener.LambdaEventListener;
import dev.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.2.0
 */
public class StatusEventTest {

    @Test
    public void testSupression() {
        final EventBus bus = new EventBus();

        MySubscriber subscriber = new MySubscriber() {{
            registerListeners(
                    new LambdaEventListener<StatusEvent>(Target.fine(StatusEvent.class), 1, event -> {
                        this.counter = 1;
                        event.suppress();
                    }),
                    new LambdaEventListener<StatusEvent>(Target.fine(StatusEvent.class), 0, event -> {
                        this.counter++;
                    })
            );
        }};
        bus.subscribe(subscriber);

        Assertions.assertTrue(bus.dispatch(new StatusEvent()));
        Assertions.assertEquals(2, subscriber.counter);
    }

    @Test
    public void testTermination() {
        final EventBus bus = new EventBus();

        MySubscriber subscriber = new MySubscriber() {{
            registerListeners(
                    new LambdaEventListener<StatusEvent>(Target.fine(StatusEvent.class), 1, event -> {
                        this.counter = 1;
                        event.terminate();
                    }),
                    new LambdaEventListener<StatusEvent>(Target.fine(StatusEvent.class), 0, event -> {
                        this.counter = 1_000;
                    })
            );
        }};
        bus.subscribe(subscriber);

        Assertions.assertTrue(bus.dispatch(new StatusEvent()));
        Assertions.assertEquals(1, subscriber.counter);
    }

    private static class MySubscriber extends Subscriber {

        public int counter = 0;

        public MySubscriber() {

        }
    }
}