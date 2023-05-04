package me.tori.wraith.event;

/**
 * @author <b>7orivorian</b>
 * @since <b>December 12, 2021</b>
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cancelable that = (Cancelable) o;
        return canceled == that.canceled;
    }

    @Override
    public int hashCode() {
        return (canceled ? 1 : 0);
    }
}