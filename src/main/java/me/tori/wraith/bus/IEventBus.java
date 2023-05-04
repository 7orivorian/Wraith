package me.tori.wraith.bus;

import me.tori.wraith.listener.IListener;
import me.tori.wraith.subscriber.ISubscriber;

/**
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
public interface IEventBus {

    int DEFAULT_PRIORITY = 0;

    void subscribe(ISubscriber subscriber);

    void unsubscribe(ISubscriber subscriber);

    void register(IListener<?> listener);

    void unregister(IListener<?> listener);

    boolean post(Object event);

    boolean post(Object event, Class<?> type);

    boolean postInverted(Object event);

    boolean postInverted(Object event, Class<?> type);

    void shutdown();

    boolean isShutdown();
}