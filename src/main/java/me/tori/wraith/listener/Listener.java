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

package me.tori.wraith.listener;

import me.tori.wraith.bus.EventBus;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An interface representing an event listener with priority, type, and target class information.
 *
 * @param <T> The type of event this listener is designed to handle.
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see EventListener
 * @see Invokable
 * @since <b>1.0.0</b>
 */
public interface Listener<T> extends Invokable<T> {

    int DEFAULT_PERSISTENCE = 0;

    /**
     * Gets the priority level of this listener for event handling.
     *
     * @return The priority level of this listener.
     */
    int getPriority();

    /**
     * Gets the type of events that this listener can handle.
     *
     * @return The type of events that this listener can handle, or {@code null} if no type is specified.
     */
    Class<?> getType();

    /**
     * Gets the target class that this listener is designed to handle events for.
     *
     * @return The target class that this listener is designed to handle events for.
     */
    Class<? super T> getTarget();

    /**
     * Determines whether this listener should persist after being invoked.
     *
     * @return {@code true} if the listener should persist, {@code false} otherwise.
     * @see EventBus#forEachListener(List, Predicate, Consumer, boolean)
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
     * Checks if the provided type is acceptable for this listener.
     * <p>
     * This default method evaluates whether the given type is acceptable by comparing it with
     * the type associated with this listener. It returns {@code true} if any of the following conditions are met:
     * <ul>
     *   <li>The provided type is {@code null}.</li>
     *   <li>The listener's type is {@code null}.</li>
     *   <li>The provided type is equal to the listener's type.</li>
     * </ul>
     *
     * @param type the class type to check for acceptability
     * @return {@code true} if the type is acceptable, {@code false} otherwise.
     * @since 3.3.0
     */
    default boolean isAcceptableType(Class<?> type) {
        return (type == null) || (getType() == null) || (getType() == type);
    }
}