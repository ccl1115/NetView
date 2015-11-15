package com.bytedance.android.netview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bytedance.android.netview.library.PlainXmlLayoutInflater;

import java.io.StringBufferInputStream;

public class PlainXmlLayoutInflaterSampleActivity extends Activity {

    private String mFixture = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "              android:orientation=\"vertical\"\n" +
            "              android:layout_width=\"match_parent\"\n" +
            "              android:layout_height=\"match_parent\">\n" +
            "\n" +
            "    <TextView\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:text=\"hello world\"/>\n" +
            "    \n" +
            "</LinearLayout>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlainXmlLayoutInflater inflater = new PlainXmlLayoutInflater(this);
        setContentView(R.layout.activity_plain_xml_layout_inflater_sample);

        View view = inflater.inflate(new StringBufferInputStream(mFixture), null);
        ((FrameLayout) findViewById(android.R.id.content)).addView(view);
    }
}
