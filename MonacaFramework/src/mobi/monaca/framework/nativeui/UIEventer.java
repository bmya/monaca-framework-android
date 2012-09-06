package mobi.monaca.framework.nativeui;

import org.json.JSONObject;

public class UIEventer {
    protected JSONObject event;
    protected UIContext context;

    public UIEventer(UIContext context, JSONObject event) {
        this.context = context;
        event = event != null ? event : new JSONObject();
        this.event = event;
    }

    public boolean hasOnTapBackButtonAction() {
        return !event.optString("onTapBackButton", "").equals("");
    }

    public void onTapBackButton() {
        String code = event.optString("onTapBackButton", "");

        if (!code.equals("")) {
            context.react("javascript:" + code);
        }
    }

}
