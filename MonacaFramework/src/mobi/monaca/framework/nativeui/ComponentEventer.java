package mobi.monaca.framework.nativeui;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ComponentEventer {
    protected JSONObject event;
    protected UIContext context;

    public ComponentEventer(UIContext context, JSONObject event) {
        this.context = context;
        this.event = event == null ? new JSONObject() : event;
    }

    public void onTap() {
        try {
            context.react("javascript:" + event.getString("onTap"));
        } catch (Exception e) {
        }
    }

    public void onChange() {
        try {
            context.react("javascript:" + event.getString("onChange"));
        } catch (Exception e) {
        }
    }

    public void onSearch(View view, String keyword) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        try {
            JSONObject js = new JSONObject();
            js.put("word", keyword);
            context.react(String.format(
                    "javascript:__search_text = (%s).word;", js.toString()));
            context.react("javascript:" + event.getString("onSearch"));
        } catch (Exception e) {
        }
    }
}
