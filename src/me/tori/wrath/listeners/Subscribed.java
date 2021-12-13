package me.tori.wrath.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author <b>7orivorian</b>
 * @version <b>Wrath v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public class Subscribed implements Subscriber {

    private final List<IListener<?>> listeners = new ArrayList<>();

    @Override
    public boolean registerListener(IListener<?> listener) {
        return listeners.add(listener);
    }

    @Override
    public Collection<IListener<?>> getListeners() {
        return listeners;
    }
}