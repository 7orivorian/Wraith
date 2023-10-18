package me.tori.example.expanded;

import me.tori.wraith.event.cancelable.CancelableEvent;
import me.tori.wraith.event.staged.EventStage;
import me.tori.wraith.event.staged.IStagedEvent;

/**
 * Example event.
 * <p>Last updated for version <b>3.1.0</b>
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 */
class ExampleEvent extends CancelableEvent implements IStagedEvent {

    private String message;
    private final EventStage stage;

    public ExampleEvent(EventStage stage) {
        this.stage = stage;
        this.message = "nothing to say";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public EventStage getStage() {
        return stage;
    }
}