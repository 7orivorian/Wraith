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

package me.tori.wraith.event.targeted;

import me.tori.wraith.listener.Listener;

/**
 * An interface representing an event that targets a specific class of listeners.
 * The class targeted by this event is determined by calling the {@link #getTargetClass()} method.
 * This interface is used to indicate that an event is designed to be directed at a specific listener class.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see ClassTargetingEvent
 * @since <b>3.0.0</b>
 */
public interface IClassTargetingEvent {

    /**
     * Retrieves the target class of listeners for this event.
     *
     * @return The class that represents the type of listeners targeted by this event.
     */
    Class<? extends Listener<?>> getTargetClass();
}