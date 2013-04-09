package mobi.monaca.framework.nativeui.component;

import mobi.monaca.framework.nativeui.ComponentEventer;
import mobi.monaca.framework.nativeui.UIContext;

import org.json.JSONException;
import org.json.JSONObject;

public class BackButtonComponent extends ButtonComponent {

    public BackButtonComponent(UIContext context, JSONObject buttonJSON) {
	super(context, buttonJSON);

        try {
            style.put("visibility", style.optBoolean("forceVisibility", false));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        style();
    }

    @Override
    protected void style() {
        super.style();
    }

}
