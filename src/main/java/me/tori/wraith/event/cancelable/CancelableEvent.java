package me.tori.wraith.event.cancelable;

/**
 * Default implementation of the {@link ICancelableEvent} interface.
 *
 * <p>This class provides a basic implementation of the cancelable event behavior.
 * Event classes can extend this class to inherit the cancellation capability.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see ICancelableEvent
 * @since <b>1.0.0</b>
 */
public class CancelableEvent implements ICancelableEvent {

    /**
     * Indicates the cancellation state of this event.
     */
    private boolean canceled = false;

    /**
     * Checks whether this event is canceled.
     *
     * @return {@code true} if this event is canceled, {@code false} otherwise
     */
    @Override
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param canceled {@code true} to mark this event as canceled, {@code false} otherwise
     * @implNote Passing {@code false} to this method can un-cancel this event if it was previously canceled.
     */
    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public String toString() {
        return "Cancelable{" +
                "canceled=" + canceled +
                '}';
    }
}