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