package me.tori.wraith.targetedevent;

import me.tori.wraith.event.cancelable.CancelableEvent;
import me.tori.wraith.event.targeted.IClassTargetingEvent;
import me.tori.wraith.listener.Listener;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 */
public class TestEvent extends CancelableEvent implements IClassTargetingEvent {

    public String message;
    private final Class<? extends Listener<?>> targetClass;

    public TestEvent(String message, Class<? extends Listener<?>> target) {
        this.message = message;
        this.targetClass = target;
    }

    @Override
    public Class<? extends Listener<?>> getTargetClass() {
        return targetClass;
    }
}