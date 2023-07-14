package me.tori.wraith.listener;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public interface Listener<T> extends Invokable<T> {

    int getPriority();

    Class<?> getType();

    Class<? super T> getTarget();
}