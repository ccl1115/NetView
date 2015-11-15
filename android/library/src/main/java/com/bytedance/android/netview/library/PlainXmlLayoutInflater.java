package com.bytedance.android.netview.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yulu
 */
public class PlainXmlLayoutInflater {

    private static final String TAG_MERGE = "merge";

    private XmlPullParserFactory mXmlPullParserFactory;
    private Context mContext;

    private final Object[] mConstructorArgs = new Object[1];

    static final Class<?>[] mConstructorSignature = new Class[]{Context.class};

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<>();

    private LayoutInflater.Filter mFilter = new LayoutInflater.Filter() {
        private InflaterAllowedList mInflaterAllowedList = new InflaterAllowedList();

        @Override
        public boolean onLoadClass(Class clazz) {
            return mInflaterAllowedList.allowed(clazz.getName());
        }
    };


    private Map<String, Boolean> mFilterMap = new HashMap<>();
    private String[] sClassPrefixList = new String[]{
            "android.view.",
            "android.widget.",
            "android.widget."
    };

    public PlainXmlLayoutInflater(Context context) {
        mContext = context.getApplicationContext();
        try {
            mXmlPullParserFactory = XmlPullParserFactory.newInstance();
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
            XmlPullParser xmlParser;
            try {
                xmlParser = new SlowParser(mContext.getResources(), mXmlPullParserFactory.newPullParser());
            } catch (XmlPullParserException e) {
                InflateException inflateException = new InflateException(e.getMessage());
                inflateException.initCause(e);
                throw inflateException;
            }
            Context lastContext = (Context) mConstructorArgs[0];
            AttributeSet attrs = (AttributeSet) xmlParser;
            mConstructorArgs[0] = inflaterContext;
            View result = root;

            try {
                int type;
                xmlParser.setInput(reader);

                while ((type = xmlParser.next()) != XmlPullParser.START_TAG &&
                        type != XmlPullParser.END_DOCUMENT) {
                    // Empty
                }

                if (type != XmlPullParser.START_TAG) {
                    throw new InflateException(xmlParser.getPositionDescription()
                            + ": No start tag found!");
                }

                final String name = xmlParser.getName();

                if (TAG_MERGE.equals(name)) {
                    if (root == null || !attachToRoot) {
                        throw new InflateException("<merge /> can be used only with a valid "
                                + "ViewGroup root and attachToRoot=true");
                    }

                    rInflate(xmlParser, root, inflaterContext, attrs);
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
                    rInflateChildren(xmlParser, temp, attrs);


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
                        new InflateException(xmlParser.getPositionDescription()
                                + ": " + e.getMessage());
                inflateException.initCause(e);
                throw inflateException;
            } finally {
                // Don't retain static reference on context.
                mConstructorArgs[0] = lastContext;
            }
            return result;
        }
    }

    private void rInflateChildren(XmlPullParser parser, View parent, AttributeSet attrs)
            throws IOException, XmlPullParserException {
        rInflate(parser, parent, parent.getContext(), attrs);
    }

    private void rInflate(XmlPullParser parser, View parent, Context context, AttributeSet attrs)
            throws IOException, XmlPullParserException {

        final int depth = parser.getDepth();
        int type;

        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = parser.getName();

            if (TAG_MERGE.equals(name)) {
                throw new InflateException("<merge /> must be the root element");
            } else {
                final View view = createViewFromTag(parent, name, context, attrs);
                final ViewGroup viewGroup = (ViewGroup) parent;
                final ViewGroup.LayoutParams params = LayoutParamsGenerator.Dispatch.generate(parent.getClass().getName(), attrs);
                rInflateChildren(parser, view, attrs);
                viewGroup.addView(view, params);
            }
        }

        //if (finishInflate) {
        //    parent.onFinishInflate();
        //}
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

    protected View onCreateView(String name, AttributeSet attrs)
            throws ClassNotFoundException {
        for (String prefix : sClassPrefixList) {
            try {
                return createView(name, prefix, attrs);
            } catch (ClassNotFoundException e) {
                // In this case we want to let the base class take a crack
                // at it.
            }
        }
        return null;
    }

    /**
     * Version of {@link #onCreateView(String, AttributeSet)} that also
     * takes the future parent of the view being constructed.  The default
     * implementation simply calls {@link #onCreateView(String, AttributeSet)}.
     *
     * @param parent The future parent of the returned view.  <em>Note that
     *               this may be null.</em>
     * @param name   The fully qualified class name of the View to be create.
     * @param attrs  An AttributeSet of attributes to apply to the View.
     * @return View The View created.
     */
    protected View onCreateView(View parent, String name, AttributeSet attrs)
            throws ClassNotFoundException {
        return onCreateView(name, attrs);
    }

    private View createView(String name, String prefix, AttributeSet attrs) throws ClassNotFoundException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Class<? extends View> clazz = null;

        try {

            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                clazz = Class.forName(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                if (mFilter != null) {
                    boolean allowed = mFilter.onLoadClass(clazz);
                    if (!allowed) {
                        failNotAllowed(name, prefix, attrs);
                    }
                }
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            } else {
                // If we have a filter, apply it to cached constructor
                if (mFilter != null) {
                    // Have we seen this name before?
                    Boolean allowedState = mFilterMap.get(name);
                    if (allowedState == null) {
                        // New class -- remember whether it is allowed
                        clazz = mContext.getClassLoader().loadClass(
                                prefix != null ? (prefix + name) : name).asSubclass(View.class);

                        boolean allowed = mFilter.onLoadClass(clazz);
                        mFilterMap.put(name, allowed);
                        if (!allowed) {
                            failNotAllowed(name, prefix, attrs);
                        }
                    } else if (allowedState.equals(Boolean.FALSE)) {
                        failNotAllowed(name, prefix, attrs);
                    }
                }
            }

            Object[] args = mConstructorArgs;

            return constructor.newInstance(args);

        } catch (NoSuchMethodException e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class "
                    + (prefix != null ? (prefix + name) : name));
            ie.initCause(e);
            throw ie;

        } catch (ClassCastException e) {
            // If loaded class is not a View subclass
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Class is not a View "
                    + (prefix != null ? (prefix + name) : name));
            ie.initCause(e);
            throw ie;
        } catch (ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            throw e;
        } catch (Exception e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class "
                    + (clazz == null ? "<unknown>" : clazz.getName()));
            ie.initCause(e);
            throw ie;
        }
    }

    private void failNotAllowed(String name, String prefix, AttributeSet attrs) {

    }

}
