package me.tori.wraith.events;

import me.tori.wraith.listeners.IListenable;

/**
 * @author <b>7orivorian</b>
 * @version <b>WraithAPI v1.0.0</b>
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