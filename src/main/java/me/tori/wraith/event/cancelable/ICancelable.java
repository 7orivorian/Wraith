package me.tori.wraith.event.cancelable;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public interface ICancelable {

    boolean isCanceled();

    void setCanceled(boolean canceled);

    default void cancel() {
        this.setCanceled(true);
    }
}