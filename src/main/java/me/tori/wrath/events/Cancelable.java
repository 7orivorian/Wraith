package main.java.me.tori.wrath.events;

import main.java.me.tori.wrath.listeners.ICancelable;

/**
 * @author <b>7orivorian</b>
 * @version <b>Wrath v1.0.0</b>
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
}