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

package dev.tori.wraith.listener;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.Target;
import dev.tori.wraith.subscriber.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class ListenerTargetingTest {

    @Test
    void testGlobalTargeting() {
        final EventBus bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new LambdaEventListener<>(Target.all(), -100, event -> {
                        if (event instanceof EventA a) {
                            a.mods = -1;
                        } else if (event instanceof EventB b) {
                            b.mods = -1;
                        } else if (event instanceof EventC c) {
                            c.mods = -1;
                        }
                    }),
                    new LambdaEventListener<>(Target.fine(EventA.class), EventA::mod),
                    new LambdaEventListener<>(Target.fine(EventB.class), EventB::mod),
                    new LambdaEventListener<>(Target.fine(EventC.class), EventC::mod)
            );
        }});

        EventA a = new EventA();
        bus.dispatch(a);
        EventB b = new EventB();
        bus.dispatch(b);
        EventC c = new EventC();
        bus.dispatch(c);

        Assertions.assertEquals(-1, a.mods);
        Assertions.assertEquals(-1, b.mods);
        Assertions.assertEquals(-1, c.mods);
    }

    @Test
    void testFineTargeting() {
        final EventBus bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new LambdaEventListener<>(Target.fine(EventA.class), EventA::mod),
                    new LambdaEventListener<>(Target.fine(EventB.class), EventB::mod),
                    new LambdaEventListener<>(Target.fine(EventC.class), EventC::mod)
            );
        }});

        EventA a = new EventA();
        bus.dispatch(a);

        Assertions.assertEquals(1, a.mods);
    }

    @Test
    void testCascadeTargeting() {
        final EventBus bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new LambdaEventListener<>(Target.cascade(ParentEvent.class), ParentEvent::mod)
            );
        }});

        ParentEvent parentEvent = new ParentEvent();
        bus.dispatch(parentEvent);
        Assertions.assertEquals(1, parentEvent.mods);

        ParentEvent.ChildEvent childEvent = new ParentEvent.ChildEvent();
        bus.dispatch(childEvent);
        Assertions.assertEquals(2, childEvent.mods);
    }

    @Test
    void testReverseCascadeTargeting() {
        final EventBus bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new LambdaEventListener<>(Target.reverseCascade(ParentEvent.ChildEvent.class), ParentEvent::mod)
            );
        }});

        ParentEvent parentEvent = new ParentEvent();
        bus.dispatch(parentEvent);
        Assertions.assertEquals(1, parentEvent.mods);

        ParentEvent.ChildEvent childEvent = new ParentEvent.ChildEvent();
        bus.dispatch(childEvent);
        Assertions.assertEquals(2, childEvent.mods);
    }

    private static class EventA {

        public int mods = 0;

        public void mod() {
            this.mods++;
        }
    }

    private static class EventB {

        public int mods = 0;

        public void mod() {
            this.mods++;
        }
    }

    private static class EventC {

        public int mods = 0;

        public void mod() {
            this.mods++;
        }
    }

    private static class ParentEvent {

        public int mods = 0;

        public void mod() {
            this.mods++;
        }

        private static class ChildEvent extends ParentEvent {

            @Override
            public void mod() {
                this.mods += 2;
            }
        }
    }
}