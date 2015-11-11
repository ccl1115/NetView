package com.bytedance.android.netview.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * @author yulu
 */
class PlainXmlLayoutInflater {

    private static final String TAG_MERGE = "merge";

    private XmlPullParser mXmlPullParser;
    private Context mContext;

    private final Object[] mConstructorArgs = new Object[2];

    protected PlainXmlLayoutInflater(Context context) {
        mContext = context;
        try {
            mXmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException ignored) {

        }
    }

    public View inflate(InputStream stream, ViewGroup root) {
        return inflate(stream, root, false);
    }

    public View inflate(InputStream stream, ViewGroup root, boolean attachToRoot) {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(stream, Charset.forName("UTF-8"));
            return inflate(in, root, attachToRoot);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    private View inflate(Reader reader, ViewGroup root, boolean attachToRoot) {
        synchronized (mConstructorArgs) {
            final Context inflaterContext = mContext;
            Context lastContext = (Context) mConstructorArgs[0];
            AttributeSet attrs = Xml.asAttributeSet(mXmlPullParser);
            mConstructorArgs[0] = inflaterContext;
            View result = root;

            if (mXmlPullParser == null) {
                return null;
            }

            try {
                XmlPullParser parser = mXmlPullParser;
                parser.setInput(reader);
                int type;

                while ((type = parser.next()) != XmlPullParser.START_TAG &&
                        type != XmlPullParser.END_DOCUMENT) {
                    // Empty
                }

                if (type != XmlPullParser.START_TAG) {
                    throw new InflateException(parser.getPositionDescription()
                            + ": No start tag found!");
                }

                final String name = parser.getName();

                if (TAG_MERGE.equals(name)) {
                    if (root == null || !attachToRoot) {
                        throw new InflateException("<merge /> can be used only with a valid "
                                + "ViewGroup root and attachToRoot=true");
                    }

                    rInflate(parser, root, inflaterContext, attrs, false);
                } else {
                    // Temp is the root view that was found in the xml
                    final View temp = createViewFromTag(root, name, inflaterContext, attrs);

                    ViewGroup.LayoutParams params = null;

                    if (root != null) {
                        // Create layout params that match root, if supplied
                        params = root.generateLayoutParams(attrs);
                        if (!attachToRoot) {
                            // Set the layout params for temp if we are not
                            // attaching. (If we are, we use addView, below)
                            temp.setLayoutParams(params);
                        }
                    }


                    // Inflate all children under temp against its context.
                    rInflateChildren(parser, temp, attrs, true);


                    // We are supposed to attach all the views we found (int temp)
                    // to root. Do that now.
                    if (root != null && attachToRoot) {
                        root.addView(temp, params);
                    }

                    // Decide whether to return the root that was passed in or the
                    // top view found in xml.
                    if (root == null || !attachToRoot) {
                        result = temp;
                    }
                }

            } catch (XmlPullParserException e) {
                InflateException inflateException = new InflateException(e.getMessage());
                inflateException.initCause(e);
                throw inflateException;
            } catch (Exception e) {
                InflateException inflateException =
                        new InflateException(mXmlPullParser.getPositionDescription()
                                + ": " + e.getMessage());
                inflateException.initCause(e);
                throw inflateException;
            } finally {
                // Don't retain static reference on context.
                mConstructorArgs[0] = lastContext;
                mConstructorArgs[1] = null;
            }
            return result;
        }
    }

    private void rInflateChildren(XmlPullParser parser, View temp, AttributeSet attrs, boolean attach) {

    }

    private View createViewFromTag(View parent, String name, Context context, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            View view = null;

            final Object lastContext = mConstructorArgs[0];
            mConstructorArgs[0] = context;
            try {
                if (-1 == name.indexOf('.')) {
                    view = onCreateView(parent, name, attrs);
                } else {
                    view = createView(name, null, attrs);
                }
            } finally {
                mConstructorArgs[0] = lastContext;
            }

            return view;
        } catch (InflateException e) {
            throw e;

        } catch (Exception e) {
            final InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name);
            ie.initCause(e);
            throw ie;
        }
    }

    private View createView(String name, String prefix, AttributeSet attrs) {
        return null;
    }

    private View onCreateView(View parent, String name, AttributeSet attrs) {
        return null;
    }

    private void rInflate(XmlPullParser parser, View root, Context context, AttributeSet attrs, boolean attach) {

    }
}
