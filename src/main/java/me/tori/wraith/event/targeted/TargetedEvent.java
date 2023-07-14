package me.tori.wraith.event.targeted;

import me.tori.wraith.listener.Listener;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public interface TargetedEvent<T extends Listener<?>> {

    Class<T> getTargetClass();
}