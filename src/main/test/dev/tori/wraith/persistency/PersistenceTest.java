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

package dev.tori.wraith.persistency;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.status.StatusEvent;
import dev.tori.wraith.listener.EventListener;
import dev.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.2.0
 */
public class PersistenceTest {

    @Test
    public void testRandomPersistence() {
        final EventBus bus = new EventBus();

        final int persists = ThreadLocalRandom.current().nextInt(1, 101);

        bus.subscribe(new Subscriber() {{
            registerListener(new MyListener(persists));
        }});

        for (int i = 0; i < persists; i++) {
            Assertions.assertTrue(bus.dispatch(new MyEvent()));
        }

        Assertions.assertFalse(bus.dispatch(new MyEvent()));
    }

    @Test
    public void testIndefinitePersistence() {
        final EventBus bus = new EventBus();

        bus.subscribe(new Subscriber() {{
            registerListener(new MyListener(0)); // <= 0 means persist indefinitely
        }});

        for (int i = 0; i < 1_000; i++) {
            Assertions.assertTrue(bus.dispatch(new MyEvent()));
        }
    }

    static class MyListener extends EventListener<MyEvent> {

        public MyListener(int persists) {
            super(Target.fine(MyEvent.class), 0, persists);
        }

        @Override
        public void invoke(MyEvent event) {
            event.suppress();
        }
    }

    static class MyEvent extends StatusEvent {

    }
}