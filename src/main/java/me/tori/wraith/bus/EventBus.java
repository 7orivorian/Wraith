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

package me.tori.wraith.bus;

import me.tori.wraith.event.status.IStatusEvent;
import me.tori.wraith.event.targeted.IClassTargetingEvent;
import me.tori.wraith.listener.Listener;
import me.tori.wraith.subscriber.ISubscriber;
import me.tori.wraith.task.ScheduledTask;
import me.tori.wraith.task.TaskExecutor;
import me.tori.wraith.util.IndexedHashSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Default implementation of {@link IEventBus}.
 * <p>
 * Manages subscription, listener registration, and event dispatching.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventBus implements IEventBus {

    /**
     * The amount of {@linkplain EventBus} instances that have been created
     */
    private static int instances = 0;

    /**
     * This event bus's id
     */
    private final int id;
    /**
     * Indicates whether this event bus is shutdown.
     * <p>Shut-down event dispatchers cannot dispatch events, and throw {@link UnsupportedOperationException} when
     * attempting to do so
     */
    private boolean shutdown;
    /**
     * A {@link Set} of this event bus' {@link ISubscriber subscribers}
     */
    private final Set<ISubscriber> subscribers;
    /**
     * A {@link ConcurrentHashMap} of this event bus' {@link Listener listeners}
     */
    private final ConcurrentHashMap<Class<?>, IndexedHashSet<Listener>> listeners;
    /**
     * A {@link TaskExecutor} that manages the scheduling and execution of tasks associated with events. It provides
     * a mechanism to associate event classes with queues of tasks and ensures their orderly execution when the
     * corresponding events are dispatched.
     *
     * @see #scheduleTask(ScheduledTask)
     */
    private final TaskExecutor taskExecutor;

    /**
     * Creates a new event bus instance
     */
    public EventBus() {
        this.id = instances++;
        this.shutdown = false;
        this.subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.listeners = new ConcurrentHashMap<>();
        this.taskExecutor = new TaskExecutor();
    }

    /**
     * @return The amount of {@linkplain EventBus} instances that have been created.
     */
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
        Objects.requireNonNull(subscriber, "Cannot subscribe null to event bus " + id + "!");

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
     * subscriber's {@link Listener listeners}
     *
     * @param subscriber the {@link ISubscriber} to be unsubscribed
     * @throws NullPointerException if the given {@link ISubscriber} is {@code null}
     * @see #unregister(Listener)
     */
    @Override
    public void unsubscribe(ISubscriber subscriber) {
        Objects.requireNonNull(subscriber, "Cannot unsubscribe null from event bus " + id + "!");

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
     * Registers a {@link Listener listener} to this event bus
     *
     * @param listener the {@link Listener} to be registered
     * @throws NullPointerException if the given {@link Listener} is {@code null}
     */
    @Override
    public void register(Listener<?> listener) {
        Objects.requireNonNull(listener, "Cannot register null listener to event bus " + id + "!");

        IndexedHashSet<Listener> listeners = this.listeners.computeIfAbsent(listener.getTarget(), target -> new IndexedHashSet<>());
        final int size = listeners.size();
        int index = 0;
        for (; index < size; index++) {
            if (listener.getPriority() > listeners.get(index).getPriority()) {
                break;
            }
        }
        listeners.add(index, listener);
    }

    /**
     * Unregisters a {@link Listener listener} from this event bus. A listener will no longer be invoked by events dispatched by this event bus
     *
     * @param listener the {@link Listener} to be unregistered
     * @throws NullPointerException if the given {@link Listener} is {@code null}
     */
    @Override
    public void unregister(Listener<?> listener) {
        Objects.requireNonNull(listener, "Cannot unregister null listener from event bus " + id + "!");
        listeners.get(listener.getTarget()).removeIf(l -> l.equals(listener));
    }

    /**
     * Convenience method to dispatch an event with no {@linkplain Listener#getType() listener type} restriction, and
     * normal processing priority.
     *
     * @see #dispatch(Object, Class, boolean)
     * @since 3.3.0
     */
    @Override
    public boolean dispatch(Object event) {
        return dispatch(event, null, false);
    }

    /**
     * Convenience method to dispatch an event with an optional {@linkplain Listener#getType() listener type} restriction, and
     * normal processing priority.
     *
     * @see #dispatch(Object, Class, boolean)
     * @since 3.3.0
     */
    @Override
    public boolean dispatch(Object event, Class<?> type) {
        return dispatch(event, type, false);
    }

    /**
     * Convenience method to dispatch an event with no {@linkplain Listener#getType() listener type} restriction, and
     * optional inverted processing priority.
     *
     * @see #dispatch(Object, Class, boolean)
     * @since 3.3.0
     */
    @Override
    public boolean dispatch(Object event, boolean invertPriority) {
        return dispatch(event, null, invertPriority);
    }

    /**
     * Dispatches the given event to all valid registered listeners.
     * <p>
     * The {@code type} parameter serves as a filtering mechanism for listeners, enabling you to selectively invoke
     * listeners based on their type, allowing for more targeted event handling.
     *
     * @param event the event to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     * @since 3.3.0
     */
    @Override
    public boolean dispatch(Object event, Class<?> type, boolean invertPriority) {
        Objects.requireNonNull(event, "Cannot dispatch a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            taskExecutor.onEvent(event);

            dispatchToEachListener(
                    event,
                    this.listeners.get(event.getClass()),
                    listener -> listener.isAcceptableType(type) && IClassTargetingEvent.isListenerTargetedByEvent(listener, event),
                    invertPriority
            );

            if (event instanceof IStatusEvent e) {
                return e.isSuppressed() || e.isTerminated();
            }
        }
        return false;
    }

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
     * resetting the task scheduling within this {@link EventBus}.
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
     * @since 3.1.0
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
     * @since 3.2.0
     */
    @SuppressWarnings("DuplicatedCode")
    private void dispatchToEachListener(Object event, IndexedHashSet<Listener> listeners, Predicate<Listener> predicate, boolean invertPriority) {
        if ((listeners != null) && !listeners.isEmpty()) {
            final ArrayList<Listener> li = listeners.asList();
            if (invertPriority) {
                for (int i = li.size() - 1; i >= 0; i--) {
                    Listener listener = li.get(i);
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
                for (Listener listener : li) {
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
     * @param o the object to compare with
     * @return {@code true} if the object is equal to this event bus, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        EventBus that = (EventBus) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "EventBus{" +
                "id=" + id +
                ", shutdown=" + shutdown +
                ", subscribers=" + subscribers +
                ", listeners=" + listeners +
                '}';
    }
}