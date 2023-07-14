package me.tori.wraith.bus;

import me.tori.wraith.WraithLogger;
import me.tori.wraith.event.cancelable.ICancelable;
import me.tori.wraith.event.targeted.TargetedEvent;
import me.tori.wraith.listener.Listener;
import me.tori.wraith.subscriber.ISubscriber;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Default implementation of {@link IEventBus}, {@link TargetableEventBus}, and {@link InvertableEventBus}
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
     * <p>Shutdown event dispatchers cannot dispatch events, and throw {@link UnsupportedOperationException} when
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
     * Creates a new event bus
     */
    public EventBus() {
        this.id = instances++;
        this.shutdown = false;
        this.subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.listeners = new ConcurrentHashMap<>();
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
        for (Listener<?> listener : subscriber.getListeners()) {
            register(listener);
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
        for (Listener<?> listener : subscriber.getListeners()) {
            unregister(listener);
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
     * @param event the event to be posted
     * @return {@code true} if the given event is {@linkplain ICancelable cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean post(Object event) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            List<Listener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                listeners.forEach(listener -> listener.invoke(event));
            }
            if (event instanceof ICancelable cancelable) {
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
     * @param event the event to be posted
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain ICancelable cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean post(Object event, Class<?> type) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            List<Listener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                listeners.stream()
                        .filter(listener -> (listener.getType() == null) || (listener.getType() == type))
                        .forEach(listener -> listener.invoke(event));
            }
            if (event instanceof ICancelable cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Dispatches the given event to the target listener class
     *
     * @param event the {@linkplain TargetedEvent} to dispatch
     * @return {@code true} if the given event was {@linkplain ICancelable cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatchTargeted(TargetedEvent<? extends Listener<?>> event) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            List<Listener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                listeners.stream()
                        .filter(listener -> listener.getClass().equals(event.getTargetClass()))
                        .forEach(listener -> listener.invoke(event));
            }
            if (event instanceof ICancelable cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Dispatches the given {@link TargetedEvent} to the target listener class.
     *
     * <p>The {@code type} parameter serves as a filtering mechanism for listeners, enabling you to selectively invoke
     * listeners based on their type, allowing for more targeted event handling.
     *
     * @param event the {@linkplain TargetedEvent} to be posted
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain ICancelable cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatchTargeted(TargetedEvent<? extends Listener<?>> event, Class<?> type) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            List<Listener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                listeners.stream()
                        .filter(listener -> (listener.getType() == null) || (listener.getType() == type))
                        .filter(listener -> listener.getClass().equals(event.getTargetClass()))
                        .forEach(listener -> listener.invoke(event));
            }
            if (event instanceof ICancelable cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Dispatches an event to listeners in order of inverse-priority. E.g. the lowest priority listeners will be
     * invoked before the highest priority listeners.
     *
     * @param event the event to be posted
     * @return {@code true} if the given event is {@linkplain ICancelable cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean postInverted(Object event) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            List<Listener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                ListIterator<Listener> iterator = listeners.listIterator(listeners.size());
                while (iterator.hasPrevious()) {
                    iterator.previous().invoke(event);
                }
            }
            if (event instanceof ICancelable cancelable) {
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
     * @param event the event to be posted
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain ICancelable cancelable} and canceled, {@code false} otherwise
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean postInverted(Object event, Class<?> type) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus " + id + "!");
        if (isShutdown()) {
            throw new UnsupportedOperationException("Dispatcher " + id + " is shutdown!");
        } else {
            List<Listener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                ListIterator<Listener> iterator = listeners.listIterator(listeners.size());
                while (iterator.hasPrevious()) {
                    Listener listener = iterator.previous();
                    if ((listener.getType() == null) || (listener.getType() == type)) {
                        listener.invoke(event);
                    }
                }
            }
            if (event instanceof ICancelable cancelable) {
                return cancelable.isCanceled();
            }
        }
        return false;
    }

    /**
     * Logs a warning message and shuts down this event bus
     *
     * @see #shutdown
     */
    @Override
    public void shutdown() {
        WraithLogger.LOGGER.warning("EventBus " + id + " shutting down! Future events will not be posted.");
        shutdown = true;
    }

    /**
     * @return {@code true} if this event bus is shut down
     * @see #shutdown
     */
    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * Checks if this event bus is equal to another object.
     * <p>If the given object is an event bus, it is only considered equal if {@code this.id == that.id}
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