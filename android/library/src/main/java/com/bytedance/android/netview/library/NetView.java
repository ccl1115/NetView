package com.bytedance.android.netview.library;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author yulu
 */
public class NetView {

    static Context sContext;
    static Resources sResources;

    public static void init(Context context) {
        sContext = context;
        sResources = context.getResources();
    }
}
