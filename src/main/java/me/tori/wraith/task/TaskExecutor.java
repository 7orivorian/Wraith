package me.tori.wraith.task;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A simple task executor that allows scheduling and execution of tasks based on events.
 *
 * <p> Each task submitted to the executor will be executed once the corresponding targeted
 * event is dispatched. After execution, the task is removed and will not execute on
 * subsequent event dispatches unless explicitly resubmitted to the TaskExecutor.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public class TaskExecutor {

    private final ConcurrentHashMap<Class<?>, Queue<Runnable>> tasks;

    /**
     * Constructs a new TaskExecutor with an empty task mapping.
     */
    public TaskExecutor() {
        this.tasks = new ConcurrentHashMap<>();
    }

    /**
     * Executes all tasks associated with the given event's class.
     *
     * @param event The event for which associated tasks should be executed.
     * @return {@code true} if any tasks were executed, {@code false} otherwise.
     */
    public boolean onEvent(Object event) {
        Queue<Runnable> queue = tasks.get(event.getClass());
        if (queue != null) {
            boolean executed = false;
            while (!queue.isEmpty()) {
                Runnable task = queue.poll();
                task.run();
                executed = true;
            }
            return executed;
        }
        return false;
    }

    /**
     * Schedules a task to be executed when an event of the specified class is dispatched.
     *
     * @param target The class of event that should be targeted by the given task.
     * @param task   The task to be executed.
     */
    public void schedule(Class<?> target, Runnable task) {
        tasks.computeIfAbsent(target, clazz -> new ConcurrentLinkedQueue<>()).add(task);
    }

    /**
     * Clears all tasks associated with events, effectively resetting the task executor.
     */
    public void clear() {
        tasks.clear();
    }
}