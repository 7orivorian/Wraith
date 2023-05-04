package me.tori.wraith.listener;

import me.tori.wraith.bus.IEventBus;

import java.util.Objects;

/**
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
public abstract class EventListener<T> implements IListener<T> {

    protected final int priority;
    protected final Class<?> type;
    protected final Class<? super T> target;

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
        Objects.requireNonNull(target);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        EventListener<?> that = (EventListener<?>) o;
        if (priority != that.priority) {
            return false;
        }
        if (!Objects.equals(type, that.type)) {
            return false;
        }
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        int result = priority;
        result = (31 * result) + ((type != null) ? type.hashCode() : 0);
        result = (31 * result) + target.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EventListener{" +
                "priority=" + priority +
                ", type=" + type +
                ", target=" + target +
                '}';
    }
}