package me.tori.wrath.bus;

import me.tori.wrath.WrathAPI;
import me.tori.wrath.listeners.ICancelable;
import me.tori.wrath.listeners.IListener;
import me.tori.wrath.listeners.Subscriber;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of {@link IEventBus}
 *
 * @author <b>7orivorian</b>
 * @version <b>Wrath v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventManager implements IEventBus {

    private static int maxID = 0;

    private static final ConcurrentHashMap<Class<?>, List<IListener>> LISTENERS = new ConcurrentHashMap<>();
    private final Set<Subscriber> subscribers;

    private boolean shutdown = false;
    private final int busID;

    public EventManager() {
        this.busID = maxID++;
        this.subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        for (IListener<?> listener : subscriber.getListeners()) {
            register(listener);
        }
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        for (IListener<?> listener : subscriber.getListeners()) {
            unregister(listener);
        }
        subscribers.remove(subscriber);
    }

    @Override
    public void register(IListener<?> listener) {
        List<IListener> listeners = LISTENERS.computeIfAbsent(listener.getTarget(), target -> new CopyOnWriteArrayList<>());

        int index = 0;
        for (; index < listeners.size(); index++) {
            if (listener.getPriority() > listeners.get(index).getPriority()) {
                break;
            }
        }
        listeners.add(index, listener);
    }

    @Override
    public void unregister(IListener<?> listener) {
        LISTENERS.get(listener.getTarget()).removeIf(l -> l.equals(listener));
    }

    @Override
    public boolean post(Object event) {
        Objects.requireNonNull(event, "Cannot post a null event");
        if (isShutdown()) {
            return false;
        } else {
            List<IListener> listeners = LISTENERS.get(event.getClass());
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
        Objects.requireNonNull(event, "Cannot post a null event");
        if (isShutdown()) {
            return false;
        } else {
            List<IListener> listeners = LISTENERS.get(event.getClass());
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
        Objects.requireNonNull(event, "Cannot post a null event");
        if (isShutdown()) {
            return false;
        } else {
            List<IListener> listeners = LISTENERS.get(event.getClass());
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
        Objects.requireNonNull(event, "Cannot post a null event");
        if (isShutdown()) {
            return false;
        } else {
            List<IListener> listeners = LISTENERS.get(event.getClass());
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
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void shutdown() {
        WrathAPI.LOGGER.warning("EventBus " + this.busID + " shutting down! Future events will not be posted.");
        shutdown = true;
    }
}