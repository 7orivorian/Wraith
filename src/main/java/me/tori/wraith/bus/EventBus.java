package me.tori.wraith.bus;

import me.tori.wraith.WraithLogger;
import me.tori.wraith.event.ICancelable;
import me.tori.wraith.listener.IListener;
import me.tori.wraith.subscriber.ISubscriber;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Default implementation of {@link IEventBus}
 *
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventBus implements IEventBus {

    private static int instances = 0;

    private final int busID;
    private boolean shutdown;
    private final Set<ISubscriber> subscribers;
    private final ConcurrentHashMap<Class<?>, List<IListener>> listeners;

    public EventBus() {
        this.busID = instances++;
        this.shutdown = false;
        this.subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.listeners = new ConcurrentHashMap<>();
    }

    @Override
    public void subscribe(ISubscriber subscriber) {
        Objects.requireNonNull(subscriber, "Cannot subscribe null to event bus %d!".formatted(busID));
        for (IListener<?> listener : subscriber.getListeners()) {
            register(listener);
        }
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(ISubscriber subscriber) {
        Objects.requireNonNull(subscriber, "Cannot unsubscribe null from event bus %d!".formatted(busID));
        for (IListener<?> listener : subscriber.getListeners()) {
            unregister(listener);
        }
        subscribers.remove(subscriber);
    }

    @Override
    public void register(IListener<?> listener) {
        Objects.requireNonNull(listener, "Cannot register null listener to event bus %d!".formatted(busID));

        List<IListener> listeners = this.listeners.computeIfAbsent(listener.getTarget(), target -> new CopyOnWriteArrayList<>());
        final int size = listeners.size();
        int index = 0;
        for (; index < size; index++) {
            if (listener.getPriority() > listeners.get(index).getPriority()) {
                break;
            }
        }
        listeners.add(index, listener);
    }

    @Override
    public void unregister(IListener<?> listener) {
        Objects.requireNonNull(listener, "Cannot unregister null listener from event bus %d!".formatted(busID));
        listeners.get(listener.getTarget()).removeIf(l -> l.equals(listener));
    }

    @Override
    public boolean post(Object event) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus %d!".formatted(busID));
        if (isShutdown()) {
            return false;
        } else {
            List<IListener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                listeners.forEach(listener -> listener.invoke(event));
            }
            if (event instanceof ICancelable) {
                return ((ICancelable) event).isCanceled();
            }
        }
        return false;
    }

    @Override
    public boolean post(Object event, Class<?> type) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus %d!".formatted(busID));
        if (isShutdown()) {
            return false;
        } else {
            List<IListener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                listeners.stream()
                        .filter(listener -> (listener.getType() == null) || (listener.getType() == type))
                        .forEach(listener -> listener.invoke(event));
            }
            if (event instanceof ICancelable) {
                return ((ICancelable) event).isCanceled();
            }
        }
        return false;
    }

    @Override
    public boolean postInverted(Object event) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus %d!".formatted(busID));
        if (isShutdown()) {
            return false;
        } else {
            List<IListener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                ListIterator<IListener> iterator = listeners.listIterator(listeners.size());
                while (iterator.hasPrevious()) {
                    iterator.previous().invoke(event);
                }
            }
            if (event instanceof ICancelable) {
                return ((ICancelable) event).isCanceled();
            }
        }
        return false;
    }

    @Override
    public boolean postInverted(Object event, Class<?> type) {
        Objects.requireNonNull(event, "Cannot post a null event to event bus %d!".formatted(busID));
        if (isShutdown()) {
            return false;
        } else {
            List<IListener> listeners = this.listeners.get(event.getClass());
            if (listeners != null) {
                ListIterator<IListener> iterator = listeners.listIterator(listeners.size());
                while (iterator.hasPrevious()) {
                    IListener listener = iterator.previous();
                    if ((listener.getType() == null) || (listener.getType() == type)) {
                        listener.invoke(event);
                    }
                }
            }
            if (event instanceof ICancelable) {
                return ((ICancelable) event).isCanceled();
            }
        }
        return false;
    }

    @Override
    public void shutdown() {
        WraithLogger.LOGGER.warning("EventBus %d shutting down! Future events will not be posted.".formatted(busID));
        shutdown = true;
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        EventBus that = (EventBus) o;
        return busID == that.busID;
    }

    @Override
    public int hashCode() {
        int result = busID;
        result = 31 * result + (shutdown ? 1 : 0);
        result = 31 * result + subscribers.hashCode();
        result = 31 * result + listeners.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EventBus{" +
                "busID=" + busID +
                ", shutdown=" + shutdown +
                ", subscribers=" + subscribers +
                ", listeners=" + listeners +
                '}';
    }
}