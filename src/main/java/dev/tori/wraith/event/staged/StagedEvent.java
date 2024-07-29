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

package dev.tori.wraith.event.staged;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a staged event implementation that specifies the stage at which the event occurs.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see IStagedEvent
 * @see EventStage
 * @since <b>1.0.0</b>
 */
@SuppressWarnings("ClassCanBeRecord")
public class StagedEvent implements IStagedEvent {

    private final @NotNull EventStage stage;

    /**
     * Constructs a {@code StagedEvent} with the specified stage.
     *
     * @param stage The {@link EventStage} representing the stage of the event.
     * @throws NullPointerException If the provided {@code stage} is {@code null}.
     */
    public StagedEvent(@NotNull EventStage stage) {
        this.stage = stage;
    }

    /**
     * Gets the stage of this staged event.
     *
     * @return The {@link EventStage} representing the stage of the event.
     */
    @NotNull
    @Override
    public EventStage getStage() {
        return stage;
    }

    @Override
    public String toString() {
        return "StagedEvent{" +
                "stage=" + stage +
                '}';
    }
}