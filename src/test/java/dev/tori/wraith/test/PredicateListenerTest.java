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
import dev.tori.wraith.listener.PredicateEventListener;
import dev.tori.wraith.subscriber.Subscriber;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.1.0
 */
public class PredicateListenerTest {

    @Test
    public void test() {
        final EventBus bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new PredicateEventListener<MyEvent>(Target.fine(MyEvent.class), event -> {
                        final Object val = event.val;
                        return val instanceof Integer;
                    }, 1, event -> {
                        // The event's initial value is 0, so we set it to 1
                        event.val = 1;
                    }),
                    new PredicateEventListener<MyEvent>(Target.fine(MyEvent.class), event -> {
                        final Object val = event.val;
                        return val instanceof Boolean;
                    }, 0, event -> {
                        // The event's initial value is false, so we set it to true
                        event.val = true;
                    })
            );
        }});

        MyEvent event = new MyEvent(0);
        bus.dispatch(event);
        Assertions.assertEquals(1, event.val);

        MyEvent event1 = new MyEvent(false);
        bus.dispatch(event1);
        Assertions.assertEquals(true, event1.val);
    }


    private static final class MyEvent {

        private Object val;

        @Contract(pure = true)
        private MyEvent(Object val) {
            this.val = val;
        }
    }
}