package me.tori.example.expanded;

import me.tori.wraith.event.cancelable.Cancelable;
import me.tori.wraith.event.staged.EventStage;
import me.tori.wraith.event.staged.Staged;

/**
 * Example event.
 * <p>Last updated for version <b>3.0.0</b>
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 */
class ExampleEvent extends Cancelable implements Staged {

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