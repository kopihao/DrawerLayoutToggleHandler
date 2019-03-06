package com.kopirealm.drawer_layout_toggle_handler.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kopirealm.drawer_layout_toggle_handler.BaseFragmentDrawerPanel;
import com.kopirealm.drawer_layout_toggle_handler.DrawerLayoutToggleListener;
import com.kopirealm.drawer_layout_toggle_handler.R;

public class FragmentRightDrawerPanel extends BaseFragmentDrawerPanel implements View.OnClickListener {

    public static FragmentRightDrawerPanel newInstance(DrawerLayoutToggleListener l) {
        final FragmentRightDrawerPanel fragment = new FragmentRightDrawerPanel();
        fragment.setFragmentDrawerPanelListener(l);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_sample_drawer_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupDrawerToggle(view.findViewById(R.id.drawerToggle));
        view.findViewById(R.id.vSpacingTop).setOnClickListener(this);
        view.findViewById(R.id.vSpacingBottom).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vSpacingTop:
            case R.id.vSpacingBottom:
                if (getFragmentDrawerPanelListener() != null) {
                    getFragmentDrawerPanelListener().toggleDrawerState();
                }
                break;
        }
    }
}
