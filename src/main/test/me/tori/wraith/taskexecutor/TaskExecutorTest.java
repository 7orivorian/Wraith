package me.tori.wraith.taskexecutor;

import me.tori.wraith.task.TaskExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public class TaskExecutorTest {

    @Test
    public void testSingleExecution() {
        TaskExecutor executor = new TaskExecutor();
        executor.schedule(TestEvent.class, () -> {
        });

        Assertions.assertTrue(executor.onEvent(new TestEvent()));
        Assertions.assertFalse(executor.onEvent(new TestEvent()));
    }

    @Test
    public void testTaskTarget() {
        TaskExecutor executor = new TaskExecutor();
        executor.schedule(TestEvent.class, () -> {
        });

        Assertions.assertFalse(executor.onEvent(new OtherEvent()));
        Assertions.assertTrue(executor.onEvent(new TestEvent()));
    }

    private static final class TestEvent {

    }

    private static final class OtherEvent {

    }
}