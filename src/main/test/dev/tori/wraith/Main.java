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

import dev.tori.wraith.event.Target;
import dev.tori.wraith.listener.EventListener;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
public class Main {

    public static void main(String[] args) {
        /*Target target = Target.reverseCascade(null);

        System.out.println(target.targets(ParentListenerOne.class));
        System.out.println(target.targets(ParentListenerOne.NestedListenerOne.class));
        System.out.println(target.targets(ParentListenerOne.NestedListenerOther.class));
        System.out.println(target.targets(ParentListenerOne.NestedListenerOne.DoubleNestedListenerOne.class));

        System.out.println(target.targets(ParentListenerTwo.class));
        System.out.println(target.targets(ParentListenerTwo.NestedListenerTwo.class));*/

        Target target = Target.cascade(Object.class);
        System.out.println(target.targets(Event.class));

/*        final EventBus bus = new EventBus();
        bus.subscribe(new Subscriber() {{
            registerListeners(
                    new ParentListenerOne(),
                    new ParentListenerOne.NestedListenerOne(),
                    new ParentListenerOne.NestedListenerOne.DoubleNestedListenerOne(),
                    new ParentListenerOne.NestedListenerOther(),
                    new ParentListenerTwo(),
                    new ParentListenerTwo.NestedListenerTwo()
            );
        }});

        Event event = new Event();
        bus.dispatch(event);
        System.out.println(event);

        Event.NestedEvent nestedEvent = new Event.NestedEvent();
        bus.dispatch(nestedEvent);
        System.out.println(nestedEvent);*/
    }

    static class ParentListenerOne extends EventListener<Event> {

        public ParentListenerOne() {
            super(Target.cascade(Event.class));
        }

        @Override
        public void invoke(Event event) {
            event.mod();
        }

        static class NestedListenerOne extends ParentListenerOne {

            static class DoubleNestedListenerOne extends NestedListenerOne {

            }
        }

        static class NestedListenerOther extends EventListener<Event> {

            public NestedListenerOther() {
                super(Target.fine(Event.class));
            }

            @Override
            public void invoke(Event event) {
                event.mod();
            }
        }
    }

    static class ParentListenerTwo extends EventListener<Event> {

        public ParentListenerTwo() {
            super(Target.fine(Event.class));
        }

        @Override
        public void invoke(Event event) {
            event.mod();
        }

        static class NestedListenerTwo extends ParentListenerTwo {

        }
    }

    static class Event {

        private int mods = 0;

        public void mod() {
            this.mods++;
        }

        @Override
        public String toString() {
            return "mods=" + mods;
        }

        static class NestedEvent extends Event {

            static class DoubleNestedEvent extends NestedEvent {

            }
        }
    }
}