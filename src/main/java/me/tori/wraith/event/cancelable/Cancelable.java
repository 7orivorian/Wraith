package me.tori.wraith.event.cancelable;

/**
 * @author <b><a href="https://github.com/7orivorian">7orivorian</a></b>
 * @since <b>1.0.0</b>
 */
public class Cancelable implements ICancelable {

    private boolean canceled = false;

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        this.canceled = cancel;
    }

    @Override
    public String toString() {
        return "Cancelable{" +
                "canceled=" + canceled +
                '}';
    }
}