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

package dev.tori.wraith;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.targeted.ClassTargetingEvent;
import dev.tori.wraith.listener.EventListener;
import dev.tori.wraith.listener.Listener;
import dev.tori.wraith.subscriber.Subscriber;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class MainTwo {

    public static void main(String[] args) {
        final EventBus bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new ListenerOne(),
                    new ListenerTwo()
            );
        }});

        TestEvent event = new TestEvent(null);

        bus.dispatch(event);

        System.out.println(event);
    }

    static class ListenerOne extends EventListener<TestEvent> {

        public ListenerOne() {
            super(Target.fine(TestEvent.class));
        }

        @Override
        public void invoke(TestEvent event) {
            event.mod();
        }
    }

    static class ListenerTwo extends EventListener<TestEvent> {

        public ListenerTwo() {
            super(Target.fine(TestEvent.class));
        }

        @Override
        public void invoke(TestEvent event) {
            event.mod();
        }
    }

    static class TestEvent extends ClassTargetingEvent {

        private int mods = 0;

        public TestEvent(Class<? extends Listener<?>> target) {
            super(target);
        }

        public void mod() {
            this.mods++;
        }

        @Override
        public String toString() {
            return "mods=" + mods;
        }
    }
}