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

package me.tori.wraith.subscriber;

import me.tori.wraith.listener.Listener;

import java.util.Collection;

/**
 * Represents a subscriber that can register and manage event listeners.
 * <p>An instance of a class implementing this interface can subscribe to an event bus by registering listeners.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see Subscriber
 * @since <b>1.0.0</b>
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
     * Retrieves a collection of event listeners registered with this subscriber.
     *
     * @return A collection of registered event listeners.
     */
    Collection<Listener<?>> getListeners();
}