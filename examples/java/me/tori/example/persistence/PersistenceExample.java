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

package me.tori.example.persistence;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.bus.IEventBus;
import me.tori.wraith.listener.LambdaEventListener;
import me.tori.wraith.subscriber.Subscriber;

/**
 * A simple example of listener persistence.
 * <p>
 * Last updated for version <b>3.1.0</b>
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.2.0
 */
class PersistenceExample {

    private static final IEventBus EVENT_BUS = new EventBus();
    private static final Subscriber SUBSCRIBER = new Subscriber() {{
        // Register a listener that prints a single
        // String event & is then removed from the event bus
        registerListener(new LambdaEventListener<>(String.class, null, IEventBus.DEFAULT_PRIORITY, 1, System.out::println));
    }};

    public static void main(String[] args) {
        // Subscribe our subscriber. This will never be removed from the event bus.
        EVENT_BUS.subscribe(SUBSCRIBER);

        // After this event is dispatched, and it's listener invoked,
        // the listener will be removed since it's set to only persist once.
        EVENT_BUS.dispatch("Hello world!");

        // You can still dispatch events to remaining listeners.
        EVENT_BUS.dispatch("I wont be printed :(");
    }
}