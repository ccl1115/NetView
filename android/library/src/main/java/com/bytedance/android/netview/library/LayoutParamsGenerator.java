package com.bytedance.android.netview.library;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author yulu
 */
interface LayoutParamsGenerator {
    String WRAP_CONTENT = "wrap_content";
    String MATCH_PARENT = "match_parent";

    ViewGroup.LayoutParams gen(ViewGroup.LayoutParams parent, AttributeSet attrs);

    String key();

    class Dispatch {
        private static Node<LayoutParamsGenerator> sGeneratorTree;

        static {
            sGeneratorTree = new Node<LayoutParamsGenerator>(new ViewGroupGenerator());
            Node<LayoutParamsGenerator> marginNode = new Node<LayoutParamsGenerator>(new MarginLayoutParamGenerator());
            sGeneratorTree.addChild(marginNode);
            marginNode.addChild(new Node<LayoutParamsGenerator>(new LinearLayoutParamGenerator()));
            marginNode.addChild(new Node<LayoutParamsGenerator>(new FrameLayoutParamsGenerator()));
        }

        private static final Stack<Node<LayoutParamsGenerator>> STACK = new Stack<>();

        static ViewGroup.LayoutParams generate(String clz, AttributeSet attrs) {
            Node<LayoutParamsGenerator> node = sGeneratorTree.find(clz);
            if (node == null) {
                return null;
            }
            STACK.clear();
            while (node != null) {
                STACK.push(node);
                node = node.parent;
            }

            ViewGroup.LayoutParams lp = null;
            while (!STACK.empty()) {
                node = STACK.pop();
                lp = node.value.gen(lp, attrs);
            }

            return lp;
        }

