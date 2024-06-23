package me.tori.wraith.listener;

import me.tori.wraith.bus.EventBus;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An interface representing an event listener with priority, type, and target class information.
 *
 * @param <T> The type of event this listener is designed to handle.
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see EventListener
 * @see Invokable
 * @since <b>1.0.0</b>
 */
public interface Listener<T> extends Invokable<T> {

    /**
     * Gets the priority level of this listener for event handling.
     *
     * @return The priority level of this listener.
     */
    int getPriority();

    /**
     * Gets the type of events that this listener can handle.
     *
     * @return The type of events that this listener can handle, or {@code null} if no type is specified.
     */
    Class<?> getType();

    /**
     * Gets the target class that this listener is designed to handle events for.
     *
     * @return The target class that this listener is designed to handle events for.
     */
    Class<? super T> getTarget();

    /**
     * Determines whether this listener should persist after being invoked.
     *
     * @return {@code true} if the listener should persist, {@code false} otherwise
     * @see EventBus#forEachListener(List, Predicate, Consumer, boolean)
     * @since 3.2.0
     */
    @SuppressWarnings("JavadocReference")
    default boolean shouldPersist() {
        return isPersistent();
    }

    /**
     * Indicates whether this listener is inherently persistent.
     *
     * @return {@code true} if the listener is inherently persistent, {@code false} otherwise
     * @since 3.2.0
     */
    default boolean isPersistent() {
        return true;
    }
}