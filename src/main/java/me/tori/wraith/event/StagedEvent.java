package me.tori.wraith.event;

import java.util.Objects;

/**
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
@SuppressWarnings("ClassCanBeRecord")
public class StagedEvent {

    private final EventStage stage;

    public StagedEvent(EventStage stage) {
        Objects.requireNonNull(stage);
        this.stage = stage;
    }

    public EventStage stage() {
        return stage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        StagedEvent that = (StagedEvent) o;
        return stage == that.stage;
    }

    @Override
    public int hashCode() {
        return stage.hashCode();
    }

    @Override
    public String toString() {
        return "StagedEvent{" +
                "stage=" + stage +
                '}';
    }
}