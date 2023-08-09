package me.tori.wraith.listener;

/**
 * A functional interface representing a callable object that can handle or process an event.
 *
 * @param <T> The type of event to be handled or processed.
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see Listener
 * @see EventListener
 * @see LambdaEventListener
 * @since <b>1.0.0</b>
 */
@FunctionalInterface
public interface Invokable<T> {

    /**
     * Invokes the handling or processing logic for the specified event.
     *
     * @param event The event to be handled or processed.
     */
    void invoke(T event);
}