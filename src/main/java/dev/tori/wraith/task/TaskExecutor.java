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

package dev.tori.wraith.task;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    @Contract(pure = true)
    public TaskExecutor() {
        this.tasks = new ConcurrentHashMap<>();
    }

    /**
     * Executes all tasks associated with the given event's class.
     *
     * @param event The event for which associated tasks should be executed.
     * @return {@code true} if any tasks were executed, {@code false} otherwise.
     */
    public boolean onEvent(@NotNull Object event) {
        ArrayList<ScheduledTask> queue = tasks.get(event.getClass());
        if ((queue != null) && !queue.isEmpty()) {
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
    public void schedule(@NotNull ScheduledTask task) {
        tasks.computeIfAbsent(task.getTarget(), clazz -> new ArrayList<>()).add(task);
    }

    /**
     * Clears all tasks associated with events, effectively resetting the task executor.
     */
    public void clear() {
        tasks.clear();
    }

    @Override
    public String toString() {
        return "TaskExecutor{" +
                "tasks=" + tasks +
                '}';
    }
}