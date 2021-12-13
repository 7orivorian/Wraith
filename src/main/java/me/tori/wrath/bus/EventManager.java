package main.java.me.tori.wrath.bus;

import main.java.me.tori.wrath.listeners.IListener;
import main.java.me.tori.wrath.listeners.Subscriber;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <b>7orivorian</b>
 * @version <b>Wrath v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public class EventManager implements IEventBus {

    private static final ConcurrentHashMap<Class<?>, List<IListener>> LISTENERS = new ConcurrentHashMap<>();

    private final Set<Subscriber> subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private boolean shutdown = false;

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
        if (isShutdown()) {
            return false;
        }
        List<IListener> listeners = LISTENERS.get(event.getClass());
        if (listeners != null) {
            listeners.forEach(listener -> listener.invoke(event));
        }
        return false;
    }

    @Override
    public boolean post(Object event, Class<?> type) {
        if (isShutdown()) {
            return false;
        }
        List<IListener> listeners = LISTENERS.get(event.getClass());
        if (listeners != null) {
            listeners.stream()
                    .filter(listener -> (listener.getType() == null) || (listener.getType() == type))
                    .forEach(listener -> listener.invoke(event));
        }
        return false;
    }

    @Override
    public boolean postInverted(Object event) {
        if (isShutdown()) {
            return false;
        }
        List<IListener> listeners = LISTENERS.get(event.getClass());
        if (listeners != null) {
            ListIterator<IListener> iterator = listeners.listIterator(listeners.size());
            while (iterator.hasPrevious()) {
                iterator.previous().invoke(event);
            }
        }
        return false;
    }

    @Override
    public boolean postInverted(Object event, Class<?> type) {
        if (isShutdown()) {
            return false;
        }
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
        return false;
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }
}