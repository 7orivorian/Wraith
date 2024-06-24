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

package me.tori.wraith.taskexecutor;

import me.tori.wraith.task.ScheduledTask;
import me.tori.wraith.task.TaskExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public class TaskExecutorTest {

    @Test
    public void testTaskTarget() {
        TaskExecutor executor = new TaskExecutor();
        executor.schedule(new ScheduledTask(TargetEvent.class) {
            @Override
            public void run() {

            }
        });

        Assertions.assertFalse(executor.onEvent(new OtherEvent()));
        Assertions.assertTrue(executor.onEvent(new TargetEvent()));
    }

    @Test
    public void testSingleExecution() {
        TaskExecutor executor = new TaskExecutor();
        executor.schedule(new ScheduledTask(TargetEvent.class) {
            @Override
            public void run() {

            }
        });

        Assertions.assertTrue(executor.onEvent(new TargetEvent()));
        Assertions.assertFalse(executor.onEvent(new TargetEvent()));
    }

    @Test
    public void testDelayedSingleExecution() {
        TaskExecutor executor = new TaskExecutor();

        // Schedule a task to execute after skipping 1 event
        executor.schedule(new ScheduledTask(TargetEvent.class, 1) {
            @Override
            public void run() {

            }
        });

        // This event should be skipped
        Assertions.assertFalse(executor.onEvent(new TargetEvent()));

        // Tasks should be executed on this event
        Assertions.assertTrue(executor.onEvent(new TargetEvent()));

        // This event should be skipped
        Assertions.assertFalse(executor.onEvent(new TargetEvent()));


        // Schedule a task to execute after skipping 3 events
        executor.schedule(new ScheduledTask(TargetEvent.class, 3) {
            @Override
            public void run() {

            }
        });

        // These events should be skipped
        Assertions.assertFalse(executor.onEvent(new TargetEvent()));
        Assertions.assertFalse(executor.onEvent(new TargetEvent()));
        Assertions.assertFalse(executor.onEvent(new TargetEvent()));

        // Tasks should be executed on this event
        Assertions.assertTrue(executor.onEvent(new TargetEvent()));

        // This event should be skipped
        Assertions.assertFalse(executor.onEvent(new TargetEvent()));
    }

    private static final class TargetEvent {

    }

    private static final class OtherEvent {

    }
}