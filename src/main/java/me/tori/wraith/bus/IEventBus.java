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

package me.tori.wraith.bus;

import me.tori.wraith.event.status.IStatusEvent;
import me.tori.wraith.listener.EventListener;
import me.tori.wraith.listener.Listener;
import me.tori.wraith.subscriber.ISubscriber;

/**
 * An event bus that allows for the subscription, registration, and dispatching of events to listeners.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public interface IEventBus {

    /**
     * Default priority used when no other priority is specified.
     *
     * @see EventListener#EventListener(Class)
     * @see EventListener#EventListener(Class, Class)
     */
    int DEFAULT_PRIORITY = 0;

    /**
     * Subscribes the specified subscriber to this event bus.
     *
     * @param subscriber the {@link ISubscriber} to be subscribed
     * @see #register(Listener)
     */
    void subscribe(ISubscriber subscriber);

    /**
     * Unsubscribes the specified subscriber from this event bus.
     *
     * @param subscriber the {@link ISubscriber} to be unsubscribed
     * @see #unregister(Listener)
     */
    void unsubscribe(ISubscriber subscriber);

    /**
     * Registers the specified listener to this event bus.
     *
     * @param listener the {@link Listener} to be registered
     */
    void register(Listener<?> listener);

    /**
     * Unregisters the specified listener from this event bus.
     *
     * @param listener the {@link Listener} to be unregistered
     */
    void unregister(Listener<?> listener);

    /**
     * Dispatches the specified event to all registered listeners.
     *
     * @param event the event to be dispatched
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise.
     */
    boolean dispatch(Object event);

    /**
     * Dispatches the specified event to all registered listeners of the specified type.
     *
     * @param event the event to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise.
     */
    boolean dispatch(Object event, Class<?> type);

    /**
     * Dispatches the specified event to all registered listeners, with the option to invert the processing priority.
     *
     * @param event          the event to be dispatched
     * @param invertPriority if {@code true}, listeners are processed in order of inverse priority; otherwise,
     *                       they are processed in normal order
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise.
     */
    boolean dispatch(Object event, boolean invertPriority);

    /**
     * Dispatches the specified event to all registered listeners of the specified type, with the option to invert the processing priority.
     *
     * @param event          the event to be dispatched
     * @param type           the type of listener to invoke (can be {@code null})
     * @param invertPriority if {@code true}, listeners are processed in order of inverse priority; otherwise,
     *                       they are processed in normal order
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise.
     */
    boolean dispatch(Object event, Class<?> type, boolean invertPriority);

    /**
     * Shuts down this event bus, preventing future events from being dispatched.
     */
    void shutdown();

    /**
     * Checks if this event bus is shut down.
     *
     * @return {@code true} if this event bus is shut down, {@code false} otherwise.
     */
    boolean isShutdown();
}