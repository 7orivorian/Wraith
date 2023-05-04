package me.tori.wraith.event;

/**
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
public interface Invokable<T> {

    void invoke(T event);
}