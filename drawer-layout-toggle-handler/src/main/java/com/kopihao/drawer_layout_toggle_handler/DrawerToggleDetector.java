package com.kopihao.drawer_layout_toggle_handler;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Gesture detection used in {@link DrawerLayoutToggleHandler#drawerToggle}
 * Detect mainly for single tap, double tap and long press event
 */
public class DrawerToggleDetector implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat gestureDetectorCompat;

    public DrawerToggleDetector(Context context) {
        gestureDetectorCompat = new GestureDetectorCompat(context, this);
        gestureDetectorCompat.setIsLongpressEnabled(true);
        gestureDetectorCompat.setOnDoubleTapListener(this);
    }

    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }

}
