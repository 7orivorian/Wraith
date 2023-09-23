package me.tori.wraith.bus;

import me.tori.wraith.event.cancelable.ICancelableEvent;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public interface InvertableEventBus extends IEventBus {

    /**
     * @param event the event to be dispatched
     * @return {@code true} if the given event is {@linkplain ICancelableEvent cancelable} and canceled, {@code false} otherwise
     * @see EventBus#dispatchInverted(Object)
     */
    boolean dispatchInverted(Object event);

    /**
     * @param event the event to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain ICancelableEvent cancelable} and canceled, {@code false} otherwise
     * @see EventBus#dispatchInverted(Object, Class)
     */
    boolean dispatchInverted(Object event, Class<?> type);
}