package dev.tori.wraith.eventbus.annotation;

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.Target.TargetingRule;
import dev.tori.wraith.listener.annotation.Listener;
import dev.tori.wraith.subscriber.AnnotatedSubscriber;
import org.junit.jupiter.api.Test;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 4.1.0
 */
public class AnnotationTest {

    public static class DummyClass {

        public DummyClass(EventBus eventBus) {
            eventBus.subscribe(new AnnotatedSubscriber(this));
        }

        @Listener
        private void otherListener(MyEvent event) {
            System.out.println(event.message());
        }

        @Listener(rule = TargetingRule.CASCADE)
        public void question(MyEvent event) {
            System.out.println(event.message());
        }

        @Listener(rule = TargetingRule.REVERSE_CASCADE)
        public void listen(MyEvent event) {
            System.out.println(event.message());
        }

    }

    public static class MyEvent {

        private final String message;

        public MyEvent(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }

        public static class MyExtendedEvent extends MyEvent {

            public MyExtendedEvent(String message) {
                super(message);
            }
        }
    }


    /* Tests */

    @Test
    public void testTarget() {
        // TODO
    }


}