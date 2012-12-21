package mobi.monaca.framework.nativeui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.monaca.framework.nativeui.ComponentEventer;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.psedo.R;
import mobi.monaca.framework.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import static mobi.monaca.framework.nativeui.UIUtil.*;

public class SegmentComponent implements ToolbarComponent {

    protected SegmentComponentView view;
    protected UIContext context;
    protected JSONObject style;
    protected ComponentEventer eventer;
    protected int backgroundColor = 0xff555555;
    protected int pressedBackgroundColor;

    public SegmentComponent(UIContext context, JSONObject style,
            ComponentEventer eventer) {
        this.context = context;
        this.style = style == null ? new JSONObject() : style;
        this.view = new SegmentComponentView(context);
        this.eventer = eventer;

        style();
    }

    public JSONObject getStyle() {
        return style;
    }

    public View getView() {
        return view;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected void style() {
        JSONArray texts = style.optJSONArray("texts");

        backgroundColor = buildColor(style.optString("backgroundColor",
                "#ff0000"));

        if (texts != null) {
            view.removeAllSegmentItemViews();
            for (int i = 0; i < texts.length(); i++) {
                SegmentItemView item = new SegmentItemView(context,
                        texts.optString(i), backgroundColor);
                if (i == 0) {
                    item.setAsLeft();
                } else if (i == texts.length() - 1) {
                    item.setAsRight();
                } else {
                    item.setAsCenter();
                }
                view.addSegmentItemView(item);
            }
            if (texts.length() == 1) {
                view.setAsSingle();
            }
        }

        view.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE
                : View.INVISIBLE);
        view.setDisable(style.optBoolean("disable", false));

        view.setActiveIndex(style.optInt("activeIndex", 0));
        view.updateSegmentItemsWidth();
    }

    class SegmentComponentView extends FrameLayout implements
            View.OnClickListener {

        protected boolean disabled = false;
        protected ArrayList<SegmentItemView> items = new ArrayList<SegmentItemView>();
        protected SegmentItemView currentItemView = null;
        protected LinearLayout layout;

        public SegmentComponentView(Context context) {
            super(context);

            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            addView(layout, layoutParams);
            addView(createFrameView(), layoutParams);
        }

        public View createFrameView() {
            View v = new FrameLayout(context);
            v.setBackgroundResource(R.drawable.monaca_button_frame);
            v.getBackground().setAlpha(0xcc);

            return v;
        }

        public List<SegmentItemView> getAllSegmentItems() {
            return items;
        }

        public void setAsSingle() {
            items.get(0).setAsSingle();
        }

        public void removeAllSegmentItemViews() {
            layout.removeAllViews();
            items = new ArrayList<SegmentItemView>();
        }

        public void setActiveIndex(int i) {
            if (currentItemView != null) {
                currentItemView.switchToUnselected();
            }
            if (i >= 0 && i < items.size()) {
                currentItemView = items.get(i);
                currentItemView.switchToSelected();
            } else {
                currentItemView = null;
            }
        }

        public void setDisable(boolean disabled) {
            this.disabled = disabled;
        }

        protected void addSegmentItemView(SegmentItemView itemView) {
            items.add(itemView);
            layout.addView(itemView, new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1));

            if (items.size() == 1) {
                itemView.switchToSelected();
                currentItemView = itemView;
            } else {
                itemView.switchToUnselected();
            }

            itemView.setOnClickListener(this);
        }

        protected void updateSegmentItemsWidth() {
            ArrayList<Integer> widths = new ArrayList<Integer>();
            for (SegmentItemView segment : items) {
                segment.measure(View.MeasureSpec.UNSPECIFIED,
                        View.MeasureSpec.UNSPECIFIED);
                segment.getMeasuredWidth();
                widths.add(segment.getMeasuredWidth());
            }

            int maxWidth = Collections.max(widths);
            // compensate for sdk > Honeycomb using Holo theme which make item width wider
            if(android.os.Build.VERSION.SDK_INT >= 11){
            	maxWidth = maxWidth - UIUtil.dip2px(getContext(), 15);
            }
            MyLog.e(TAG, "max width:" + maxWidth);
            for (SegmentItemView segment : items) {
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) segment
                        .getLayoutParams();
                p.width =  maxWidth;
                segment.setLayoutParams(p);
            }

            View frame = getChildAt(1);
            FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) frame
                    .getLayoutParams();
            p.width = maxWidth * items.size();
            frame.setLayoutParams(p);
        }

        @Override
        public void onClick(View v) {
            if (!this.disabled) {
                SegmentItemView item = (SegmentItemView) v;
                item.switchToSelected();

                int activeIndex = 0;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i) == item) {
                        activeIndex = i;
                        try {
                            style.put("activeIndex", i);
                        } catch (JSONException e) {
                        }
                        break;
                    }
                }

                context.react("javascript: __segment_index = " + activeIndex
                        + ";");
                if (item == currentItemView) {
                    eventer.onTap();
                } else {
                    eventer.onChange();
                }

                if (currentItemView != null && currentItemView != item) {
                    currentItemView.switchToUnselected();
                }
                currentItemView = item;

            }
        }
    }

    @Override
    public void updateStyle(JSONObject update) {
        updateJSONObject(style, update);
        style();
    }

    public class SegmentItemView extends FrameLayout {

        protected Button button;
        protected boolean isSelected = true;
        protected int tint;
        protected SegmentBackgroundDrawable background;

        public SegmentItemView(UIContext context, String title, int tint) {
            super(context);

            this.tint = tint;

            button = new Button(context);
            button.setText(title);
            button.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            button.setTextColor(0xffffffff);
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getFontSizeFromDip(Component.SEGMENT_TEXT_DIP));
            button.setShadowLayer(1f, 0f, -1f, 0xcc000000);

            addView(button, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));

            setAsSingle();
            switchToUnselected();
        }

        public void setAsLeft() {
            background = new SegmentBackgroundDrawable(context,
                    SegmentBackgroundDrawable.Type.LEFT, tint);
            button.setBackgroundDrawable(background);
        }

        public void setAsRight() {
            background = new SegmentBackgroundDrawable(context,
                    SegmentBackgroundDrawable.Type.RIGHT, tint);
            button.setBackgroundDrawable(background);
        }

        public void setAsCenter() {
            background = new SegmentBackgroundDrawable(context,
                    SegmentBackgroundDrawable.Type.CENTER, tint);
            button.setBackgroundDrawable(background);
        }

        public void setAsSingle() {
            background = new SegmentBackgroundDrawable(context,
                    SegmentBackgroundDrawable.Type.SINGLE, tint);
            button.setBackgroundDrawable(background);
        }

        @Override
        public void setOnClickListener(final View.OnClickListener listener) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(SegmentItemView.this);
                }
            });
        }

        public void switchToSelected() {
            background.setSelected(true);
            isSelected = true;
            invalidate();
            MyLog.d(getClass().getSimpleName(), "selected");
        }

        public void switchToUnselected() {
            background.setSelected(false);
            isSelected = false;
            invalidate();
            MyLog.d(getClass().getSimpleName(), "unselected");
        }

        protected void updateSwitchingEffect() {
            if (isSelected) {
                switchToSelected();
            } else {
                switchToUnselected();
            }
        }

        public boolean isSelected() {
            return isSelected;
        }
    }

}
