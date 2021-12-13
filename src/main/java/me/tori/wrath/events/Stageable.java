package main.java.me.tori.wrath.events;

import main.java.me.tori.wrath.listeners.IListenable;

/**
 * @author <b>7orivorian</b>
 * @version <b>Wrath v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public class Stageable implements IListenable {

    private final EventStage stage;

    public Stageable(EventStage stage) {
        this.stage = stage;
    }

    public EventStage getStage() {
        return stage;
    }
}