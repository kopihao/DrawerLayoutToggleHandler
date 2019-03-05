package com.kopihao.drawerLayoutToggleHandler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class DrawerLayoutToggleHandler implements DrawerLayoutToggleListener, DrawerLayout.DrawerListener, View.OnTouchListener {

    private Context context;
    private BaseFragmentDrawerPanel drawerPanel;
    private View drawerToggle;
    private DrawerAutoLock drawerAutoLock;
    private DrawerToggleDetector drawerToggleDetector;
    private DrawerLayout drawerLayout;
    private int drawerLayoutGravity = GravityCompat.END;
    private boolean hasAutoLock = true;

    public DrawerLayoutToggleHandler(Context context) {
        this(context, GravityCompat.END);
    }

    public DrawerLayoutToggleHandler(Context context, int drawerLayoutGravity) {
        this.context = context;
        this.drawerLayoutGravity = drawerLayoutGravity;
    }

    public void initialize(@NonNull BaseFragmentDrawerPanel drawerPanel, @NonNull DrawerLayout drawerLayout, @NonNull View drawerHandler) {
        initialize(drawerPanel, drawerLayout, drawerHandler, false);
    }

    public void initialize(@NonNull BaseFragmentDrawerPanel drawerPanel, @NonNull DrawerLayout drawerLayout, @NonNull View drawerHandler, boolean enableDrawerLayout) {
        this.drawerPanel = drawerPanel;
        this.drawerLayout = drawerLayout;
        this.drawerToggle = drawerHandler;
        this.drawerLayout.addDrawerListener(this);
        this.drawerToggle.setOnTouchListener(this);
        setupDrawerAutoLock();
        setupDrawerHandleDetector();
        enableDrawerLayout(enableDrawerLayout);
    }

    @Override
    public final boolean onTouch(View v, MotionEvent event) {
        return drawerToggleDetector.onTouchEvent(event);
    }

    private void setupDrawerAutoLock() {
        drawerAutoLock = new DrawerAutoLock() {
            @Override
            public void execute() {
                super.execute();
                KopiTrace.d("onDrawerStateChanged::DrawerLayout.STATE_RELOCK_EXECUTED");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                KopiTrace.d("onDrawerStateChanged::DrawerLayout.STATE_RELOCK_LEFT" + (millisUntilFinished / 1000.00) + "s");
            }

            @Override
            public void onFinish() {
                enableDrawerLayout(false);
                KopiTrace.d("onDrawerStateChanged::DrawerLayout.STATE_RELOCK_COMPLETED");
            }
        };
    }

    private void setupDrawerHandleDetector() {
        drawerToggleDetector = new DrawerToggleDetector(context) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                toggleDrawerState();
                KopiTrace.d("onSingleTapConfirmed");
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                unlockDrawerLayout();
                KopiTrace.d("onDoubleTap");
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                unlockDrawerLayout();
                KopiTrace.d("onLongPress");
                super.onLongPress(e);
            }
        };
    }

    public void enableAutoLock(boolean enable) {
        this.hasAutoLock = enable;
    }

    public boolean hasAutoLock() {
        return hasAutoLock;
    }

    @Override
    public void toggleDrawerState() {
        final int gravity = drawerLayoutGravity;
        if (drawerLayout.isDrawerOpen(gravity)) {
            drawerLayout.closeDrawer(gravity);
        } else {
            drawerLayout.openDrawer(gravity);
        }
    }

    @Override
    public void enableDrawerLayout(boolean enable) {
        if (enable) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        if (slideOffset * drawerView.getWidth() >= drawerToggle.getWidth()) {
            hideDrawerHandler();
        } else {
            showDrawerHandler();
        }
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        KopiTrace.d("onDrawerOpened");
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        KopiTrace.d("onDrawerClosed");
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        switch (newState) {
            case DrawerLayout.STATE_IDLE:
                if (hasAutoLock()) {
                    if (drawerLayout.isDrawerOpen(drawerLayoutGravity)) {
                        enableDrawerLayout(true);
                    } else {
                        enableDrawerLayout(false);
                    }
                }
                KopiTrace.d("onDrawerStateChanged::DrawerLayout.STATE_IDLE");
                break;
            case DrawerLayout.STATE_DRAGGING:
                KopiTrace.d("onDrawerStateChanged::DrawerLayout.STATE_DRAGGING");
                if (drawerAutoLock.isRunning()) {
                    drawerAutoLock.cancel();
                }
                break;
            case DrawerLayout.STATE_SETTLING:
                KopiTrace.d("onDrawerStateChanged::DrawerLayout.STATE_SETTLING");
                break;
        }
    }

    public void showDrawerHandler() {
        drawerToggle.setVisibility(View.VISIBLE);
        drawerPanel.hideDrawerToggle();
    }

    public void hideDrawerHandler() {
        drawerToggle.setVisibility(View.INVISIBLE);
        drawerPanel.showDrawerToggle();
    }

    public void unlockDrawerLayout() {
        drawerToggle.startAnimation(getUnlockDrawerAnimation());
    }

    public AlphaAnimation getUnlockDrawerAnimation() {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        alphaAnimation.setDuration(450);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(3);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(getUnlockDrawerAnimationListener());
        return alphaAnimation;
    }

    public Animation.AnimationListener getUnlockDrawerAnimationListener() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                enableDrawerLayout(true);
                if (hasAutoLock()) {
                    drawerAutoLock.execute();
                }
            }
        };
    }
}
