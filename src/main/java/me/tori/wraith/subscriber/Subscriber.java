package me.tori.wraith.subscriber;

import me.tori.wraith.listener.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Default implementation of {@link ISubscriber}
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public class Subscriber implements ISubscriber {

    private final List<Listener<?>> listeners = new ArrayList<>();

    @Override
    public <T extends Listener<?>> T registerListener(T listener) {
        listeners.add(listener);
        return listener;
    }

    @SafeVarargs
    @Override
    public final <T extends Listener<?>> T[] registerListeners(T... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
        return listeners;
    }

    @Override
    public Collection<Listener<?>> getListeners() {
        return listeners;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "listeners=" + listeners +
                '}';
    }
}