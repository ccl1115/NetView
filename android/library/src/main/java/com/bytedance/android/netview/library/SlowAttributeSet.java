package com.bytedance.android.netview.library;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;

import org.xmlpull.v1.XmlPullParser;

import java.util.FormatFlagsConversionMismatchException;

/**
 * Parse attributes in a slow way. Just slower than the Android implementation.
 * @author yulu
 */
class SlowAttributeSet implements AttributeSet {

    private Resources mRes;
    private final XmlPullParser mParser;

    public SlowAttributeSet(Resources res, XmlPullParser parser) {
        mRes = res;
        mParser = parser;
    }

    @Override
    public int getAttributeCount() {
        return mParser.getAttributeCount();
    }

    @Override
    public String getAttributeName(int index) {
        return mParser.getAttributeName(index);
    }

    @Override
    public String getAttributeValue(int index) {
        return mParser.getAttributeValue(index);
    }

    @Override
    public String getAttributeValue(String namespace, String name) {
        return mParser.getAttributeValue(namespace, name);
    }

    @Override
    public String getPositionDescription() {
        return mParser.getPositionDescription();
    }

    @Override
    public int getAttributeNameResource(int index) {
        return 0;
    }

    @Override
    public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
        return convertValueToList(getAttributeValue(namespace, attribute), options, defaultValue);
    }

    @Override
    public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
        return convertValueToBoolean(getAttributeValue(namespace, attribute), defaultValue);
    }

    @Override
    public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
        String value = getAttributeValue(namespace, attribute);
        if (value.startsWith("@")) {
            value = value.substring(1);
            String[] split = value.split("/");
            return mRes.getIdentifier(split[1], split[0], null);
        } else {
            return defaultValue;
        }
    }

    @Override
    public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
        return convertValueToInt(getAttributeValue(namespace, attribute), defaultValue);
    }

    @Override
    public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
        return convertValueToUnsignedInt(getAttributeValue(namespace, attribute), defaultValue);
    }

    @Override
    public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
        String s = getAttributeValue(namespace, attribute);
        if (s != null) {
            return Float.parseFloat(s);
        }
        return defaultValue;
    }

    @Override
    public int getAttributeListValue(int index, String[] options, int defaultValue) {
        return convertValueToList(getAttributeValue(index), options, defaultValue);
    }

    @Override
    public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
        return convertValueToBoolean(getAttributeValue(index), defaultValue);
    }

    @Override
    public int getAttributeResourceValue(int index, int defaultValue) {
        String value = getAttributeValue(index);
        if (value.startsWith("@")) {
            value = value.substring(1);
            String[] split = value.split("/");
            return mRes.getIdentifier(split[1], split[0], null);
        } else {
            return defaultValue;
        }
    }

    @Override
    public int getAttributeIntValue(int index, int defaultValue) {
        return convertValueToInt(getAttributeValue(index), defaultValue);
    }

    @Override
    public int getAttributeUnsignedIntValue(int index, int defaultValue) {
        return convertValueToUnsignedInt(getAttributeValue(index), defaultValue);
    }

    @Override
    public float getAttributeFloatValue(int index, float defaultValue) {
        String s = getAttributeValue(index);
        if (s != null) {
            return Float.parseFloat(s);
        }
        return defaultValue;
    }

    @Override
    public String getIdAttribute() {
        return getAttributeValue(null, "id");
    }

    @Override
    public String getClassAttribute() {
        return getAttributeValue(null, "class");
    }

    @Override
    public int getIdAttributeResourceValue(int defaultValue) {
        return getAttributeResourceValue(null, "id", defaultValue);
    }

    @Override
    public int getStyleAttribute() {
        return getAttributeResourceValue(null, "style", 0);
    }

    private static int convertValueToList(CharSequence value, String[] options, int defaultValue)
    {
        if (null != value) {
            for (int i = 0; i < options.length; i++) {
                if (value.equals(options[i]))
                    return i;
            }
        }

        return defaultValue;
    }

    private static boolean convertValueToBoolean(CharSequence value, boolean defaultValue)
    {
        boolean result = false;

        if (null == value)
            return defaultValue;

        if (value.equals("1")
                ||  value.equals("true")
                ||  value.equals("TRUE"))
            result = true;

        return result;
    }

    private static int convertValueToInt(CharSequence charSeq, int defaultValue)
    {
        if (null == charSeq)
            return defaultValue;

        String nm = charSeq.toString();

        // XXX This code is copied from Integer.decode() so we don't
        // have to instantiate an Integer!

        int sign = 1;
        int index = 0;
        int len = nm.length();
        int base = 10;

        if ('-' == nm.charAt(0)) {
            sign = -1;
            index++;
        }

        if ('0' == nm.charAt(index)) {
            //  Quick check for a zero by itself
            if (index == (len - 1))
                return 0;

            char    c = nm.charAt(index + 1);

            if ('x' == c || 'X' == c) {
                index += 2;
                base = 16;
            } else {
                index++;
                base = 8;
            }
        }
        else if ('#' == nm.charAt(index))
        {
            index++;
            base = 16;
        }

        return Integer.parseInt(nm.substring(index), base) * sign;
    }

    private static int convertValueToUnsignedInt(String value, int defaultValue) {
        if (null == value) {
            return defaultValue;
        }

        return parseUnsignedIntAttribute(value);
    }

    private static int parseUnsignedIntAttribute(CharSequence charSeq) {
        String  value = charSeq.toString();

        int     index = 0;
        int     len = value.length();
        int     base = 10;

        if ('0' == value.charAt(index)) {
            //  Quick check for zero by itself
            if (index == (len - 1))
                return 0;

            char    c = value.charAt(index + 1);

            if ('x' == c || 'X' == c) {     //  check for hex
                index += 2;
                base = 16;
            } else {                        //  check for octal
                index++;
                base = 8;
            }
        } else if ('#' == value.charAt(index)) {
            index++;
            base = 16;
        }

        return (int) Long.parseLong(value.substring(index), base);
    }
}
