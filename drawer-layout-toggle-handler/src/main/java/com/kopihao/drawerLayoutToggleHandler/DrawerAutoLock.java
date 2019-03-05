package com.kopihao.drawerLayoutToggleHandler;

import android.os.CountDownTimer;

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
