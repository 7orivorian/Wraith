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

package dev.tori.wraith.listener;

import dev.tori.wraith.event.Target;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An event listener implementation that wraps a lambda expression or functional interface as the invokable action.
 * This provides a convenient way to create event listeners using lambda expressions.
 *
 * @param <E> The type of event handled by this listener.
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 1.3.0
 */
public class LambdaEventListener<E> extends EventListener<E> {

    @NotNull
    protected final Invokable<E> invokable;

    /**
     * Constructs a new {@link LambdaEventListener} with the given target and invokable action.
     *
     * @param target    This listener's {@link Target}.
     * @param invokable The invokable action to be executed when the event is dispatched.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public LambdaEventListener(@NotNull Target target, @NotNull Invokable<E> invokable) {
        super(target);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    /**
     * Constructs a new {@link LambdaEventListener} with the given target, priority, and invokable action.
     *
     * @param target    This listener's {@link Target}.
     * @param priority  The priority of this listener.
     * @param invokable The invokable action to be executed when the event is dispatched.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public LambdaEventListener(@NotNull Target target, int priority, @NotNull Invokable<E> invokable) {
        super(target, priority);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    /**
     * Constructs a new {@link LambdaEventListener} with the given target, priority, and invokable action.
     *
     * @param target    This listener's {@link Target}.
     * @param priority  The priority of this listener.
     * @param persists  How many events this listener should handle before being killed.
     *                  A value of {@code <= 0} will flag this listener to {@linkplain #indefinitePersistence persist indefinitely}.
     * @param invokable The invokable action to be executed when the event is dispatched.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public LambdaEventListener(@NotNull Target target, int priority, int persists, @NotNull Invokable<E> invokable) {
        super(target, priority, persists);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    /**
     * Invokes the wrapped invokable action with the provided event.
     *
     * @param event The event to be handled.
     */
    @Override
    public void invoke(E event) {
        invokable.invoke(event);
    }

    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }

        LambdaEventListener<?> that = (LambdaEventListener<?>) obj;
        return invokable.equals(that.invokable);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + invokable.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LambdaEventListener{" +
                "target=" + target +
                ", priority=" + priority +
                ", indefinitePersistence=" + indefinitePersistence +
                ", persists=" + persists +
                ", invokable=" + invokable +
                '}';
    }
}