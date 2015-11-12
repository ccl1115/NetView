package com.bytedance.android.netview.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Only View class in this allowed list can be inflated by PlainXmlLayoutInflater
 * @author yulu
 */
public class InflaterAllowedList {

    private List<String> mAllowed = new ArrayList<>();

    {
        mAllowed.add("android.view.View");
        mAllowed.add("android.widget.TextView");
        mAllowed.add("android.widget.ImageView");
        mAllowed.add("android.view.LinearLayout");
        mAllowed.add("android.view.FrameLayout");
        mAllowed.add("android.view.RelativeLayout");
    }

    public boolean allowed(String clz) {
        return mAllowed.contains(clz);
    }
}
