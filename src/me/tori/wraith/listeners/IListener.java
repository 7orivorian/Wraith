package me.tori.wraith.listeners;

/**
 * @author <b>7orivorian</b>
 * @version <b>Wrath v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public interface IListener<T> extends Invokable<T> {

    int getPriority();

    Class<?> getType();

    Class<? super T> getTarget();
}