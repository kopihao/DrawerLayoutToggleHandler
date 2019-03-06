package com.kopihao.drawerLayoutToggleHandler.util;

import android.util.Log;

import com.example.drawerLayoutToggleHandler.BuildConfig;

public class KopiTrace {

    public static void d(String msg) {
        if (!BuildConfig.DEBUG) return;
        Log.d("kopihao", msg);
    }

}
