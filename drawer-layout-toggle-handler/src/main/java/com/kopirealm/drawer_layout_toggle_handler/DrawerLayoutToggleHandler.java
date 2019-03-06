package com.kopirealm.drawer_layout_toggle_handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.kopirealm.drawer_layout_toggle_handler.util.KopiTrace;

/**
 * A helpful handler to control {@link android.support.v4.widget.DrawerLayout} with toggle
 * Toggle can be any {@link View}
 * Enhanced toggle experience with {@link android.support.v4.view.GestureDetectorCompat} and {@link android.support.v4.widget.DrawerLayout.DrawerListener}
 * Nothing fancy here but to ease setup and code lines required
 *
 * @refer {@link DrawerLayoutToggleHandler#initialize(BaseFragmentDrawerPanel, DrawerLayout, View, boolean)}
 */
public class DrawerLayoutToggleHandler implements DrawerLayoutToggleListener, DrawerLayout.DrawerListener, View.OnTouchListener {

    private Context context;
    private BaseFragmentDrawerPanel drawerPanel;
    private View drawerToggle;
    private DrawerAutoLock drawerAutoLock;
    private DrawerToggleDetector drawerToggleDetector;
    private DrawerLayout drawerLayout;
    private int drawerLayoutGravity;
    private boolean hasAutoLock = true;

    public DrawerLayoutToggleHandler(Context context) {
        this(context, GravityCompat.END);
    }

    /**
     * @param context
     * @param drawerLayoutGravity {@value GravityCompat#START} or {@value GravityCompat#END}
     */
    public DrawerLayoutToggleHandler(Context context, int drawerLayoutGravity) {
        this.context = context;
        this.drawerLayoutGravity = drawerLayoutGravity;
    }

    /**
     * Essential setup
     *
     * @param drawerPanel
     * @param drawerLayout
     * @param drawerToggle
     */
    public void initialize(@NonNull BaseFragmentDrawerPanel drawerPanel, @NonNull DrawerLayout drawerLayout, @NonNull View drawerToggle) {
        initialize(drawerPanel, drawerLayout, drawerToggle, false);
    }

    /**
     * Essential setup
     *
     * @param drawerPanel
     * @param drawerLayout
     * @param drawerToggle
     * @param enableDrawerLayout
     */
    public void initialize(@NonNull BaseFragmentDrawerPanel drawerPanel, @NonNull DrawerLayout drawerLayout, @NonNull View drawerToggle, boolean enableDrawerLayout) {
        this.drawerPanel = drawerPanel;
        this.drawerLayout = drawerLayout;
        this.drawerToggle = drawerToggle;
        this.drawerLayout.addDrawerListener(this);
        this.drawerToggle.setOnTouchListener(this);
        setupDrawerAutoLock();
        setupDrawerHandleDetector();
        enableDrawerLayout(enableDrawerLayout);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public final boolean onTouch(View v, MotionEvent event) {
        return drawerToggleDetector.onTouchEvent(event);
    }

    /**
     * Setup timer to set drawer lock mode as {@value DrawerLayout#LOCK_MODE_UNLOCKED} after desired duration
     */
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

    /**
     * Setup gesture detection on {@link DrawerLayoutToggleHandler#drawerToggle}
     */
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

    /**
     * Enabling Auto lock feature
     * To lock drawer after unlocking drawer mode via {@link DrawerLayoutToggleHandler#drawerToggle}
     *
     * @param enable
     * @see #onDrawerStateChanged(int)
     */
    public void enableAutoLock(boolean enable) {
        this.hasAutoLock = enable;
    }

    /**
     * Check auto lock feature status
     *
     * @return true if enabled
     */
    public boolean hasAutoLock() {
        return hasAutoLock;
    }

    /**
     * Expand/collapse drawer layout
     */
    @Override
    public void toggleDrawerState() {
        final int gravity = drawerLayoutGravity;
        if (drawerLayout.isDrawerOpen(gravity)) {
            drawerLayout.closeDrawer(gravity);
        } else {
            drawerLayout.openDrawer(gravity);
        }
    }

    /***
     * Manipulate {@link DrawerLayout#setDrawerLockMode(int)}
     *
     * @param enable enabling swipe gesture to control drawer layout
     */
    @Override
    public void enableDrawerLayout(boolean enable) {
        if (enable) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    /**
     * Manipulating drawer swipe magnitude to define desire drawer behavior
     * Eg. Expand the drawer if swipe more than toggle width
     *
     * @param drawerView
     * @param slideOffset
     */
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

    /**
     * Manipulating drawer state change to define desire drawer behavior
     *
     * @param newState
     */
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

    /**
     * Control toggle visibility
     */
    public void showDrawerHandler() {
        drawerToggle.setVisibility(View.VISIBLE);
        drawerPanel.hideDrawerToggle();
    }

    /**
     * Control toggle visibility
     */
    public void hideDrawerHandler() {
        drawerToggle.setVisibility(View.INVISIBLE);
        drawerPanel.showDrawerToggle();
    }

    /**
     * Control drawer unlocking behavior
     */
    public void unlockDrawerLayout() {
        final Animation animation = getUnlockDrawerAnimation();
        if (animation == null) return;
        drawerToggle.startAnimation(animation);
    }

    /**
     * Provide drawer unlocking animation
     *
     * @return blinking twice animation
     */
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

    /**
     * Define unlocking drawer animation callbacks
     *
     * @return animation listener
     */
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
