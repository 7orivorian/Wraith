package me.tori.wraith.task;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple task executor that allows scheduling and execution of tasks based on events.
 *
 * <p> Each task submitted to the executor will be executed once its delay has elapsed
 * and the corresponding targeted event is dispatched. After execution, the task is
 * removed and will not execute on subsequent event dispatches unless explicitly resubmitted
 * to the TaskExecutor.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see ScheduledTask
 * @since <b>3.0.0</b>
 */
public class TaskExecutor {

    private final ConcurrentHashMap<Class<?>, ArrayList<ScheduledTask>> tasks;

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
        ArrayList<ScheduledTask> queue = tasks.get(event.getClass());
        if (queue != null) {
            boolean executed = false;
            for (int i = 0; i < queue.size(); ) {
                ScheduledTask task = queue.get(i);
                if (task.decrementDelay() < 0) {
                    task.run();
                    executed = true;
                }
                i++;
            }
            queue.removeIf(task -> task.getDelay() < 0);
            return executed;
        }
        return false;
    }

    /**
     * Schedules a task to be executed when an event of the specified class is dispatched.
     *
     * @param task The task to be executed.
     * @see ScheduledTask
     */
    public void schedule(ScheduledTask task) {
        tasks.computeIfAbsent(task.getTarget(), clazz -> new ArrayList<>()).add(task);
    }

    /**
     * Clears all tasks associated with events, effectively resetting the task executor.
     */
    public void clear() {
        tasks.clear();
    }
}