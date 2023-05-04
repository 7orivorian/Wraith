package me.tori.wraith.subscriber;

import me.tori.wraith.listener.IListener;

import java.util.Collection;

/**
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
public interface ISubscriber {

    <T extends IListener<?>> T registerListener(T listener);

    <T extends IListener<?>> T[] registerListeners(T... listeners);

    Collection<IListener<?>> getListeners();
}