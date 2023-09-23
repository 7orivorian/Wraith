package me.tori.wraith.listener;

import me.tori.wraith.bus.IEventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An abstract base class for event listeners that implements the {@link Listener} interface.
 * Event listeners provide event handling logic with specified priorities, target classes, and types.
 *
 * @param <T> The type of event this listener is designed to handle.
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see Listener
 * @see Invokable
 * @since <b>1.0.0</b>
 */
public abstract class EventListener<T> implements Listener<T> {

    protected final int priority;
    protected final @Nullable Class<?> type;
    protected final @NotNull Class<? super T> target;

    /**
     * Constructs an event listener with default priority and no specified type.
     *
     * @param target The target class that this listener is designed to handle events for.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public EventListener(@NotNull Class<? super T> target) {
        this(target, IEventBus.DEFAULT_PRIORITY, null);
    }

    /**
     * Constructs an event listener with a specified priority and no specified type.
     *
     * @param target   The target class that this listener is designed to handle events for.
     * @param priority The priority level of this listener for event handling.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public EventListener(@NotNull Class<? super T> target, int priority) {
        this(target, priority, null);
    }

    /**
     * Constructs an event listener with default priority and a specified type.
     *
     * @param target The target class that this listener is designed to handle events for.
     * @param type   The type of events that this listener can handle.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public EventListener(@NotNull Class<? super T> target, @Nullable Class<?> type) {
        this(target, IEventBus.DEFAULT_PRIORITY, type);
    }

    /**
     * Constructs an event listener with a specified priority and type.
     *
     * @param target   The target class that this listener is designed to handle events for.
     * @param priority The priority level of this listener for event handling.
     * @param type     The type of events that this listener can handle.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public EventListener(@NotNull Class<? super T> target, int priority, @Nullable Class<?> type) {
        Objects.requireNonNull(target);
        this.priority = priority;
        this.target = target;
        this.type = type;
    }

    /**
     * Gets the priority level of this listener for event handling.
     *
     * @return The priority level of this listener.
     */
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * Gets the type of events that this listener can handle.
     *
     * @return The type of events that this listener can handle, or {@code null} if no type is specified.
     */
    @Nullable
    @Override
    public Class<?> getType() {
        return type;
    }

    /**
     * Gets the target class that this listener is designed to handle events for.
     *
     * @return The target class that this listener is designed to handle events for.
     */
    @NotNull
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