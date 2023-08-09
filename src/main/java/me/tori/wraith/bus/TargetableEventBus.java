package me.tori.wraith.bus;

import me.tori.wraith.event.cancelable.ICancelableEvent;
import me.tori.wraith.event.targeted.IClassTargetingEvent;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public interface TargetableEventBus extends IEventBus {

    /**
     * @param event the {@linkplain IClassTargetingEvent} to dispatch
     * @return {@code true} if the given event was {@linkplain ICancelableEvent cancelable} and canceled, {@code false otherwise}
     * @see EventBus#dispatchTargeted(IClassTargetingEvent)
     */
    boolean dispatchTargeted(IClassTargetingEvent event);

    /**
     * @param event the {@linkplain IClassTargetingEvent} to dispatch
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event was {@linkplain ICancelableEvent cancelable} and canceled, {@code false otherwise}
     * @see EventBus#dispatchTargeted(IClassTargetingEvent, Class)
     */
    boolean dispatchTargeted(IClassTargetingEvent event, Class<?> type);
}