package me.tori.wraith.listener;

import me.tori.wraith.event.Invokable;

/**
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
public interface IListener<T> extends Invokable<T> {

    int getPriority();

    Class<?> getType();

    Class<? super T> getTarget();
}