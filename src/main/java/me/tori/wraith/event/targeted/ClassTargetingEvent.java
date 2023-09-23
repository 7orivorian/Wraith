package me.tori.wraith.event.targeted;

import me.tori.wraith.listener.Listener;

/**
 * A basic implementation of the {@link IClassTargetingEvent} interface.
 * This class provides a simple way to specify the target class of listeners for an event.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see IClassTargetingEvent
 * @since <b>3.0.0</b>
 */
@SuppressWarnings("ClassCanBeRecord")
public class ClassTargetingEvent implements IClassTargetingEvent {

    private final Class<? extends Listener<?>> targetClass;

    /**
     * Constructs a new {@link ClassTargetingEvent} with the specified target class of listeners.
     *
     * @param targetClass The class representing the type of listeners to be targeted by this event.
     */
    public ClassTargetingEvent(Class<? extends Listener<?>> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Retrieves the target class of listeners for this event.
     *
     * @return The class that represents the type of listeners targeted by this event.
     */
    @Override
    public Class<? extends Listener<?>> getTargetClass() {
        return targetClass;
    }
}