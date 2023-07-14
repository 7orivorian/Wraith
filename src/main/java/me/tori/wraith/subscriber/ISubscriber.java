package me.tori.wraith.subscriber;

import me.tori.wraith.listener.Listener;

import java.util.Collection;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public interface ISubscriber {

    <T extends Listener<?>> T registerListener(T listener);

    @SuppressWarnings("unchecked")
    <T extends Listener<?>> T[] registerListeners(T... listeners);

    Collection<Listener<?>> getListeners();
}