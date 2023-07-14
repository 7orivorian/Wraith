package me.tori.wraith.bus;

import me.tori.wraith.event.targeted.TargetedEvent;
import me.tori.wraith.event.cancelable.ICancelable;
import me.tori.wraith.listener.Listener;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public interface TargetableEventBus extends IEventBus {

    /**
     * @param event the {@linkplain TargetedEvent} to dispatch
     * @return {@code true} if the given event was {@linkplain ICancelable cancelable} and canceled, {@code false otherwise}
     * @see EventBus#dispatchTargeted(TargetedEvent)
     */
    boolean dispatchTargeted(TargetedEvent<? extends Listener<?>> event);

    /**
     * @param event the {@linkplain TargetedEvent} to dispatch
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event was {@linkplain ICancelable cancelable} and canceled, {@code false otherwise}
     * @see EventBus#dispatchTargeted(TargetedEvent, Class)
     */
    boolean dispatchTargeted(TargetedEvent<? extends Listener<?>> event, Class<?> type);
}