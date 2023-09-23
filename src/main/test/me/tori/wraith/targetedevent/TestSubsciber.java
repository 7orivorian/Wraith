package me.tori.wraith.targetedevent;

import me.tori.wraith.subscriber.Subscriber;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 */
public class TestSubsciber extends Subscriber {

    public TestSubsciber() {
        registerListeners(
                new ValidListener(),
                new InvalidListener()
        );
    }
}