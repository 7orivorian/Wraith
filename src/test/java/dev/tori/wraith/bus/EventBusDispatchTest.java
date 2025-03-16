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

package dev.tori.wraith.bus;

import dev.tori.wraith.event.Target;
import dev.tori.wraith.listener.LambdaEventListener;
import dev.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventBusDispatchTest {

    private EventBus bus;

    @BeforeEach
    void setup() {
        bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new LambdaEventListener<>(Target.all(), 100, event -> {
                        if (event instanceof ModifiableEvent e) {
                            e.setMods(0);
                        }
                    }),
                    new LambdaEventListener<>(Target.fine(EventA.class), EventA::mod),
                    new LambdaEventListener<>(Target.fine(EventB.class), EventB::mod),
                    new LambdaEventListener<>(Target.fine(EventC.class), EventC::mod)
            );
        }});
    }

    @Test
    void testEventADispatch() {
        EventA event = new EventA();
        bus.dispatch(event);
        Assertions.assertEquals(1, event.getMods(), "EventA was not modified correctly.");
    }

    @Test
    void testEventBDispatch() {
        EventB event = new EventB();
        bus.dispatch(event);
        Assertions.assertEquals(1, event.getMods(), "EventB was not modified correctly.");
    }

    @Test
    void testEventCDispatch() {
        EventC event = new EventC();
        bus.dispatch(event);
        Assertions.assertEquals(1, event.getMods(), "EventC was not modified correctly.");
    }

    private static class EventA extends ModifiableEvent {

        @Override
        public String toString() {
            return "EventA{" +
                    "mods=" + mods +
                    '}';
        }
    }

    private static class EventB extends ModifiableEvent {

        @Override
        public String toString() {
            return "EventB{" +
                    "mods=" + mods +
                    '}';
        }
    }

    private static class EventC extends ModifiableEvent {

        @Override
        public String toString() {
            return "EventC{" +
                    "mods=" + mods +
                    '}';
        }
    }

    static class ModifiableEvent {

        protected int mods = 0;

        public void mod() {
            this.mods++;
        }

        public int getMods() {
            return mods;
        }

        public void setMods(int mods) {
            this.mods = mods;
        }
    }
}