package me.tori.wraith.event.staged;

/**
 * An interface representing a staged event, indicating the stage at which the event is occurring.
 *
 * <p>This interface is implemented by events that have distinct stages of processing. Events can be categorized
 * into different stages such as pre, peri, and post.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see EventStage
 * @see StagedEvent
 * @since <b>3.0.0</b>
 */
public interface Staged {

    /**
     * Gets the stage at which this event is occurring.
     *
     * @return The {@link EventStage} representing the stage of this event.
     */
    EventStage getStage();
}