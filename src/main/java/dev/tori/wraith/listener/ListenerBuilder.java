/*
 * Copyright (c) 2021-2025 7orivorian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package dev.tori.wraith.listener;

import dev.tori.wraith.bus.IEventBus;
import dev.tori.wraith.event.Target;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A builder class for creating instances of {@link EventListener}.
 *
 * <p>This builder allows configuring various properties of an {@link EventListener},
 * including the target class, type, priority, persistence, and the invokable action.
 *
 * <p><b>Usage Example:</b>
 * <pre>
 * {@code
 * EventListener<MyEvent> listener = new ListenerBuilder<>()
 *     .target(ClassTarget.fine(MyEvent.class))
 *     .priority(5)
 *     .persists(10)
 *     .invokable(event -> handleEvent(event))
 *     .build();
 * }
 * </pre>
 *
 * @param <T> the type of event the built listener handles
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @see EventListener
 * @since 3.2.0
 */
public class ListenerBuilder<T> {

    private Target target = null;
    private int priority = IEventBus.DEFAULT_PRIORITY;
    private int persists = -1;
    private boolean persistent = true;
    private Invokable<T> invokable = null;

    /**
     * Sets the {@link Target} for this listener.
     *
     * @param target This listener's {@link Target}.
     * @return this {@code ListenerBuilder} instance.
     */
    public ListenerBuilder<T> target(@NotNull Target target) {
        this.target = target;
        return this;
    }

    /**
     * Sets the target class and targeting rule for this listener.
     *
     * @param clazz the target class to be used.
     * @param rule  the targeting rule to determine how the target class should be matched.
     * @return this {@code ListenerBuilder} instance.
     */
    public ListenerBuilder<T> target(@NotNull Class<?> clazz, @NotNull Target.TargetingRule rule) {
        return target(Target.of(clazz, rule));
    }

    /**
     * Sets the priority for this listener.
     *
     * @param priority the priority of the listener.
     * @return this {@code ListenerBuilder} instance.
     */
    public ListenerBuilder<T> priority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Sets the number of times this listener should persist.
     * <p>
     * Overrides {@link #persistent}.
     *
     * @param persists the number of events this listener should handle before being removed.
     * @return this {@code ListenerBuilder} instance.
     */
    public ListenerBuilder<T> persists(int persists) {
        this.persists = persists;
        this.persistent = persists <= 0;
        return this;
    }

    /**
     * Sets whether this listener is inherently persistent.
     * <p>
     * Overrides {@link #persists}.
     *
     * @param persistent {@code true} if the listener should be persistent, {@code false} otherwise.
     * @return this {@code ListenerBuilder} instance.
     */
    public ListenerBuilder<T> persistent(boolean persistent) {
        this.persistent = persistent;
        if (persistent) {
            this.persists = 0;
        }
        return this;
    }

    /**
     * Sets the invokable action for this listener.
     *
     * @param invokable the action to be invoked when an event is handled
     * @return this {@code ListenerBuilder} instance.
     */
    public ListenerBuilder<T> invokable(@NotNull Invokable<T> invokable) {
        this.invokable = invokable;
        return this;
    }

    /**
     * Builds and returns an {@link EventListener} with the configured properties.
     *
     * @return a new {@link EventListener} instance.
     * @throws NullPointerException     if the target or invokable is not set.
     * @throws IllegalArgumentException if there is a mismatch between persistence settings.
     */
    @NotNull
    public EventListener<T> build() {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(invokable, "invokable must not be null");

        if ((persistent && (persists > 0)) || (!persistent && (persists <= 0))) {
            throw new IllegalArgumentException(
                    "Persistency missmatch. persistent=" + persistent + " and persists=" + persists + " is not allowed."
            );
        }
        return new EventListener<>(target, priority, persists) {
            @Override
            public void invoke(T event) {
                invokable.invoke(event);
            }
        };
    }
}