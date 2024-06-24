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

package me.tori.wraith.event.cancelable;

/**
 * Default implementation of the {@link ICancelableEvent} interface.
 *
 * <p>This class provides a basic implementation of the cancelable event behavior.
 * Event classes can extend this class to inherit the cancellation capability.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see ICancelableEvent
 * @since <b>1.0.0</b>
 */
public class CancelableEvent implements ICancelableEvent {

    /**
     * Indicates the cancellation state of this event.
     */
    private boolean canceled = false;

    /**
     * Checks whether this event is canceled.
     *
     * @return {@code true} if this event is canceled, {@code false} otherwise
     */
    @Override
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param canceled {@code true} to mark this event as canceled, {@code false} otherwise
     * @implNote Passing {@code false} to this method can un-cancel this event if it was previously canceled.
     */
    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public String toString() {
        return "Cancelable{" +
                "canceled=" + canceled +
                '}';
    }
}