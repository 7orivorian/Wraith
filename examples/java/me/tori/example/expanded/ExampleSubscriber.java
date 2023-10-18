package me.tori.example.expanded;

import me.tori.wraith.event.staged.EventStage;
import me.tori.wraith.listener.LambdaEventListener;
import me.tori.wraith.subscriber.Subscriber;

/**
 * Example subscriber.
 * <p>Last updated for version <b>3.1.0</b>
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
class ExampleSubscriber extends Subscriber {

    public ExampleSubscriber() {
        registerListener(
                new LambdaEventListener<>(ExampleEvent.class, event -> {
                    if (event.getStage() == EventStage.PRE) {
                        event.setMessage("Hello world!");
                    }
                })
        );
        registerListener(new ExampleListener());
    }
}