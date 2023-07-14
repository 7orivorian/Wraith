package me.tori.wraith.listener;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public interface Invokable<T> {

    void invoke(T event);
}