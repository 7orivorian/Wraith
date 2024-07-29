/*
 * Copyright (c) 2021-2024 7orivorian.
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An abstract base class for event listeners that implements the {@link Listener} interface.
 * Event listeners provide event handling logic with specified priorities, target classes, and types.
 *
 * @param <T> The type of event this listener is designed to handle.
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @see Listener
 * @see Invokable
 * @since 1.0.0
 */
public abstract class EventListener<T> implements Listener<T> {

    protected final @NotNull Class<? super T> target;
    protected final @Nullable Class<?> type;
    protected final int priority;
    protected final boolean indefinitePersistence;
    protected int persists;

    /**
     * Constructs an event listener with default priority and no specified type.
     *
     * @param target The target class that this listener is designed to handle events for.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public EventListener(@NotNull Class<? super T> target) {
        this(target, null, IEventBus.DEFAULT_PRIORITY, DEFAULT_PERSISTENCE);
    }

    /**
     * Constructs an event listener with a specified priority and no specified type.
     *
     * @param target   The target class that this listener is designed to handle events for.
     * @param priority The priority level of this listener for event handling.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public EventListener(@NotNull Class<? super T> target, int priority) {
        this(target, null, priority, DEFAULT_PERSISTENCE);
    }

    /**
     * Constructs an event listener with default priority and a specified type.
     *
     * @param target The target class that this listener is designed to handle events for.
     * @param type   The type of events that this listener can handle. Can be {@code null}.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public EventListener(@NotNull Class<? super T> target, @Nullable Class<?> type) {
        this(target, type, IEventBus.DEFAULT_PRIORITY, DEFAULT_PERSISTENCE);
    }

    /**
     * Constructs an event listener with a specified priority and type.
     *
     * @param target   The target class that this listener is designed to handle events for.
     * @param priority The priority level of this listener for event handling.
     * @param type     The type of events that this listener can handle. Can be {@code null}.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public EventListener(@NotNull Class<? super T> target, @Nullable Class<?> type, int priority) {
        this(target, type, priority, DEFAULT_PERSISTENCE);
    }

    /**
     * Constructs an event listener with a specified priority and type.
     *
     * @param target   The target class that this listener is designed to handle events for.
     * @param type     The type of events that this listener can handle. Can be {@code null}.
     * @param priority The priority level of this listener for event handling.
     * @param persists How many events this listener should handle before being killed.
     *                 A value {@code <= 0} will flag this listener to {@linkplain #indefinitePersistence persist indefinitely}.
     * @throws NullPointerException if {@code target} is {@code null}.
     * @since 3.2.0
     */
    public EventListener(@NotNull Class<? super T> target, @Nullable Class<?> type, int priority, int persists) {
        Objects.requireNonNull(target);
        this.target = target;
        this.type = type;
        this.priority = priority;
        this.persists = persists;
        this.indefinitePersistence = persists <= 0;
    }

    /**
     * Gets the priority level of this listener for event handling.
     *
     * @return The priority level of this listener.
     */
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * Gets the type of events that this listener can handle.
     *
     * @return The type of events that this listener can handle, or {@code null} if no type is specified.
     */
    @Nullable
    @Override
    public Class<?> getType() {
        return type;
    }

    /**
     * Gets the target class that this listener is designed to handle events for.
     *
     * @return The target class that this listener is designed to handle events for.
     */
    @NotNull
    @Override
    public Class<? super T> getTarget() {
        return target;
    }

    /**
     * Determines whether this listener should persist after being invoked.
     * The listener persists if it is inherently persistent (as determined by {@link #hasIndefinitePersistence()})
     * or if the {@linkplain EventListener#persists internal persistence counter} is greater than zero
     * after being decremented.
     *
     * @return {@code true} if the listener should persist, {@code false} otherwise
     * @since 3.2.0
     */
    @Override
    public boolean shouldPersist() {
        return hasIndefinitePersistence() || ((--persists) > 0);
    }

    /**
     * Indicates whether this listener is inherently persistent.
     * A listener is considered inherently persistent if the {@linkplain #indefinitePersistence} flag is set to {@code true}.
     *
     * @return {@code true} if the listener is inherently persistent, {@code false} otherwise
     * @since 3.2.0
     */
    @Override
    public boolean hasIndefinitePersistence() {
        return indefinitePersistence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        EventListener<?> that = (EventListener<?>) o;
        return (priority == that.priority)
                && (indefinitePersistence == that.indefinitePersistence)
                && target.equals(that.target)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        result = (31 * result) + Objects.hashCode(type);
        result = (31 * result) + priority;
        result = (31 * result) + Boolean.hashCode(indefinitePersistence);
        return result;
    }

    @Override
    public String toString() {
        return "EventListener{" +
                "target=" + target +
                ", type=" + type +
                ", priority=" + priority +
                ", indefinitePersistence=" + indefinitePersistence +
                ", persists=" + persists +
                '}';
    }
}