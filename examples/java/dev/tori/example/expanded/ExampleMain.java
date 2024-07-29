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

package dev.tori.example.expanded;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.bus.IEventBus;
import dev.tori.wraith.event.staged.EventStage;

/**
 * Example program.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 1.0.0
 */
class ExampleMain {

    private static final IEventBus EVENT_BUS = new EventBus();

    private static final ExampleSubscriber EXAMPLE_SUBSCRIBER = new ExampleSubscriber();

    public static void main(String[] args) {
        EVENT_BUS.subscribe(EXAMPLE_SUBSCRIBER);

        ExampleEvent event1 = new ExampleEvent(EventStage.PRE);

        EVENT_BUS.dispatch(event1);

        if (!event1.isSuppressed()) {
            System.out.println(event1.getMessage());
        }

        ExampleEvent event2 = new ExampleEvent(EventStage.POST);

        EVENT_BUS.dispatch(event2);

        if (!event2.isSuppressed()) {
            System.out.println(event2.getMessage());
        }
    }
}