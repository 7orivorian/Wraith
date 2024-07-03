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

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 3.0.0
 * @deprecated This event bus' functionality is now built into the {@linkplain IEventBus standard event bus}.
 */
@Deprecated(since = "3.3.0", forRemoval = true)
public interface InvertableEventBus extends IEventBus {

    /**
     * @param event the event to be dispatched
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise
     * @see EventBus#dispatchInverted(Object)
     * @deprecated This method's functionality is now handled by {@link #dispatch(Object, boolean)}.
     */
    @Deprecated(since = "3.3.0", forRemoval = true)
    boolean dispatchInverted(Object event);

    /**
     * @param event the event to be dispatched
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise
     * @see EventBus#dispatchInverted(Object, Class)
     * @deprecated This method's functionality is now handled by {@link #dispatch(Object, Class, boolean)}.
     */
    @Deprecated(since = "3.3.0", forRemoval = true)
    boolean dispatchInverted(Object event, Class<?> type);
}