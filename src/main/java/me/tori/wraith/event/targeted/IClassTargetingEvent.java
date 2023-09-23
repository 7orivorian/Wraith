package me.tori.wraith.event.targeted;

import me.tori.wraith.listener.Listener;

/**
 * An interface representing an event that targets a specific class of listeners.
 * The class targeted by this event is determined by calling the {@link #getTargetClass()} method.
 * This interface is used to indicate that an event is designed to be directed at a specific listener class.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see ClassTargetingEvent
 * @since <b>3.0.0</b>
 */
public interface IClassTargetingEvent {

    /**
     * Retrieves the target class of listeners for this event.
     *
     * @return The class that represents the type of listeners targeted by this event.
     */
    Class<? extends Listener<?>> getTargetClass();
}