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

package dev.tori.wraith.bus;

import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.status.IStatusEvent;
import dev.tori.wraith.listener.Listener;
import dev.tori.wraith.subscriber.ISubscriber;
import dev.tori.wraith.task.ScheduledTask;
import dev.tori.wraith.task.TaskExecutor;
import dev.tori.wraith.util.IndexedHashSet;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Abstract implementation of {@link IEventBus}.
 * <p>
 * Manages subscription and task scheduling.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractEventBus implements IEventBus {

    /**
     * The amount of {@linkplain AbstractEventBus} instances that have been created.
     */
    private static int instances = 0;

    /**
     * This event bus's id.
     */
    protected final int id;
    /**
     * Indicates whether this event bus is shutdown.
     *
     * @apiNote Shut-down event dispatchers cannot dispatch events, and throw {@link UnsupportedOperationException} when
     * attempting to do so.
     */
    protected boolean shutdown;
    /**
     * A {@link Set} of this event bus' {@link ISubscriber subscribers}.
     */
    protected final Set<ISubscriber> subscribers;
    /**
     * A {@link TaskExecutor} that manages the scheduling and execution of tasks associated with events. It provides
     * a mechanism to associate event classes with queues of tasks and ensures their orderly execution when the
     * corresponding events are dispatched.
     *
     * @see #scheduleTask(ScheduledTask)
     */
    protected final TaskExecutor taskExecutor;

    /**
     * Creates a new event bus instance.
     */
    public AbstractEventBus() {
        this.id = instances++;
        this.shutdown = false;
        this.subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.taskExecutor = new TaskExecutor();
    }

    /**
     * @return The amount of {@linkplain AbstractEventBus} instances that have been created.
     */
    @Contract(pure = true)
    public static int getInstanceCount() {
        return instances;
    }

    /**
     * Subscribes a {@link ISubscriber subscriber} to this event bus and registers all the
     * subscriber's {@link Listener listeners}
     *
     * @param subscriber the {@link ISubscriber} to be subscribed
     * @throws NullPointerException if the given {@link ISubscriber} is {@code null}
     * @see #register(Listener)
     */
    @Override
    public void subscribe(ISubscriber subscriber) {
        Objects.requireNonNull(subscriber, "Cannot subscribe null to event bus " + id + ".");

        subscribers.add(subscriber);
        subscriber.linkToBus(this);

        Collection<Listener<?>> listeners = subscriber.getListeners();
        if (!listeners.isEmpty()) {
            if (listeners.size() == 1) {
                register(listeners.iterator().next());
            } else {
                for (Listener<?> listener : listeners) {
                    register(listener);
                }
            }
        }
    }

    /**
     * Unsubscribes a {@link ISubscriber subscriber} from this event bus and unregisters all the
     * subscriber's {@link Listener listeners}.
     *
     * @param subscriber the {@link ISubscriber} to be unsubscribed
     * @throws NullPointerException if the given {@link ISubscriber} is {@code null}
     * @see #unregister(Listener)
     */
    @Override
    public void unsubscribe(ISubscriber subscriber) {
        Objects.requireNonNull(subscriber, "Cannot unsubscribe null from event bus " + id + ".");

        subscribers.remove(subscriber);
        subscriber.unlinkFromBus(this);

        Collection<Listener<?>> listeners = subscriber.getListeners();
        if (!listeners.isEmpty()) {
            if (listeners.size() == 1) {
                unregister(listeners.iterator().next());
            } else {
                for (Listener<?> listener : listeners) {
                    unregister(listener);
                }
            }
        }
    }

    /**
     * Unsubscribes all {@link ISubscriber subscribers} and unregisters all their
     * {@link Listener listeners} from this event bus.
     *
     * @apiNote Listeners registered directly to this event bus will not be unregistered.
     */
    public void unsubscribeAll() {
        for (ISubscriber subscriber : subscribers) {
            subscriber.unlinkFromBus(this);
        }
        subscribers.clear();
    }

    /**
     * Registers a {@link Listener listener} to this event bus
     *
     * @param listener the {@link Listener} to be registered
     * @throws NullPointerException if the given {@link Listener} is {@code null}
     */
    @Override
    public abstract void register(Listener<?> listener);

    /**
     * Unregisters a {@link Listener listener} from this event bus. A listener will no longer be invoked by events dispatched by this event bus
     *
     * @param listener the {@link Listener} to be unregistered
     * @throws NullPointerException if the given {@link Listener} is {@code null}
     */
    @Override
    public abstract void unregister(Listener<?> listener);

    /**
     * Convenience method to dispatch an event with no {@linkplain Target target listener}, and
     * normal processing priority.
     *
     * @see #dispatch(Object, Target, boolean)
     */
    @Override
    public boolean dispatch(Object event) {
        return dispatch(event, Target.all(), false);
    }

    /**
     * Convenience method to dispatch an event with an optional {@linkplain Target target listener} and
     * normal processing priority.
     *
     * @see #dispatch(Object, Target, boolean)
     */
    @Override
    public boolean dispatch(Object event, Target target) {
        return dispatch(event, target, false);
    }

    /**
     * Convenience method to dispatch an event with a universal {@linkplain Target target listener} and
     * optional inverted processing priority.
     *
     * @see #dispatch(Object, Target, boolean)
     */
    @Override
    public boolean dispatch(Object event, boolean invertPriority) {
        return dispatch(event, Target.all(), invertPriority);
    }

    /**
     * Dispatches the given event to all valid registered listeners.
     * <p>
     * The {@code type} parameter serves as a filtering mechanism for listeners, enabling you to selectively invoke
     * listeners based on their type, allowing for more targeted event handling.
     *
     * @param event          the event to be dispatched.
     * @param target         the {@linkplain Target target listener} to invoke.
     * @param invertPriority flag to dispatch the event in inverse listener priority.
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise.
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public abstract boolean dispatch(Object event, Target target, boolean invertPriority);

    /**
     * Schedules a task to be executed.
     *
     * @param task The task to be executed.
     * @see TaskExecutor#schedule(ScheduledTask)
     * @see ScheduledTask
     */
    public void scheduleTask(ScheduledTask task) {
        taskExecutor.schedule(task);
    }

    /**
     * Clears all tasks associated with events from the underlying {@link #taskExecutor}, effectively
     * resetting the task scheduling within this {@link AbstractEventBus}.
     *
     * @see TaskExecutor#clear()
     */
    public void clearTaskExecutor() {
        taskExecutor.clear();
    }

    /**
     * Logs a warning message and shuts down this event bus, preventing future events from being dispatched.
     *
     * @implNote Shut-down event dispatchers cannot dispatch events, and throw {@link UnsupportedOperationException}
     * when attempting to do so.
     */
    @Override
    public void shutdown() {
        shutdown = true;
    }

    /**
     * @return {@code true} if this event bus is shut down
     * @implNote Shut-down event dispatchers cannot dispatch events, and throw {@link UnsupportedOperationException}
     * when attempting to do so.
     */
    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * @return the {@code id} of this event bus
     */
    public int getId() {
        return id;
    }

    /**
     * Applies a given action to each {@linkplain Listener} in a list that matches a specified predicate.
     * Listeners are processed either in normal order or in reverse order based on the
     * {@code invertPriority} flag.
     * Listeners that should not persist are removed from the list after the action is applied.
     *
     * @param event          the event to be handled by each listener that satisfies the predicate
     * @param listeners      the list of listeners to be processed
     * @param predicate      the condition that each listener must satisfy to have the action applied
     * @param invertPriority if {@code true}, listeners are processed in order of inverse priority; otherwise,
     *                       they are processed in normal order
     */
    @SuppressWarnings("DuplicatedCode")
    protected final void dispatchToEachListener(Object event, IndexedHashSet<Listener> listeners, Predicate<Listener> predicate, boolean invertPriority) {
        if ((listeners != null) && !listeners.isEmpty()) {
            if (invertPriority) {
                for (int i = listeners.size() - 1; i >= 0; i--) {
                    Listener listener = listeners.get(i);
                    if (!predicate.test(listener)) {
                        continue;
                    }

                    listener.invoke(event);

                    if ((event instanceof IStatusEvent e) && e.isTerminated()) {
                        break;
                    }
                    if (!listener.shouldPersist()) {
                        listeners.remove(listener);
                    }
                }
            } else {
                for (int i = 0; i < listeners.size(); i++) {
                    Listener listener = listeners.get(i);
                    if (!predicate.test(listener)) {
                        continue;
                    }

                    listener.invoke(event);

                    if ((event instanceof IStatusEvent e) && e.isTerminated()) {
                        break;
                    }
                    if (!listener.shouldPersist()) {
                        listeners.remove(listener);
                    }
                }
            }
        }
    }

    /**
     * Checks if this event bus is equal to another object.
     * <p>
     * If the given object is an event bus, it is only considered equal if {@code this.id == that.id}.
     *
     * @param obj the object to compare with
     * @return {@code true} if the object is equal to this event bus, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        AbstractEventBus that = (AbstractEventBus) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public abstract String toString();
}