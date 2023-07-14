package me.tori.wraith.bus;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public interface InvertableEventBus extends IEventBus {

    boolean postInverted(Object event);

    boolean postInverted(Object event, Class<?> type);
}