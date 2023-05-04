package me.tori.example;

import me.tori.wraith.bus.EventBus;
import me.tori.wraith.bus.IEventBus;
import me.tori.wraith.event.EventStage;

/**
 * @author <b>7orivorian</b>
 * @version <b>WraithLib v1.3.0</b>
 * @since <b>May 04, 2023</b>
 */
public class Main {

    private static final IEventBus EVENT_BUS = new EventBus();

    private static final ExampleSubscriber EXAMPLE_SUBSCRIBER = new ExampleSubscriber();

    public static void main(String[] args) {
        EVENT_BUS.subscribe(EXAMPLE_SUBSCRIBER);

        ExampleEvent event1 = new ExampleEvent(EventStage.PRE);

        EVENT_BUS.post(event1);

        if (!event1.isCanceled()) {
            System.out.println(event1.getString());
        }

        ExampleEvent event2 = new ExampleEvent(EventStage.POST);

        EVENT_BUS.post(event2);

        if (!event2.isCanceled()) {
            System.out.println(event2.getString());
        }
    }
}