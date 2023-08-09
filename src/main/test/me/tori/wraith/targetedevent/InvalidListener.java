package me.tori.wraith.targetedevent;

import me.tori.wraith.listener.EventListener;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 */
public class InvalidListener extends EventListener<TestEvent> {

    public InvalidListener() {
        super(TestEvent.class);
    }

    @Override
    public void invoke(TestEvent event) {
        event.message = "mwahahahaa >:/";
        event.cancel();
    }
}