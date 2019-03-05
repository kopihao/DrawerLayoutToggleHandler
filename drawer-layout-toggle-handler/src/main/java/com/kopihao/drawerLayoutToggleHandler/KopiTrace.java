package com.kopihao.drawerLayoutToggleHandler;

import android.util.Log;

public class KopiTrace {

    static boolean LEAVE_TRACE = true;

    public static void d(String msg) {
        if (!LEAVE_TRACE) return;
        Log.d("kopihao", msg);
    }

}
