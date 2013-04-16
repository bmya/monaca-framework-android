package mobi.monaca.framework.nativeui.component;

import mobi.monaca.framework.nativeui.DefaultStyleJSON;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.exception.DuplicateIDException;
import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONException;
import org.json.JSONObject;

public class BackButtonComponent extends ButtonComponent {
	protected static final String[] STYLE_VALID_KEYS = { "visibility", "disable", "opacity", "backgroundColor", "activeTextColor", "textColor", "image", "innerImage", "text", "forceVisibility" };
	
	public BackButtonComponent(UIContext context, JSONObject buttonJSON) throws NativeUIException {
		super(context, buttonJSON);
		try {
			style.put("visibility", style.optBoolean("forceVisibility", false));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		style();
		PageComponent.BACK_BUTTON_EVENTER = this.eventer;
	}
	
	@Override
	protected String[] getStyleValidKeys() {
		return STYLE_VALID_KEYS;
	}

	@Override
	public String getComponentName() {
		return "BackButton";
	}

	@Override
	public JSONObject getDefaultStyle() {
		return DefaultStyleJSON.backButton();
	}

	@Override
	protected void style() throws NativeUIException {
		super.style();
	}

}
