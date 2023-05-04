package me.tori.wraith.listeners;

/**
 * @author <b>7orivorian</b>
 * @version <b>WraithLib v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public interface Invokable<T> {

    void invoke(T event);
}