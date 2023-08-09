package me.tori.wraith.event.cancelable;

/**
 * Represents a cancelable event that can be prevented from further processing.
 *
 * <p>This interface allows events to be canceled, indicating that they should not proceed through their
 * normal processing flow. This is particularly useful in scenarios where certain conditions require an event
 * to be halted from performing its intended action.
 *
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @see Cancelable
 * @since <b>1.0.0</b>
 */
public interface ICancelable {

    /**
     * Checks whether this event is canceled.
     *
     * @return {@code true} if this event is canceled, {@code false} otherwise
     */
    boolean isCanceled();

    /**
     * Sets the cancellation state of this event.
     *
     * @param canceled {@code true} to mark this event as canceled, {@code false} otherwise
     * @implNote Passing {@code false} to this method can un-cancel this event if it was previously canceled.
     */
    void setCanceled(boolean canceled);

    /**
     * Convenience method to cancel this event.
     *
     * <p>This is a shorthand method equivalent to calling {@code setCanceled(true)}, conveniently
     * marking this event as canceled.
     *
     * @see #setCanceled(boolean)
     */
    default void cancel() {
        setCanceled(true);
    }
}