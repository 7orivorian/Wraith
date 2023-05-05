package me.tori.wraith.listener;

import me.tori.wraith.event.Invokable;

import java.util.Objects;

/**
 * @author <b>7orivorian</b>
 * @since <b>April 05, 2023</b>
 */
public class LambdaEventListener<E> extends EventListener<E> {

    protected final Invokable<E> invokable;

    public LambdaEventListener(Class<? super E> target, Invokable<E> invokable) {
        super(target);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    public LambdaEventListener(Class<? super E> target, int priority, Invokable<E> invokable) {
        super(target, priority);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    public LambdaEventListener(Class<? super E> target, Class<?> type, Invokable<E> invokable) {
        super(target, type);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

    public LambdaEventListener(Class<? super E> target, int priority, Class<?> type, Invokable<E> invokable) {
        super(target, priority, type);
        Objects.requireNonNull(invokable);
        this.invokable = invokable;
    }

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