package com.kopihao.drawerLayoutToggleHandler.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.kopihao.drawerLayoutToggleHandler.DrawerLayoutToggleHandler;
import com.kopihao.drawerLayoutToggleHandler.DrawerLayoutToggleListener;
import com.kopihao.drawerLayoutToggleHandler.R;

public class MainActivity extends AppCompatActivity {

    private FragmentRightDrawerPanel drawerPanel;
    private DrawerLayoutToggleHandler drawerLayoutHandler;
    private DrawerLayout drawerLayout;
    private ImageView drawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.dlDrawerParent);
        drawerToggle = findViewById(R.id.drawerToggle);
        drawerPanel = FragmentRightDrawerPanel.newInstance(new DrawerLayoutToggleListener() {
            @Override
            public void toggleDrawerState() {
                if (drawerLayoutHandler != null) {
                    drawerLayoutHandler.toggleDrawerState();
                }
            }

            @Override
            public void enableDrawerLayout(boolean enable) {
                if (drawerLayoutHandler != null) {
                    drawerLayoutHandler.enableDrawerLayout(enable);
                }
            }
        });
        drawerLayoutHandler = new DrawerLayoutToggleHandler(getApplicationContext());
        drawerLayoutHandler.initialize(drawerPanel, drawerLayout, drawerToggle, false);
        drawerLayoutHandler.enableAutoLock(true);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flDrawerContainter, drawerPanel);
        transaction.commit();
    }


}