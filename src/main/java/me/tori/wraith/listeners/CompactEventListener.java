package me.tori.wraith.listeners;

/**
 * @author <b>7orivorian</b>
 * @version <b>WraithLib v1.3.0</b>
 * @since <b>April 05, 2023</b>
 */
public class CompactEventListener<E> extends EventListener<E> {

    private final Invokable<E> invokable;

    public CompactEventListener(Class<? super E> target, Invokable<E> invokable) {
        super(target);
        this.invokable = invokable;
    }

    public CompactEventListener(Class<? super E> target, int priority, Invokable<E> invokable) {
        super(target, priority);
        this.invokable = invokable;
    }

    public CompactEventListener(Class<? super E> target, Class<?> type, Invokable<E> invokable) {
        super(target, type);
        this.invokable = invokable;
    }

    public CompactEventListener(Class<? super E> target, int priority, Class<?> type, Invokable<E> invokable) {
        super(target, priority, type);
        this.invokable = invokable;
    }

    @Override
    public void invoke(E event) {
        invokable.invoke(event);
    }
}