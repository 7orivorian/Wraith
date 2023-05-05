package me.tori.example;

import me.tori.wraith.event.Cancelable;
import me.tori.wraith.event.EventStage;

/**
 * @author <b>7orivorian</b>
 * @since <b>May 04, 2023</b>
 */
public class ExampleEvent extends Cancelable {

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

    public EventStage getStage() {
        return stage;
    }
}