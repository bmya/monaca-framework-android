package mobi.monaca.framework.nativeui.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIValidator;
import mobi.monaca.framework.nativeui.exception.DuplicateIDException;
import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;
import mobi.monaca.framework.nativeui.exception.NativeUIIOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;

public abstract class Component {
	private static final String TAG = Component.class.getSimpleName();
	protected UIContext uiContext;
	protected JSONObject componentJSON;
	protected JSONObject style;
	private static Map<String, Component> COMPONENT_MAPS = new HashMap<String, Component>();

	public Component(UIContext uiContext, JSONObject componentJSON) throws KeyNotValidException, DuplicateIDException {
		this.uiContext = uiContext;
		this.componentJSON = componentJSON;
		String id = getComponentJSON().optString("id");
		if (!TextUtils.isEmpty(id)) {
			if (!COMPONENT_MAPS.containsKey(id)) {
				COMPONENT_MAPS.put(id, this);
			} else {
				String components[] = { COMPONENT_MAPS.get(id).getComponentName(), getComponentName() };
				DuplicateIDException exception = new DuplicateIDException(id, components);
				throw exception;
			}
		}
		mixStyleWithDefault();
		validate();
	}

	public abstract String getComponentName();

	public abstract String[] getValidKeys();

	public abstract View getView();

	public abstract void updateStyle(JSONObject update) throws NativeUIException;

	public abstract JSONObject getDefaultStyle();

	public JSONObject getComponentJSON() {
		return componentJSON;
	}

	public Map<String, Component> getComponentIdMap() {
		return COMPONENT_MAPS;
	}

	public JSONObject getStyle() {
		return style;
	}

	private void mixStyleWithDefault() {
		this.style = getComponentJSON().optJSONObject("style");
		style = style != null ? style : new JSONObject();

		JSONObject androidStyle = getComponentJSON().optJSONObject("androidStyle");
		androidStyle = androidStyle != null ? androidStyle : new JSONObject();

		JSONObject mixed = getDefaultStyle();

		Iterator<String> keys = style.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			try {
				mixed.put(key, style.get(key));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		keys = androidStyle.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			try {
				mixed.put(key, androidStyle.get(key));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		this.style = mixed;
	}

	public void validate() throws KeyNotValidException {
		validateKeyNotValid();
	}

	private void validateKeyNotValid() throws KeyNotValidException {
		UIValidator.validateKey(uiContext, getComponentName(), componentJSON, getValidKeys());
	}

	public static final int BUTTON_TEXT_DIP = 14;
	public static final int LABEL_TEXT_DIP = 14;
	public static final int TAB_TEXT_DIP = 14;
	public static final int SEGMENT_TEXT_DIP = 14;
	public static final int BIG_TITLE_TEXT_DIP = 18;
	public static final int SUBTITLE_TEXT_DIP = 12;
	public static final int TITLE_TEXT_DIP = 18;
	public static final int TAB_BADGE_TEXT_DIP = 9;
	public static final int SPINNER_TEXT_DIP = 20;

}
