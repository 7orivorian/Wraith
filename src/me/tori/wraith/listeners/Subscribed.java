package me.tori.wraith.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author <b>7orivorian</b>
 * @version <b>WraithAPI v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public class Subscribed implements Subscriber {

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
}