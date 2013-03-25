package mobi.monaca.framework.nativeui.component;

import org.json.JSONObject;

import android.view.View;

public interface Component {

    public View getView();

    public void updateStyle(JSONObject update);

    public JSONObject getStyle();

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
