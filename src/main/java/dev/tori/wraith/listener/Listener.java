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

import dev.tori.wraith.bus.EventBus;
import dev.tori.wraith.event.Target;
import dev.tori.wraith.util.IndexedHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * An interface representing an event listener with priority, type, and target class information.
 *
 * @param <T> The type of event this listener is designed to handle.
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @see EventListener
 * @see Invokable
 * @since 1.0.0
 */
public interface Listener<T> extends Invokable<T>, Comparable<Listener<T>> {

    int DEFAULT_PERSISTENCE = 0;

    /**
     * Gets the priority level of this listener for event handling.
     *
     * @return The priority level of this listener.
     */
    int getPriority();

    /**
     * Gets the {@linkplain Target target class} of this listener.
     *
     * @return the {@linkplain Target target class} of this listener.
     * @since 4.0.0
     */
    @NotNull
    Target getTarget();

    /**
     * Determines whether this listener should persist after being invoked.
     *
     * @return {@code true} if the listener should persist, {@code false} otherwise.
     * @see EventBus#dispatchToEachListener(Object, IndexedHashSet, Predicate, boolean)
     * @since 3.2.0
     */
    @SuppressWarnings("JavadocReference")
    default boolean shouldPersist() {
        return hasIndefinitePersistence();
    }

    /**
     * Indicates whether this listener is inherently persistent.
     *
     * @return {@code true} if the listener is inherently persistent, {@code false} otherwise.
     * @since 3.2.0
     */
    default boolean hasIndefinitePersistence() {
        return true;
    }

    /**
     * @param listener the listener to be compared.
     * @apiNote This class has a natural ordering that is inconsistent with equals.
     * @since 4.0.0
     */
    @Override
    default int compareTo(@NotNull Listener<T> listener) {
        return Integer.compare(listener.getPriority(), getPriority());
    }
}