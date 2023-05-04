package me.tori.wraith.subscriber;

import me.tori.wraith.listener.IListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Default implementation of {@link ISubscriber}
 *
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
public class Subscriber implements ISubscriber {

    private final List<IListener<?>> listeners = new ArrayList<>();

    @Override
    public <T extends IListener<?>> T registerListener(T listener) {
        listeners.add(listener);
        return listener;
    }

    @SafeVarargs
    @Override
    public final <T extends IListener<?>> T[] registerListeners(T... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
        return listeners;
    }

    @Override
    public Collection<IListener<?>> getListeners() {
        return listeners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Subscriber that = (Subscriber) o;
        return listeners.equals(that.listeners);
    }

    @Override
    public int hashCode() {
        return listeners.hashCode();
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "listeners=" + listeners +
                '}';
    }
}