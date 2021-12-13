package main.java.me.tori.wrath.bus;

import main.java.me.tori.wrath.listeners.IListener;
import main.java.me.tori.wrath.listeners.Subscriber;

/**
 * @author <b>7orivorian</b>
 * @version <b>Wrath v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public interface IEventBus {

    int DEFAULT_PRIORITY = 0;

    void subscribe(Subscriber subscriber);

    void unsubscribe(Subscriber subscriber);

    void register(IListener<?> listener);

    void unregister(IListener<?> listener);

    boolean post(Object event);

    boolean post(Object event, Class<?> type);

    boolean postInverted(Object event);

    boolean postInverted(Object event, Class<?> type);

    boolean isShutdown();

    void shutdown();
}