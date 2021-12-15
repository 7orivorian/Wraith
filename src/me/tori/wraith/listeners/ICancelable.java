package me.tori.wraith.listeners;

/**
 * @author <b>7orivorian</b>
 * @version <b>WraithAPI v1.0.0</b>
 * @since <b>December 12, 2021</b>
 */
public interface ICancelable {

    boolean isCanceled();

    void setCanceled(boolean canceled);

    default void cancel() {
        this.setCanceled(true);
    }
}