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

import me.tori.wraith.event.cancelable.ICancelableEvent;
import me.tori.wraith.event.targeted.IClassTargetingEvent;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>3.0.0</b>
 */
public interface TargetableEventBus extends IEventBus {

    /**
     * @param event the {@linkplain IClassTargetingEvent} to dispatch
     * @return {@code true} if the given event was {@linkplain ICancelableEvent cancelable} and canceled, {@code false otherwise}
     * @see EventBus#dispatchTargeted(IClassTargetingEvent)
     */
    boolean dispatchTargeted(IClassTargetingEvent event);

    /**
     * @param event the {@linkplain IClassTargetingEvent} to dispatch
     * @param type  the type of listener to invoke (can be {@code null})
     * @return {@code true} if the given event was {@linkplain ICancelableEvent cancelable} and canceled, {@code false otherwise}
     * @see EventBus#dispatchTargeted(IClassTargetingEvent, Class)
     */
    boolean dispatchTargeted(IClassTargetingEvent event, Class<?> type);
}