        /**
         * Dimension string to pixel value
         *
         * @param s dimension like 10dp, 8sp, 22px
         * @return the value of pixel
         */
        private static int d2i(String s) {
            if (s == null) {
                return 0;
            }
            int ret = 0;
            if (WRAP_CONTENT.equals(s)) {
                ret = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else if (MATCH_PARENT.equals(s)) {
                ret = ViewGroup.LayoutParams.MATCH_PARENT;
            } else if (s.length() > 2) {
                String unit = s.substring(s.length() - 3, s.length() - 1);
                float value = Float.parseFloat(s.substring(0, s.length() - 4));
                int type = 0;
                switch (unit) {
                    case "dp":
                        type = TypedValue.COMPLEX_UNIT_DIP;
                        break;
                    case "sp":
                        type = TypedValue.COMPLEX_UNIT_PX;
                        break;
                    case "px":
                        type = TypedValue.COMPLEX_UNIT_PX;
                        break;
                    case "mm":
                        type = TypedValue.COMPLEX_UNIT_MM;
                        break;
                    case "in":
                        type = TypedValue.COMPLEX_UNIT_IN;
                        break;
                    case "pt":
                        type = TypedValue.COMPLEX_UNIT_PT;
                        break;
                }

                return (int) (TypedValue.applyDimension(type, value, NetView.sResources.getDisplayMetrics()) + 0.5f);
            }
            return ret;
        }

        /**
         * Gravity string to int value
         *
         * @param s gravity string like TOP, LEFT
         * @return the value of gravity
         */
        private static int g2i(String s) {
            if (s == null) {
                return Gravity.NO_GRAVITY;
            }
            String[] split = s.split("|");
            int ret = Gravity.NO_GRAVITY;
            for (String i : split) {
                switch (i) {
                    case "left":
                        ret |= Gravity.LEFT;
                        break;
                    case "top":
                        ret |= Gravity.TOP;
                        break;
                    case "right":
                        ret |= Gravity.RIGHT;
                        break;
                    case "bottom":
                        ret |= Gravity.BOTTOM;
                        break;
                    case "center":
                        ret |= Gravity.CENTER;
                        break;
                    case "center_horizontal":
                        ret |= Gravity.CENTER_HORIZONTAL;
                        break;
                    case "center_vertical":
                        ret |= Gravity.CENTER_VERTICAL;
                        break;
                    case "clip_horizontal":
                        ret |= Gravity.CLIP_HORIZONTAL;
                        break;
                    case "clip_vertical":
                        ret |= Gravity.CLIP_VERTICAL;
                        break;
                    case "fill":
                        ret |= Gravity.FILL;
                        break;
                    case "fill_horizontal":
                        ret |= Gravity.FILL_HORIZONTAL;
                        break;
                    case "fill_vertical":
                        ret |= Gravity.FILL_VERTICAL;
                        break;
                    case "start":
                        ret |= Gravity.START;
                        break;
                    case "end":
                        ret |= Gravity.END;
                        break;
                }
            }
            return ret;
        }

        private static class ViewGroupGenerator implements LayoutParamsGenerator {

            @Override
            public ViewGroup.LayoutParams gen(ViewGroup.LayoutParams parent, AttributeSet attrs) {
                final String lw = attrs.getAttributeValue(null, "layout_width");
                final String lh = attrs.getAttributeValue(null, "layout_height");
                int width = d2i(lw);
                int height = d2i(lh);
                return new ViewGroup.LayoutParams(width, height);
            }

            @Override
            public String key() {
                return "android.view.ViewGroup";
            }

        }

        private static class MarginLayoutParamGenerator implements LayoutParamsGenerator {

            @Override
            public ViewGroup.MarginLayoutParams gen(ViewGroup.LayoutParams parent, AttributeSet attrs) {
                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(parent);
                int margin = d2i(attrs.getAttributeValue(null, "layout_margin"));
                if (margin != 0) {
                    layoutParams.leftMargin = margin;
                    layoutParams.rightMargin = margin;
                    layoutParams.topMargin = margin;
                    layoutParams.bottomMargin = margin;
                } else {
                    layoutParams.leftMargin = d2i(attrs.getAttributeValue(null, "layout_marginLeft"));
                    layoutParams.topMargin = d2i(attrs.getAttributeValue(null, "layout_marginTop"));
                    layoutParams.rightMargin = d2i(attrs.getAttributeValue(null, "layout_marginRight"));
                    layoutParams.bottomMargin = d2i(attrs.getAttributeValue(null, "layout_marginBottom"));

                }

                return layoutParams;
            }

            @Override
            public String key() {
                return "android.view.ViewGroup";
            }
        }

        private static class LinearLayoutParamGenerator implements LayoutParamsGenerator {

            @SuppressWarnings("ResourceType")
            @Override
            public ViewGroup.LayoutParams gen(ViewGroup.LayoutParams parent, AttributeSet attrs) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(parent);
                layoutParams.weight = attrs.getAttributeFloatValue(null, "layout_weight", 0);
                layoutParams.gravity = g2i(attrs.getAttributeValue(null, "layout_gravity"));
                return layoutParams;
            }

            @Override
            public String key() {
                return "android.widget.LinearLayout";
            }
        }

        private static class FrameLayoutParamsGenerator implements LayoutParamsGenerator {

            @Override
            public ViewGroup.LayoutParams gen(ViewGroup.LayoutParams parent, AttributeSet attrs) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(parent);
                layoutParams.gravity = g2i(attrs.getAttributeValue(null, "layout_gravity"));
                return layoutParams;
            }

            @Override
            public String key() {
                return "android.widget.FrameLayout";
            }
        }

        private static class Node<T extends LayoutParamsGenerator> {
            private T value;
            private Node<T> parent;

            private List<Node<T>> children;

            private Node(T value) {
                this.value = value;
            }


            private void addChild(Node<T> child) {
                if (children == null) {
                    children = new ArrayList<>();
                }

                children.add(child);
                child.parent = this;
            }

            private Node<T> find(String key) {
                if (value != null && key != null && key.equals(value.key())) {
                    return this;
                } else if (children != null) {
                    for (Node<T> n : children) {
                        Node<T> node = n.find(key);
                        if (node != null) {
                            return node;
                        }
                    }
                }
                return null;
            }
        }
    }
}
