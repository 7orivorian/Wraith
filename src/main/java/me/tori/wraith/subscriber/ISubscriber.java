package me.tori.wraith.subscriber;

import me.tori.wraith.listener.Listener;

import java.util.Collection;

/**
 * Represents a subscriber that can register and manage event listeners.
 * <p>An instance of a class implementing this interface can subscribe to an event bus by registering listeners.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see Subscriber
 * @since <b>1.0.0</b>
 */
public interface ISubscriber {

    /**
     * Registers a single event listener with this subscriber.
     *
     * @param listener The event listener to register.
     * @param <T>      The type of the listener.
     * @return The registered listener.
     */
    <T extends Listener<?>> T registerListener(T listener);

    /**
     * Registers multiple event listeners with this subscriber.
     *
     * @param listeners The event listeners to register.
     * @param <T>       The type of the listeners.
     * @return An array containing the registered listeners.
     */
    @SuppressWarnings("unchecked")
    <T extends Listener<?>> T[] registerListeners(T... listeners);

    /**
     * Retrieves a collection of event listeners registered with this subscriber.
     *
     * @return A collection of registered event listeners.
     */
    Collection<Listener<?>> getListeners();
}