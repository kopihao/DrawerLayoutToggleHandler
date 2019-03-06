package com.kopirealm.drawer_layout_toggle_handler;

import android.os.CountDownTimer;

/**
 * A timer to manipulate {@link android.support.v4.widget.DrawerLayout}
 */
public class DrawerAutoLock extends CountDownTimer {

    private boolean isRunning = false;

    public DrawerAutoLock() {
        this(2 * 1000, 1000);
    }

    public DrawerAutoLock(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        isRunning = true;
    }

    @Override
    public void onFinish() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void execute() {
        isRunning = true;
        super.start();
    }
}
