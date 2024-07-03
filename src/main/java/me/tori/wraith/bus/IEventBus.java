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
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public interface IEventBus {

    /**
     * Default priority that used when no other priority is specified
     *
     * @see EventListener#EventListener(Class)
     * @see EventListener#EventListener(Class, Class)
     */
    int DEFAULT_PRIORITY = 0;

    /**
     * @param subscriber the {@link ISubscriber} to be subscribed
     * @see #register(Listener)
     */
    void subscribe(ISubscriber subscriber);

    /**
     * @param subscriber the {@link ISubscriber} to be unsubscribed
     * @see #unregister(Listener)
     */
    void unsubscribe(ISubscriber subscriber);

    /**
     * @param listener the {@link Listener} to be registered
     */
    void register(Listener<?> listener);

    /**
     * @param listener the {@link Listener} to be unregistered
     */
    void unregister(Listener<?> listener);

    /**
     * @param event the event to be dispatched
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise.
     */
    boolean dispatch(Object event);

    /**
     * @param event the event to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise.
     */
    boolean dispatch(Object event, Class<?> type);

    /**
     * Shuts down this event bus, preventing future events from being dispatched.
     */
    void shutdown();

    /**
     * @return {@code true} if this event bus is shut down, {@code false} otherwise.
     */
    boolean isShutdown();
}