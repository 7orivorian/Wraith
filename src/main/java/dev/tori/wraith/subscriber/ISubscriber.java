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

package dev.tori.wraith.subscriber;

import dev.tori.wraith.bus.IEventBus;
import dev.tori.wraith.listener.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents a subscriber that can register and manage event listeners.
 * <p>
 * An instance of a class implementing this interface can subscribe to an event bus by registering listeners.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @see Subscriber
 * @since 1.0.0
 */
public interface ISubscriber {

    /**
     * Registers a single event listener with this subscriber.
     *
     * @param listener The event listener to register.
     * @param <T>      The type of the listener.
     * @return The registered listener.
     */
    <T extends Listener<?>> T registerListener(T listener);

    /**
     * Registers multiple event listeners with this subscriber.
     *
     * @param listeners The event listeners to register.
     * @param <T>       The type of the listeners.
     * @return An array containing the registered listeners.
     */
    @SuppressWarnings("unchecked")
    <T extends Listener<?>> T[] registerListeners(T... listeners);

    /**
     * Unregisters a single event listener from this subscriber.
     *
     * @param listener The event listener to unregister.
     * @param <T>      The type of the listener.
     * @return {@code true} if this subscriber contained the specified listener, {@code false} otherwise.
     */
    <T extends Listener<?>> boolean unregisterListener(T listener);

    /**
     * Unregisters multiple event listeners from this subscriber.
     *
     * @param listeners The event listeners to unregister.
     * @param <T>       The type of the listeners.
     * @return {@code true} if this subscriber was changed as a result of the call, {@code false} otherwise.
     */
    @SuppressWarnings("unchecked")
    <T extends Listener<?>> boolean unregisterListeners(T... listeners);

    /**
     * Retrieves a collection of event listeners registered with this subscriber.
     *
     * @return A collection of registered event listeners.
     */
    @NotNull
    Collection<Listener<?>> getListeners();

    /**
     * Links this subscriber to the specified event bus.
     *
     * @param eventBus The event bus to link to this subscriber. Must not be {@code null}.
     * @throws NullPointerException If the {@code eventBus} is {@code null}.
     */
    void linkToBus(IEventBus eventBus);

    /**
     * Unlinks this subscriber from the specified event bus.
     *
     * @param eventBus The event bus to unlink from this subscriber. Must not be {@code null}.
     * @throws NullPointerException If the {@code eventBus} is {@code null}.
     */
    void unlinkFromBus(IEventBus eventBus);
}