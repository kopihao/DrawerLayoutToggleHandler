package com.kopirealm.drawer_layout_toggle_handler.util;

import android.util.Log;

import com.kopirealm.drawer_layout_toggle_handler.BuildConfig;

public class KopiTrace {

    public static void d(String msg) {
        if (!BuildConfig.DEBUG) return;
        Log.d("kopihao", msg);
    }

}
