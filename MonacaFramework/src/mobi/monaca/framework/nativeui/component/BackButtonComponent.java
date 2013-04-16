package mobi.monaca.framework.nativeui.component;

import mobi.monaca.framework.nativeui.DefaultStyleJSON;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.exception.DuplicateIDException;
import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONException;
import org.json.JSONObject;

public class BackButtonComponent extends ButtonComponent {

	public BackButtonComponent(UIContext context, JSONObject buttonJSON) throws NativeUIException {
		super(context, buttonJSON);
		// TODO: how to validate valid keys? is it same as Button but with forceVisibility?
		try {
			style.put("visibility", style.optBoolean("forceVisibility", false));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		style();
		PageComponent.BACK_BUTTON_EVENTER = this.eventer;
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
