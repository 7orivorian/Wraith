package me.tori.example;

import me.tori.wraith.event.EventStage;
import me.tori.wraith.listener.LambdaEventListener;
import me.tori.wraith.subscriber.Subscriber;

/**
 * @author <b>7orivorian</b>
 * @version <b>WraithLib v1.3.0</b>
 * @since <b>May 04, 2023</b>
 */
public class ExampleSubscriber extends Subscriber {

    public ExampleSubscriber() {
        registerListener(
                new LambdaEventListener<>(ExampleEvent.class, event -> {
                    if (event.getStage() == EventStage.PRE) {
                        event.setString("Hello world!");
                    }
                })
        );
        registerListener(new ExampleListener());
    }
}