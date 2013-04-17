package mobi.monaca.framework.nativeui;

import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.exception.DuplicateIDException;
import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ComponentEventer extends Component{
    protected JSONObject event;
    protected UIContext context;
    protected static final String[] VALID_KEYS = {"onTap", "onSearch", "onChange"}; 

    public ComponentEventer(UIContext context, JSONObject event) throws NativeUIException, JSONException {
    	super(context, event);
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

	@Override
	public String getComponentName() {
		return ComponentEventer.class.getSimpleName();
	}

	@Override
	public String[] getValidKeys() {
		return VALID_KEYS;
	}

	@Override
	public View getView() {
		return null;
	}

	@Override
	public void updateStyle(JSONObject update) throws NativeUIException {
		
	}

	@Override
	public JSONObject getDefaultStyle() {
		return null;
	}
}
