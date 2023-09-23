package me.tori.wraith.task;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a task that can be scheduled for execution based on events, with an optional delay.
 * The task's execution is triggered when an event of the specified target class is dispatched.
 *
 * <p> The delay value determines the number of event dispatches that should occur before the task is executed.
 * The delay decrements with each event dispatch, and once it becomes less than or equal to 0, the task is executed.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see TaskExecutor
 * @since <b>3.0.0</b>
 */
public abstract class ScheduledTask implements Runnable {

    private final @NotNull Class<?> target;
    private int delay;

    /**
     * Constructs a new ScheduledTask with the specified target class and no delay.
     *
     * @param target The target class representing the event that triggers the task's execution.
     */
    public ScheduledTask(@NotNull Class<?> target) {
        this(target, 0);
    }

    /**
     * Constructs a new ScheduledTask with the specified target class and delay.
     *
     * @param target The target class representing the event that triggers the task's execution.
     * @param delay  The delay value, determining the number of event dispatches before execution.
     */
    public ScheduledTask(@NotNull Class<?> target, int delay) {
        this.target = target;
        this.delay = delay;
    }

    /**
     * Returns the target class associated with this task.
     *
     * @return The target class representing the event that triggers the task's execution.
     */
    @NotNull
    public Class<?> getTarget() {
        return target;
    }

    /**
     * Checks if this task's target class matches the provided class.
     *
     * @param clazz The class to compare with this task's target class.
     * @return {@code true} if the classes match, {@code false} otherwise.
     */
    public boolean isTarget(Class<?> clazz) {
        return target.equals(clazz);
    }

    /**
     * Returns the current delay value of the task.
     *
     * @return The delay value, representing the remaining number of event dispatches before execution.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Decrements the delay value by 1 and returns the updated delay.
     *
     * @return The updated delay value.
     */
    public int decrementDelay() {
        delay--;
        return delay;
    }
}