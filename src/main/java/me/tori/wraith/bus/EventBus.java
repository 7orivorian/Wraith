package me.tori.wraith.bus;

import me.tori.wraith.event.cancelable.ICancelableEvent;
import me.tori.wraith.event.targeted.IClassTargetingEvent;
import me.tori.wraith.listener.Listener;
import me.tori.wraith.subscriber.ISubscriber;
import me.tori.wraith.task.ScheduledTask;
import me.tori.wraith.task.TaskExecutor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Default implementation of {@link IEventBus}, {@link TargetableEventBus}, and {@link InvertableEventBus}.
 *
 * <p>Manages event subscription, dispatching, and listener registration.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventBus implements TargetableEventBus, InvertableEventBus {

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
    private final ConcurrentHashMap<Class<?>, List<Listener>> listeners;
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
        Collection<Listener<?>> listeners = subscriber.getListeners();
        if (listeners.size() == 1) {
            register(listeners.iterator().next());
        } else {
            for (Listener<?> listener : listeners) {
                register(listener);
            }
        }
        subscribers.add(subscriber);
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
        Collection<Listener<?>> listeners = subscriber.getListeners();
        if (listeners.size() == 1) {
            unregister(listeners.iterator().next());
        } else {
            for (Listener<?> listener : listeners) {
                unregister(listener);
            }
        }
        subscribers.remove(subscriber);
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

        List<Listener> listeners = this.listeners.computeIfAbsent(listener.getTarget(), target -> new CopyOnWriteArrayList<>());
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
     * Dispatches an event to listeners.
     *
     * @param event the event to be dispatched
     * @return {@code true} if the given event is {@linkplain ICancelableEvent cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatch(Object event) {
        Objects.requireNonNull(event, "Cannot dispatch a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            taskExecutor.onEvent(event);

            forEachListener(
                    this.listeners.get(event.getClass()),
                    listener -> true,
                    listener -> listener.invoke(event),
                    false
            );

            if (event instanceof ICancelableEvent cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Dispatches an event to listeners.
     *
     * <p>The {@code type} parameter serves as a filtering mechanism for listeners, enabling you to selectively invoke
     * listeners based on their type, allowing for more targeted event handling.
     *
     * @param event the event to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain ICancelableEvent cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatch(Object event, Class<?> type) {
        Objects.requireNonNull(event, "Cannot dispatch a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            taskExecutor.onEvent(event);

            forEachListener(
                    this.listeners.get(event.getClass()),
                    listener -> (type == null) || (listener.getType() == null) || (listener.getType() == type),
                    listener -> listener.invoke(event),
                    false
            );

            if (event instanceof ICancelableEvent cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Dispatches the given event to the target listener class
     *
     * @param event the {@linkplain IClassTargetingEvent} to be dispatched
     * @return {@code true} if the given event was {@linkplain ICancelableEvent cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatchTargeted(IClassTargetingEvent event) {
        Objects.requireNonNull(event, "Cannot dispatch a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            taskExecutor.onEvent(event);

            forEachListener(
                    this.listeners.get(event.getClass()),
                    listener -> listener.getClass().equals(event.getTargetClass()),
                    listener -> listener.invoke(event),
                    false
            );

            if (event instanceof ICancelableEvent cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Dispatches the given {@link IClassTargetingEvent} to the target listener class.
     *
     * <p>The {@code type} parameter serves as a filtering mechanism for listeners, enabling you to selectively invoke
     * listeners based on their type, allowing for more targeted event handling.
     *
     * @param event the {@linkplain IClassTargetingEvent} to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain ICancelableEvent cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatchTargeted(IClassTargetingEvent event, Class<?> type) {
        Objects.requireNonNull(event, "Cannot dispatch a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            taskExecutor.onEvent(event);

            forEachListener(
                    this.listeners.get(event.getClass()),
                    listener -> ((type == null) || (listener.getType() == null) || (listener.getType() == type))
                            && listener.getClass().equals(event.getTargetClass()),
                    listener -> listener.invoke(event),
                    false
            );

            if (event instanceof ICancelableEvent cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Dispatches an event to listeners in order of inverse-priority. E.g. the lowest priority listeners will be
     * invoked before the highest priority listeners.
     *
     * @param event the event to be dispatched
     * @return {@code true} if the given event is {@linkplain ICancelableEvent cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatchInverted(Object event) {
        Objects.requireNonNull(event, "Cannot dispatch a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            taskExecutor.onEvent(event);

            forEachListener(
                    this.listeners.get(event.getClass()),
                    listener -> true,
                    listener -> listener.invoke(event),
                    true
            );

            if (event instanceof ICancelableEvent cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Dispatches an event to listeners in order of inverse-priority. E.g. the lowest priority listeners will be
     * invoked before the highest priority listeners.
     *
     * <p>The {@code type} parameter serves as a filtering mechanism for listeners, enabling you to selectively invoke
     * listeners based on their type, allowing for more targeted event handling.
     *
     * @param event the event to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain ICancelableEvent cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatchInverted(Object event, Class<?> type) {
        Objects.requireNonNull(event, "Cannot dispatch a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            taskExecutor.onEvent(event);

            forEachListener(
                    this.listeners.get(event.getClass()),
                    listener -> (type == null) || (listener.getType() == null) || (listener.getType() == type),
                    listener -> listener.invoke(event),
                    true
            );

            if (event instanceof ICancelableEvent cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Applies a given action to each {@linkplain Listener} in a list that matches a specified predicate.
     * Listeners are processed either in normal order or in reverse order based on the
     * {@code invertPriority} flag.
     * Listeners that should not persist are removed from the list after the action is applied.
     *
     * @param listeners      the list of listeners to be processed
     * @param predicate      the condition that each listener must satisfy to have the action applied
     * @param action         the action to be performed on each listener that satisfies the predicate
     * @param invertPriority if {@code true}, listeners are processed in reverse order; otherwise,
     *                       they are processed in normal order
     * @since 3.1.0
     */
    private void forEachListener(List<Listener> listeners, Predicate<Listener> predicate, Consumer<Listener> action, boolean invertPriority) {
        if (listeners != null && !listeners.isEmpty()) {
            if (invertPriority) {
                ListIterator<Listener> iterator = listeners.listIterator(listeners.size());
                while (iterator.hasPrevious()) {
                    Listener listener = iterator.previous();
                    if (!predicate.test(listener)) {
                        continue;
                    }
                    action.accept(listener);
                    if (!listener.shouldPersist()) {
                        listeners.remove(listener);
                    }
                }
            } else {
                Iterator<Listener> iterator = listeners.listIterator(0);
                while (iterator.hasNext()) {
                    Listener listener = iterator.next();
                    if (!predicate.test(listener)) {
                        continue;
                    }
                    action.accept(listener);
                    if (!listener.shouldPersist()) {
                        listeners.remove(listener);
                    }
                }
            }
        }
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
     * @author 7orivorian
     * @since 3.1.0
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if this event bus is equal to another object.
     * <p>If the given object is an event bus, it is only considered equal if {@code this.id == that.id}.
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