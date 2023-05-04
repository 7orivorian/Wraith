package me.tori.example;

import me.tori.wraith.event.EventStage;
import me.tori.wraith.listener.EventListener;

/**
 * @author <b>7orivorian</b>
 * @version <b>WraithLib v1.3.0</b>
 * @since <b>May 04, 2023</b>
 */
public class ExampleListener extends EventListener<ExampleEvent> {

    public ExampleListener() {
        super(ExampleEvent.class);
    }

    @Override
    public void invoke(ExampleEvent event) {
        if (event.getStage() == EventStage.POST) {
            event.setString("I feel wonderful!");
        }
    }
}