/*
 * Copyright (c) 2024-2025 7orivorian.
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
 * Represents an event that can be alive, suppressed, or terminated.
 * <p>
 * This interface defines the status of an event and provides methods to
 * manipulate and check this status. The possible statuses are defined by {@link EventStatus}.</p>
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @see StatusEvent
 * @since 3.2.0
 */
public interface IStatusEvent {

    /**
     * Gets the current status of this event.
     *
     * @return the current {@link EventStatus} of this event.
     */
    @NotNull
    EventStatus getEventStatus();

    /**
     * Sets the status of this event.
     * <p>
     * <b>Note:</b>
     * This method should not be called directly.
     *
     * @param status the new {@link EventStatus} to set.
     * @see #suppress()
     * @see #terminate()
     */
    void setEventStatus(@NotNull EventStatus status);

    /**
     * Checks whether this event is suppressed.
     *
     * @return {@code true} if this event is suppressed, {@code false} otherwise.
     */
    boolean isSuppressed();

    /**
     * Sets the suppression state of this event.
     *
     * <p>If the event is not terminated, this method will update the event's state to either
     * suppressed or alive based on the provided boolean parameter ({@code suppressed}).</p>
     *
     * @param suppressed {@code true} to suppress the event, {@code false} to unsuppress it.
     * @return {@code true} if the suppression state was successfully updated, {@code false} if the event is terminated.
     */
    default boolean setSuppressed(boolean suppressed) {
        if (getEventStatus() != EventStatus.TERMINATED) {
            if (suppressed) {
                setEventStatus(EventStatus.SUPPRESSED);
            } else {
                setEventStatus(EventStatus.ALIVE);
            }
            return true;
        }
        return false;
    }

    /**
     * Convenience method to suppress this event if it is not currently terminated.
     * <p>
     * This is a shorthand method equivalent to calling {@code setSuppressed(true)}.
     *
     * @return {@code true} if the event was successfully suppressed, {@code false} if the event is terminated.
     * @see #setSuppressed(boolean)
     */
    default boolean suppress() {
        return setSuppressed(true);
    }

    /**
     * Checks whether this event is terminated.
     *
     * @return {@code true} if this event is terminated, {@code false} otherwise.
     */
    boolean isTerminated();

    /**
     * Convenience method to terminate this event.
     * <p>
     * This is a shorthand method equivalent to calling {@code setEventStatus(EventStatus.TERMINATED)}.
     *
     * @see #setEventStatus(EventStatus)
     */
    default void terminate() {
        setEventStatus(EventStatus.TERMINATED);
    }

    /**
     * Checks whether this event is alive.
     * <p>
     * An event is only considered alive if {@code getEventStatus() == EventStatus.ALIVE}.
     *
     * @return {@code true} if this event is alive, {@code false} otherwise.
     * @since 4.1.0
     */
    boolean isAlive();

    /**
     * Enum representing the possible statuses of a {@link IStatusEvent}.
     */
    enum EventStatus {
        /**
         * The event is active and not suppressed or terminated.
         */
        ALIVE,
        /**
         * The event is suppressed. A suppressed event may become unsuppressed.
         */
        SUPPRESSED,
        /**
         * The event is terminated and cannot be altered further.
         */
        TERMINATED
    }
}