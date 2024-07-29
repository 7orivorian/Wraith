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

package dev.tori.wraith.event.targeted;

import dev.tori.wraith.listener.Listener;
import org.jetbrains.annotations.Nullable;

/**
 * A basic implementation of the {@link IClassTargetingEvent} interface.
 * This class provides a simple way to specify the target class of listeners for an event.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see IClassTargetingEvent
 * @since <b>3.0.0</b>
 */
@SuppressWarnings("ClassCanBeRecord")
public class ClassTargetingEvent implements IClassTargetingEvent {

    @Nullable
    private final Class<? extends Listener<?>> targetClass;

    /**
     * Constructs a new {@link ClassTargetingEvent} with the specified target class of listeners.
     *
     * @param targetClass The class representing the type of listeners to be targeted by this event.
     */
    public ClassTargetingEvent(@Nullable Class<? extends Listener<?>> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Retrieves the target class of listeners for this event.
     *
     * @return The class that represents the type of listeners targeted by this event.
     */
    @Nullable
    @Override
    public Class<? extends Listener<?>> getTargetClass() {
        return targetClass;
    }
}