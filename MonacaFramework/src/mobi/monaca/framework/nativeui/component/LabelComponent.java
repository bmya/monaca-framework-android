package mobi.monaca.framework.nativeui.component;

import static mobi.monaca.framework.nativeui.UIUtil.buildOpacity;
import static mobi.monaca.framework.nativeui.UIUtil.dip2px;
import static mobi.monaca.framework.nativeui.UIUtil.updateJSONObject;
import mobi.monaca.framework.nativeui.DefaultStyleJSON;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIValidator;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class LabelComponent extends ToolbarComponent {

	protected TextView view;
	protected static final String[] LABEL_VALID_KEYS = { "style","iosStyle", "androidStyle", "component", "id", };
	protected static final String[] STYLE_VALID_KEYS = { "opacity", "textColor", "text" };

	public LabelComponent(UIContext context, JSONObject labelJSON) throws NativeUIException, JSONException {
		super(context, labelJSON);
		this.view = new TextView(context);
		this.view.setGravity(Gravity.CENTER_VERTICAL);

		style();
	}

	public void updateStyle(JSONObject update) throws NativeUIException {
		updateJSONObject(style, update);
		style();
	}

	public View getView() {
		return view;
	}

	protected void style() throws NativeUIException {
		view.setBackgroundColor(0x00000000);

		if (style.has("text")) {
			view.setText(style.optString("text", ""));
		}
		view.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE : View.INVISIBLE);

		int textColor = UIValidator.parseAndValidateColor(uiContext, getComponentName() + " style", "textColor", "#ffffff", style);
		view.setTextColor(textColor);
		float opacity = UIValidator.parseAndValidateFloat(uiContext, getComponentName() + " style", "opacity", "1.0", style, 0.0f, 1.0f);
		view.setTextColor(view.getTextColors().withAlpha(buildOpacity(opacity)));
		view.setShadowLayer(1f, 0f, -1f, 0xcc000000);
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, uiContext.getFontSizeFromDip(Component.LABEL_TEXT_DIP));

		view.setPadding(dip2px(uiContext, 3), dip2px(uiContext, 4), dip2px(uiContext, 3), dip2px(uiContext, 4));

		if (style.has("backgroundColor")) {
			int backgroundColor = UIValidator.parseAndValidateColor(uiContext, getComponentName() + " style", "backgroundColor", "#ffffff", style);
			view.setBackgroundColor(backgroundColor);
			view.getBackground().setAlpha(buildOpacity(opacity));
		} else {
			view.setBackgroundColor(0);
		}
	}

	@Override
	public String getComponentName() {
		return "Label";
	}

	@Override
	public JSONObject getDefaultStyle() {
		return DefaultStyleJSON.label();
	}

	@Override
	public String[] getValidKeys() {
		return LABEL_VALID_KEYS;
	}
}
