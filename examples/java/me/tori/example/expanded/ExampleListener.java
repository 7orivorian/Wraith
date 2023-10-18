package me.tori.example.expanded;

import me.tori.wraith.event.staged.EventStage;
import me.tori.wraith.listener.EventListener;

/**
 * Example listener.
 * <p>Last updated for version <b>3.1.0</b>
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
class ExampleListener extends EventListener<ExampleEvent> {

    public ExampleListener() {
        super(ExampleEvent.class);
    }

    @Override
    public void invoke(ExampleEvent event) {
        if (event.getStage() == EventStage.POST) {
            event.setMessage("I feel wonderful!");
        }
    }
}