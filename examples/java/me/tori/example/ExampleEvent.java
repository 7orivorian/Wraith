package me.tori.example;

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

    private String string;
    private final EventStage stage;

    public ExampleEvent(EventStage stage) {
        this.stage = stage;
        this.string = null;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public EventStage stage() {
        return stage;
    }
}