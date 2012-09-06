package mobi.monaca.framework.nativeui.component;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public class NullComponent implements ToolbarComponent {

    protected View view;

    public NullComponent(Context context) {
        view = new FrameLayout(context);
        view.setVisibility(View.GONE);
    }

    public View getView() {
        return view;
    }

    public void updateStyle(JSONObject style) {
    }

    public JSONObject getStyle() {
        return new JSONObject();
    }

}
