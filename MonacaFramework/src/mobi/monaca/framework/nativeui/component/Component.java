package mobi.monaca.framework.nativeui.component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mobi.monaca.framework.util.MyLog;

import org.json.JSONObject;

import android.view.View;

public abstract class Component {
    private static final String TAG = Component.class.getSimpleName();
    protected JSONObject componentJSON;

    public Component(JSONObject compoJSON) {
	this.componentJSON = compoJSON;
	}

	public abstract View getView();

    public abstract void updateStyle(JSONObject update);

    public abstract JSONObject getStyle();

    public JSONObject getComponentJSON(){
	return componentJSON;
    }

    public abstract Set<String> getValidKeys();

    public void validate(){
	JSONObject componentJSON = getComponentJSON();
	Set<String> validKeys = getValidKeys();
	Iterator keys = componentJSON.keys();
	if(keys.hasNext()){
		String key = (String) keys.next();
		if(!validKeys.contains(key)){
			MyLog.w(TAG, "KEY " + key + " IS NOT ONE OF VALID KEYS");
		}
	}
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
