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

package dev.tori.wraith.listener;

import dev.tori.wraith.event.Target;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * @author <a href="https://github.com/7orivorian">7orivorian</a>
 * @since 4.1.0
 */
public class PredicateEventListener<E> extends EventListener<E> {

    @NotNull
    private final Predicate<E> predicate;
    @NotNull
    protected final Invokable<E> invokable;

    public PredicateEventListener(@NotNull Target target, @NotNull Predicate<E> predicate, @NotNull Invokable<E> invokable) {
        super(target);
        this.predicate = predicate;
        this.invokable = invokable;
    }

    public PredicateEventListener(@NotNull Target target, @NotNull Predicate<E> predicate, int priority, @NotNull Invokable<E> invokable) {
        super(target, priority);
        this.predicate = predicate;
        this.invokable = invokable;
    }

    public PredicateEventListener(@NotNull Target target, @NotNull Predicate<E> predicate, int priority, int persists, @NotNull Invokable<E> invokable) {
        super(target, priority, persists);
        this.predicate = predicate;
        this.invokable = invokable;
    }

    @Override
    public void invoke(E event) {
        if ((event != null) && predicate.test(event)) {
            this.invokable.invoke(event);
        }
    }
}