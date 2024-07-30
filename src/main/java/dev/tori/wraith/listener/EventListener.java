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
import dev.tori.wraith.event.Target;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An abstract base class for event listeners that implements the {@link Listener} interface.
 * Event listeners provide event handling logic with specified targets and priorities.
 *
 * @param <T> The type of event this listener is designed to handle.
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @see Listener
 * @see Invokable
 * @since 1.0.0
 */
public abstract class EventListener<T> implements Listener<T> {

    @NotNull
    protected final Target target;
    protected final int priority;
    protected final boolean indefinitePersistence;
    protected int persists;

    /**
     * Constructs an event listener with default {@linkplain IEventBus#DEFAULT_PRIORITY priority} and {@linkplain #DEFAULT_PERSISTENCE persistence}.
     *
     * @param target This listener's {@linkplain Target target class}.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    @Contract(pure = true)
    public EventListener(@NotNull Target target) {
        this(target, IEventBus.DEFAULT_PRIORITY, DEFAULT_PERSISTENCE);
    }

    /**
     * Constructs an event listener with a specified {@code priority} and {@linkplain #DEFAULT_PERSISTENCE default persistence}.
     *
     * @param target   This listener's {@link Target}.
     * @param priority The priority level of this listener for event handling.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    @Contract(pure = true)
    public EventListener(@NotNull Target target, int priority) {
        this(target, priority, DEFAULT_PERSISTENCE);
    }

    /**
     * Constructs an event listener with a specified {@code priority} and {@code persists}.
     *
     * @param target   This listener's {@link Target}.
     * @param priority The priority level of this listener for event handling.
     * @param persists How many events this listener should handle before being killed.
     *                 A value {@code <= 0} will {@linkplain #indefinitePersistence flag this listener to persist indefinitely}.
     * @throws NullPointerException if {@code target} is {@code null}.
     * @since 3.2.0
     */
    @Contract(pure = true)
    public EventListener(@NotNull Target target, int priority, int persists) {
        Objects.requireNonNull(target);
        this.target = target;
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
     * Gets the {@link Target} of this listener.
     *
     * @return The {@link Target} of this listener.
     */
    @NotNull
    @Override
    public Target getTarget() {
        return target;
    }

    /**
     * Determines whether this listener should persist after being invoked.
     * The listener persists if it is inherently persistent (as determined by {@link #hasIndefinitePersistence()})
     * or if the {@linkplain EventListener#persists internal persistence counter} is greater than zero
     * after being decremented.
     *
     * @return {@code true} if the listener should persist, {@code false} otherwise.
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
     * @return {@code true} if the listener is inherently persistent, {@code false} otherwise.
     * @since 3.2.0
     */
    @Override
    public boolean hasIndefinitePersistence() {
        return indefinitePersistence;
    }

    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        EventListener<?> that = (EventListener<?>) obj;
        return (priority == that.priority)
                && (indefinitePersistence == that.indefinitePersistence)
                && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        result = (31 * result) + priority;
        result = (31 * result) + Boolean.hashCode(indefinitePersistence);
        return result;
    }

    @Override
    public String toString() {
        return "EventListener{" +
                "target=" + target +
                ", priority=" + priority +
                ", indefinitePersistence=" + indefinitePersistence +
                ", persists=" + persists +
                '}';
    }
}