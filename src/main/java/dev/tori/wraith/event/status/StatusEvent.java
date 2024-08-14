/*
 * Copyright (c) 2024 7orivorian.
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

package dev.tori.wraith.event.status;

import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of {@link IStatusEvent}.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @see IStatusEvent
 * @since 3.2.0
 */
public class StatusEvent implements IStatusEvent {

    protected EventStatus status = EventStatus.ALIVE;

    /**
     * {@inheritDoc}
     *
     * @return the current {@link EventStatus} of this event.
     */
    @NotNull
    @Override
    public EventStatus getEventStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     *
     * @param status the new {@link EventStatus} to set.
     */
    @Override
    public void setEventStatus(@NotNull EventStatus status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} if this event is suppressed, {@code false} otherwise.
     */
    @Override
    public boolean isSuppressed() {
        return status == EventStatus.SUPPRESSED;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} if this event is terminated, {@code false} otherwise.
     */
    @Override
    public boolean isTerminated() {
        return status == EventStatus.TERMINATED;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        StatusEvent that = (StatusEvent) obj;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return status.hashCode();
    }

    @Override
    public String toString() {
        return "StatusEvent{" +
                "status=" + status +
                '}';
    }
}