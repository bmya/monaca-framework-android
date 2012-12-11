package mobi.monaca.framework.nativeui.component;

import static mobi.monaca.framework.nativeui.UIUtil.buildColor;
import static mobi.monaca.framework.nativeui.UIUtil.buildOpacity;
import static mobi.monaca.framework.nativeui.UIUtil.dip2px;
import static mobi.monaca.framework.nativeui.UIUtil.updateJSONObject;
import mobi.monaca.framework.nativeui.UIContext;

import org.json.JSONObject;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class LabelComponent implements ToolbarComponent {

    protected UIContext context;
    protected JSONObject style;
    protected TextView view;

    public LabelComponent(UIContext context, JSONObject style) {
        this.context = context;
        this.style = style != null ? style : new JSONObject();
        this.view = new TextView(context);
        this.view.setGravity(Gravity.CENTER_VERTICAL);

        style();
    }

    public void updateStyle(JSONObject update) {
        updateJSONObject(style, update);
        style();
    }

    public JSONObject getStyle() {
        return style;
    }

    public View getView() {
        return view;
    }

    protected void style() {
        view.setBackgroundColor(0x00000000);

        if (style.has("text")) {
            view.setText(style.optString("text", ""));
        }
        view.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE
                : View.INVISIBLE);
        view.setTextColor(buildColor(style.optString("textColor", "#ffffff")));
        view.setTextColor(view.getTextColors().withAlpha(
                buildOpacity(style.optDouble("opacity", 1.0))));
        view.setShadowLayer(1f, 0f, -1f, 0xcc000000);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getFontSizeFromDip(Component.LABEL_TEXT_DIP));

        view.setPadding(dip2px(context, 3), dip2px(context, 4),
                dip2px(context, 3), dip2px(context, 4));

        if (style.has("backgroundColor")) {
            view.setBackgroundColor(buildColor(style.optString(
                    "backgroundColor", "#ffffff")));
            view.getBackground().setAlpha(
                    buildOpacity(style.optDouble("opacity", 1.0)));
        } else {
            view.setBackgroundColor(0);
        }
    }
}
