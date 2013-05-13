package mobi.monaca.framework.nativeui.component.view;

import static mobi.monaca.framework.nativeui.UIUtil.buildOpacity;
import static mobi.monaca.framework.nativeui.UIUtil.getFontSizeFromDip;
import static mobi.monaca.framework.nativeui.UIUtil.updateJSONObject;
import mobi.monaca.framework.nativeui.UIValidator;
import mobi.monaca.framework.nativeui.component.ButtonBackgroundDrawable;
import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MonacaTextButton extends Button {
    protected JSONObject style;
    protected Context context;

    public MonacaTextButton(Context context) {
        super(context);
        this.context = context;
        this.style = new JSONObject();
    }

    public MonacaTextButton(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        this.style = buildStyleFromAttributeSet(attr);
    }

    protected JSONObject buildStyleFromAttributeSet(AttributeSet attr) {
        JSONObject style = new JSONObject();
        String value = attr.getAttributeValue(
                "http://schemas.android.com/apk/res/android", "text");
        if (value != null) {
            if (value.startsWith("@")) {
                value = context.getResources().getString(
                        Integer.parseInt(value.substring(1), 10));
            }
            try {
                style.put("text", value);
            } catch (JSONException e) {
                // ignore
            }
        }

        return style;
    }

    public void updateStyle(JSONObject update) throws NativeUIException {
        updateJSONObject(style, update);
        style();
    }

    protected void style() throws NativeUIException {
		int activeTextColor = UIValidator.parseAndValidateColor(context, "Button style", "activeTextColor", "#999999", style);
		int textColorPressed = UIValidator.parseAndValidateColor(context, "Button style", "textColor", "#ffffff", style);

		ColorStateList textColor = new ColorStateList(new int[][] {
                new int[] { android.R.attr.state_pressed }, new int[0] },
                new int[] { activeTextColor,
                        	textColorPressed });

		int backgroundColor = UIValidator.parseAndValidateColor(context, "Button style", "backgroundColor", "#000000", style);
		ButtonBackgroundDrawable background = new ButtonBackgroundDrawable(
                context, backgroundColor);

		float opacity = UIValidator.parseAndValidateFloat(context, "Button style", "opacity", "1.0", style, 0.0f, 1.0f);
		background.setAlpha(buildOpacity(opacity));

        setBackgroundDrawable(new ButtonDrawable(background));

        setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            background.getIntrinsicHeight()
        ));

        setText(style.optString("text", ""));
        setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE
                : View.GONE);
        setTextColor(textColor);

        setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getFontSizeFromDip(context, Component.BUTTON_TEXT_DIP));

        setTextColor(0xffffffff);
        setTextColor(getTextColors().withAlpha(
                buildOpacity(opacity)));

        setEnabled(!style.optBoolean("disable", false));
        if (style.optBoolean("disable", false)) {
            setTextColor(0xff999999);
            setTextColor(getTextColors().withAlpha(
                    buildOpacity(opacity)));
        }

        setShadowLayer(1f, 0f, -1f, 0xff000000);
    }

}
