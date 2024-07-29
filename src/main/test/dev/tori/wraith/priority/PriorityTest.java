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

package dev.tori.wraith.priority;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.status.StatusEvent;
import dev.tori.wraith.listener.LambdaEventListener;
import dev.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class PriorityTest {

    @Test
    public void testPriority() {
        final EventBus bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListener(new LambdaEventListener<>(MyEvent.class, 5, event -> event.setFlag(true)));
            registerListener(new LambdaEventListener<>(MyEvent.class, 4, event -> {
                event.setFlag(false);
                event.terminate();
            }));
            registerListener(new LambdaEventListener<>(MyEvent.class, 3, event -> event.setFlag(true)));
            registerListener(new LambdaEventListener<>(MyEvent.class, 2, event -> event.setFlag(true)));
            registerListener(new LambdaEventListener<>(MyEvent.class, 1, event -> event.setFlag(true)));
            registerListener(new LambdaEventListener<>(MyEvent.class, 0, event -> event.setFlag(true)));
        }});

        MyEvent event = new MyEvent();
        Assertions.assertTrue(bus.dispatch(event));
        Assertions.assertFalse(event.flag());
    }

    static class MyEvent extends StatusEvent {

        private boolean flag;

        MyEvent() {
            this.flag = true;
        }

        public boolean flag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}