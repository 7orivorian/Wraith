/*
 * Copyright (c) 2021-2025 7orivorian.
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

package dev.tori.wraith.bus;

import dev.tori.wraith.event.Target;
import dev.tori.wraith.event.status.IStatusEvent;
import dev.tori.wraith.listener.Listener;
import dev.tori.wraith.util.IndexedHashSet;

import java.util.Comparator;
import java.util.Objects;

/**
 * An implementation of {@link AbstractEventBus}.
 * <p>
 * Manages listener registration, event dispatching, and task execution.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventBus extends AbstractEventBus {

    /**
     * An {@link IndexedHashSet} of {@link Listener listeners} registered to the event bus.
     */
    private final IndexedHashSet<Listener> listeners;
    /**
     * Indicates whether the listeners in the event bus are sorted.
     */
    private boolean sorted = false;

    /**
     * Creates a new {@link EventBus} instance.
     */
    public EventBus() {
        super();
        this.listeners = new IndexedHashSet<>();
    }

    /**
     * Registers a {@link Listener listener} to the event bus.
     *
     * @param listener the {@link Listener} to be registered
     * @throws NullPointerException if the given {@link Listener} is {@code null}
     */
    @Override
    public void register(Listener<?> listener) {
        Objects.requireNonNull(listener, "Cannot register null listener to event bus " + id + ".");

        listeners.add(listener);
        sorted = false;
    }

    /**
     * Unregisters a {@link Listener listener} from the event bus. The unregistered listener will no longer be invoked
     * by events dispatched to the event bus.
     *
     * @param listener the {@link Listener} to be unregistered
     * @throws NullPointerException if the given {@link Listener} is {@code null}
     */
    @Override
    public void unregister(Listener<?> listener) {
        Objects.requireNonNull(listener, "Cannot unregister null listener from event bus " + id + ".");

        listeners.removeIf(l -> l.equals(listener));
    }

    /**
     * Dispatches the given event to all valid registered listeners.
     * <p>
     * The {@code type} parameter serves as a filtering mechanism for listeners, allowing listeners to be selectively
     * invoked based on their type for more targeted event handling.
     *
     * @param event          the event to be dispatched.
     * @param target         the {@linkplain Target target listener} to invoke.
     * @param invertPriority flag to dispatch the event in inverse listener priority.
     * @return {@code true} if the given event is {@linkplain IStatusEvent suppressed or terminated} by any listener,
     * {@code false} otherwise.
     * @throws NullPointerException          if the given event is {@code null}
     * @throws UnsupportedOperationException if this event bus is {@link #shutdown}
     */
    @Override
    public boolean dispatch(Object event, Target target, boolean invertPriority) {
        Objects.requireNonNull(event, "Cannot dispatch a null event to event bus " + id + ".");
        Objects.requireNonNull(target, "Cannot dispatch an event with a null target to event bus " + id + ".");

        if (isShutdown()) {
            throw new UnsupportedOperationException("Event bus " + id + " is shutdown!");
        } else {
            taskExecutor.onEvent(event);

            if (!sorted) {
                listeners.sort(Comparator.naturalOrder());
                sorted = true;
            }

            dispatchToEachListener(
                    event,
                    listeners,
                    listener -> target.targets(listener.getClass()) && listener.getTarget().targets(event.getClass()),
                    invertPriority
            );

            if (event instanceof IStatusEvent e) {
                return e.isSuppressed() || e.isTerminated();
            }
        }
        return false;
    }

    /**
     * Retrieves the set of listeners currently registered to the event bus.
     *
     * @return an {@link IndexedHashSet} containing all registered {@link Listener listeners}.
     */
    public IndexedHashSet<Listener> getListeners() {
        return listeners;
    }

    @Override
    public String toString() {
        return "EventBus{" +
                "id=" + id +
                ", shutdown=" + shutdown +
                ", listeners=" + listeners +
                ", sorted=" + sorted +
                ", subscribers=" + subscribers +
                ", taskExecutor=" + taskExecutor +
                '}';
    }
}