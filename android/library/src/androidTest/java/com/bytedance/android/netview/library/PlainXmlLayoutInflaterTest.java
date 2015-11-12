package com.bytedance.android.netview.library;

import android.test.AndroidTestCase;
import android.view.View;

import junit.framework.TestCase;

import java.io.StringBufferInputStream;

/**
 * @author yulu
 */
public class PlainXmlLayoutInflaterTest extends AndroidTestCase {

    private PlainXmlLayoutInflater mPlainXmlLayoutInflater;

    private String fixture = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
    protected void setUp() throws Exception {
        super.setUp();
        mPlainXmlLayoutInflater = new PlainXmlLayoutInflater(getContext());
    }

    public void testInflate() throws Exception {
        View view = mPlainXmlLayoutInflater.inflate(new StringBufferInputStream(fixture), null);
        assertNotNull(view);
    }

    public void testInflate1() throws Exception {
        View view = mPlainXmlLayoutInflater.inflate(new StringBufferInputStream(fixture), null, false);
        assertNotNull(view);
    }
}