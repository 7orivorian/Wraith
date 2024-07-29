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

package dev.tori.example.simple;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.status.StatusEvent;
import dev.tori.wraith.listener.LambdaEventListener;
import dev.tori.wraith.subscriber.Subscriber;

/**
 * One-class example.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 */
class SimpleExample {

    public static void main(String[] args) {
        // Create an event bus
        EventBus bus = new EventBus();

        // Create our subscriber
        SimpleSubscriber subscriber = new SimpleSubscriber();

        // Subscribe to the event bus
        bus.subscribe(subscriber);

        // Create a simple event
        SimpleEvent event = new SimpleEvent("Pie is delicious <3");

        // Dispatch our event
        bus.dispatch(event);
    }

    private static final class SimpleSubscriber extends Subscriber {

        public SimpleSubscriber() {
            registerListener(
                    new LambdaEventListener<>(SimpleEvent.class, event -> System.out.println(event.getMessage()))
            );
        }
    }

    private static final class SimpleEvent extends StatusEvent {

        private final String message;

        public SimpleEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}