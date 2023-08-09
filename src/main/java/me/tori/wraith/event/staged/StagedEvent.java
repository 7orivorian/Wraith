package me.tori.wraith.event.staged;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a staged event implementation that specifies the stage at which the event occurs.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see IStagedEvent
 * @see EventStage
 * @since <b>1.0.0</b>
 */
@SuppressWarnings("ClassCanBeRecord")
public class StagedEvent implements IStagedEvent {

    private final @NotNull EventStage stage;

    /**
     * Constructs a {@code StagedEvent} with the specified stage.
     *
     * @param stage The {@link EventStage} representing the stage of the event.
     * @throws NullPointerException If the provided {@code stage} is {@code null}.
     */
    public StagedEvent(@NotNull EventStage stage) {
        Objects.requireNonNull(stage);
        this.stage = stage;
    }

    /**
     * Gets the stage of this staged event.
     *
     * @return The {@link EventStage} representing the stage of the event.
     */
    @NotNull
    @Override
    public EventStage getStage() {
        return stage;
    }

    @Override
    public String toString() {
        return "StagedEvent{" +
                "stage=" + stage +
                '}';
    }
}