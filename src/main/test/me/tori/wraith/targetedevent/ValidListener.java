package me.tori.wraith.targetedevent;

import me.tori.wraith.listener.EventListener;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 */
public class ValidListener extends EventListener<TestEvent> {

    public ValidListener() {
        super(TestEvent.class);
    }

    @Override
    public void invoke(TestEvent event) {
        event.message = "hello world :D";
    }
}