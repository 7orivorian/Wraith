package me.tori.wraith.bus;

import me.tori.wraith.listener.Listener;
import me.tori.wraith.subscriber.ISubscriber;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public interface IEventBus {

    int DEFAULT_PRIORITY = 0;

    void subscribe(ISubscriber subscriber);

    void unsubscribe(ISubscriber subscriber);

    void register(Listener<?> listener);

    void unregister(Listener<?> listener);

    boolean post(Object event);

    boolean post(Object event, Class<?> type);

    void shutdown();

    /**
     * @return {@code true} if this event bus is shut down, {@code false} otherwise
     */
    boolean isShutdown();
}