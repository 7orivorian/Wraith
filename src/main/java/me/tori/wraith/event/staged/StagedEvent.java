package me.tori.wraith.event.staged;

import java.util.Objects;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
@SuppressWarnings("ClassCanBeRecord")
public class StagedEvent implements Staged {

    private final EventStage stage;

    public StagedEvent(EventStage stage) {
        Objects.requireNonNull(stage);
        this.stage = stage;
    }

    @Override
    public EventStage stage() {
        return stage;
    }

    @Override
    public String toString() {
        return "StagedEvent{" +
                "stage=" + stage +
                '}';
    }
}