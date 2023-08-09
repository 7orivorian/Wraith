package me.tori.wraith.bus;

import me.tori.wraith.event.cancelable.ICancelable;
import me.tori.wraith.listener.EventListener;
import me.tori.wraith.listener.Listener;
import me.tori.wraith.subscriber.ISubscriber;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public interface IEventBus {

    /**
     * Default priority that used when no other priority is specified
     *
     * @see EventListener#EventListener(Class)
     * @see EventListener#EventListener(Class, Class)
     */
    int DEFAULT_PRIORITY = 0;

    /**
     * @param subscriber the {@link ISubscriber} to be subscribed
     * @see #register(Listener)
     */
    void subscribe(ISubscriber subscriber);

    /**
     * @param subscriber the {@link ISubscriber} to be unsubscribed
     * @see #unregister(Listener)
     */
    void unsubscribe(ISubscriber subscriber);

    /**
     * @param listener the {@link Listener} to be registered
     */
    void register(Listener<?> listener);

    /**
     * @param listener the {@link Listener} to be unregistered
     */
    void unregister(Listener<?> listener);

    /**
     * @param event the event to be dispatched
     * @return {@code true} if the given event is {@linkplain ICancelable cancelable} and canceled, {@code false} otherwise
     */
    boolean dispatch(Object event);

    /**
     * @param event the event to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain ICancelable cancelable} and canceled, {@code false} otherwise
     */
    boolean dispatch(Object event, Class<?> type);

    /**
     * Shuts down this event bus, preventing future events from being dispatched
     */
    void shutdown();

    /**
     * @return {@code true} if this event bus is shut down, {@code false} otherwise
     */
    boolean isShutdown();
}