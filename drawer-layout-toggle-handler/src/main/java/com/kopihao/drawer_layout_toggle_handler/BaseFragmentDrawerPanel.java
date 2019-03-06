package com.kopihao.drawer_layout_toggle_handler;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Base fragment which will be hosted by drawer layout
 */
public class BaseFragmentDrawerPanel extends Fragment {

    private View drawerToggle;
    private DrawerLayoutToggleListener fragmentDrawerPanelListener;

    public void setupDrawerToggle(View toggle) {
        this.drawerToggle = toggle;
        this.drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == getDrawerToggle().getId()) {
                    if (fragmentDrawerPanelListener != null) {
                        fragmentDrawerPanelListener.toggleDrawerState();
                    }
                }
            }
        });
    }

    public void setFragmentDrawerPanelListener(DrawerLayoutToggleListener l) {
        fragmentDrawerPanelListener = l;
    }

    public void showDrawerToggle() {
        if (getDrawerToggle() == null) return;
        drawerToggle.setVisibility(View.VISIBLE);
    }

    public void hideDrawerToggle() {
        if (getDrawerToggle() == null) return;
        drawerToggle.setVisibility(View.INVISIBLE);
    }

    public View getDrawerToggle() {
        return drawerToggle;
    }

    public DrawerLayoutToggleListener getFragmentDrawerPanelListener() {
        return fragmentDrawerPanelListener;
    }
}
