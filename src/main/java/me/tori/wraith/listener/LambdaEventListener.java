package me.tori.wraith.listener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An event listener implementation that wraps a lambda expression or functional interface as the invokable action.
 * This provides a convenient way to create event listeners using lambda expressions.
 *
 * @param <E> The type of event handled by this listener.
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.3.0</b>
 */
public class LambdaEventListener<E> extends EventListener<E> {

    protected final @NotNull Invokable<E> invokable;

    /**
     * Constructs a new `LambdaEventListener` with the given target class and invokable action.
     *
     * @param target    The target class of the event.
     * @param invokable The invokable action to be executed when the event is dispatched.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public LambdaEventListener(@NotNull Class<? super E> target, @NotNull Invokable<E> invokable) {
        super(target);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    /**
     * Constructs a new `LambdaEventListener` with the given target class, priority, and invokable action.
     *
     * @param target    The target class of the event.
     * @param priority  The priority of this listener.
     * @param invokable The invokable action to be executed when the event is dispatched.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public LambdaEventListener(@NotNull Class<? super E> target, int priority, @NotNull Invokable<E> invokable) {
        super(target, priority);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    /**
     * Constructs a new `LambdaEventListener` with the given target class, type, and invokable action.
     *
     * @param target    The target class of the event.
     * @param type      The type of the event.
     * @param invokable The invokable action to be executed when the event is dispatched.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public LambdaEventListener(@NotNull Class<? super E> target, @Nullable Class<?> type, @NotNull Invokable<E> invokable) {
        super(target, type);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    /**
     * Constructs a new `LambdaEventListener` with the given target class, priority, type, and invokable action.
     *
     * @param target    The target class of the event.
     * @param priority  The priority of this listener.
     * @param type      The type of the event.
     * @param invokable The invokable action to be executed when the event is dispatched.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public LambdaEventListener(@NotNull Class<? super E> target, int priority, @Nullable Class<?> type, @NotNull Invokable<E> invokable) {
        super(target, priority, type);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    /**
     * Constructs a new {@code LambdaEventListener} with the given target class, priority, type, and invokable action.
     *
     * @param target    The target class of the event.
     * @param type      The type of the event.
     * @param priority  The priority of this listener.
     * @param persists How many events this listener should handle before being killed.
     * @param invokable The invokable action to be executed when the event is dispatched.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public LambdaEventListener(@NotNull Class<? super E> target, @Nullable Class<?> type, int priority, int persists, @NotNull Invokable<E> invokable) {
        super(target, type, priority, persists);
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LambdaEventListener<?> that = (LambdaEventListener<?>) o;
        return invokable.equals(that.invokable);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + invokable.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LambdaEventListener{" +
                "invokable=" + invokable +
                ", priority=" + priority +
                ", type=" + type +
                ", target=" + target +
                '}';
    }
}