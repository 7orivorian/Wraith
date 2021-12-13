package me.tori.wrath.listeners;

import me.tori.wrath.bus.IEventBus;

/**
 * @author <b>7orivorian</b>
 * @version <b>Wrath v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public abstract class EventListener<T> implements IListener<T> {

    private final int priority;
    private final Class<?> type;
    private final Class<? super T> target;

    public EventListener(Class<? super T> target) {
        this(target, IEventBus.DEFAULT_PRIORITY, null);
    }

    public EventListener(Class<? super T> target, int priority) {
        this(target, priority, null);
    }

    public EventListener(Class<? super T> target, Class<?> type) {
        this(target, IEventBus.DEFAULT_PRIORITY, type);
    }

    public EventListener(Class<? super T> target, int priority, Class<?> type) {
        this.priority = priority;
        this.target = target;
        this.type = type;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public Class<? super T> getTarget() {
        return target;
    }
}