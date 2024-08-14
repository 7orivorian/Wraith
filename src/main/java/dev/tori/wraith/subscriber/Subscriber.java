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

package dev.tori.wraith.subscriber;

import dev.tori.wraith.bus.IEventBus;
import dev.tori.wraith.listener.Listener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default implementation of {@link ISubscriber} that manages event listeners and their registration.
 * <p>
 * This class provides methods to register single or multiple event listeners and retrieve registered listeners.
 *
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @see ISubscriber
 * @since 1.0.0
 */
public class Subscriber implements ISubscriber {

    /**
     * The amount of {@linkplain Subscriber} instances that have been created.
     */
    private static int instances = 0;

    private final int id;
    private final Set<@NotNull IEventBus> linkedBuses = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final List<@NotNull Listener<?>> listeners = new ArrayList<>();

    public Subscriber() {
        this.id = instances++;
    }

    /**
     * Registers a single event listener with this subscriber.
     *
     * @param listener The event listener to register.
     * @param <T>      The type of the listener.
     * @return The registered listener.
     */
    @Override
    public <T extends Listener<?>> T registerListener(@NotNull T listener) {
        listeners.add(listener);
        if (!linkedBuses.isEmpty()) {
            linkedBuses.forEach(bus -> bus.register(listener));
        }
        return listener;
    }

    /**
     * Registers multiple event listeners with this subscriber.
     *
     * @param listeners The event listeners to register.
     * @param <T>       The type of the listeners.
     * @return An array containing the registered listeners.
     */
    @Contract("_ -> param1")
    @SafeVarargs
    @Override
    public final <T extends Listener<?>> T[] registerListeners(@NotNull T... listeners) {
        final List<@NotNull T> list = Arrays.asList(listeners);
        this.listeners.addAll(list);
        if (!linkedBuses.isEmpty()) {
            linkedBuses.forEach(bus -> list.forEach(bus::register));
        }
        return listeners;
    }

    /**
     * Unregisters a single event listener from this subscriber.
     *
     * @param listener The event listener to unregister.
     * @param <T>      The type of the listener.
     * @return {@code true} if this subscriber contained the specified listener, {@code false} otherwise.
     */
    @Override
    public <T extends Listener<?>> boolean unregisterListener(@NotNull T listener) {
        if (!linkedBuses.isEmpty()) {
            linkedBuses.forEach(bus -> bus.unregister(listener));
        }
        return listeners.remove(listener);
    }

    /**
     * Unregisters multiple event listeners from this subscriber.
     *
     * @param listeners The event listeners to unregister.
     * @param <T>       The type of the listeners.
     * @return {@code true} if this subscriber was changed as a result of the call, {@code false} otherwise.
     */
    @SafeVarargs
    @Override
    public final <T extends Listener<?>> boolean unregisterListeners(T... listeners) {
        if (!linkedBuses.isEmpty()) {
            for (T listener : listeners) {
                linkedBuses.forEach(bus -> bus.unregister(listener));
            }
        }
        return this.listeners.removeAll(Arrays.asList(listeners));
    }

    /**
     * Retrieves a collection of event listeners registered with this subscriber.
     *
     * @return A collection of registered event listeners.
     */
    @Override
    public Collection<Listener<?>> getListeners() {
        return listeners;
    }

    /**
     * Links this subscriber to the specified event bus.
     * <p>
     * This method adds the given {@code eventBus} to the {@linkplain #linkedBuses set of event buses that this subscriber is linked to}.
     * Once linked, the event bus can register the event listeners managed by this subscriber.
     *
     * @param eventBus The event bus to link to this subscriber. Must not be {@code null}.
     * @throws NullPointerException If the {@code eventBus} is {@code null}.
     * @implNote This method should not be called outside an {@link IEventBus} implementation
     */
    @Override
    public void linkToBus(IEventBus eventBus) {
        Objects.requireNonNull(eventBus, "Cannot link subscriber to a null event bus");
        linkedBuses.add(eventBus);
    }

    /**
     * Unlinks this subscriber from the specified event bus.
     * <p>
     * This method removes the given {@code eventBus} from the {@linkplain #linkedBuses set of event buses that this subscriber is linked to}.
     * Once unlinked, the event bus will no longer register the event listeners managed by this subscriber.
     *
     * @param eventBus The event bus to unlink from this subscriber. Must not be {@code null}.
     * @throws NullPointerException If the {@code eventBus} is {@code null}.
     * @implNote This method should not be called outside an {@link IEventBus} implementation
     */
    @Override
    public void unlinkFromBus(IEventBus eventBus) {
        Objects.requireNonNull(eventBus, "Cannot unlink unsubscriber from a null event bus");
        linkedBuses.remove(eventBus);
    }

    /**
     * Returns this {@linkplain Subscriber}'s {@linkplain #id}.
     *
     * @return this {@linkplain Subscriber}'s {@linkplain #id}.
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Subscriber that = (Subscriber) obj;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "linkedBuses=" + linkedBuses +
                ", listeners=" + listeners +
                '}';
    }
}