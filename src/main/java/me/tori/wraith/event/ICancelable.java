package me.tori.wraith.event;

/**
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
 */
public interface ICancelable {

    boolean isCanceled();

    void setCanceled(boolean canceled);

    default void cancel() {
        this.setCanceled(true);
    }
}