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

package dev.tori.example.taskexecutor;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.task.ScheduledTask;
import dev.tori.wraith.task.TaskExecutor;

/**
 * A simple example showing how to use the {@link TaskExecutor}
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.2.0
 */
public class TaskExecutorExample {

    private static final EventBus EVENT_BUS = new EventBus();

    public static void main(final String[] args) {
        // Schedule a task to run when the next String event is dispatched
        EVENT_BUS.scheduleTask(new ScheduledTask(String.class, 3) {
            @Override
            public void run() {
                System.out.println("Hello World!");
            }
        });

        // These events are ignored
        EVENT_BUS.dispatch("just a string event");
        EVENT_BUS.dispatch("just a string event");
        EVENT_BUS.dispatch("just a string event");

        // This event will trigger the previously scheduled task.
        // Note that scheduled tasks will be run before any listeners
        // are invoked, regardless of listener priority.
        EVENT_BUS.dispatch("just a string event");
    }
}