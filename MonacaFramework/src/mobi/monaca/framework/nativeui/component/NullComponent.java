package mobi.monaca.framework.nativeui.component;

import java.util.Set;

import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public class NullComponent extends ToolbarComponent {

    protected View view;

    public NullComponent(Context context) throws NativeUIException {
	super(new JSONObject());
	view = new FrameLayout(context);
	view.setVisibility(View.GONE);
    }

    public View getView() {
        return view;
    }

    public void updateStyle(JSONObject style) {
    }

	@Override
	public String[] getValidKeys() {
		return null;
	}

	@Override
	public String getComponentName() {
		return "NULL";
	}

	@Override
	public JSONObject getDefaultStyle() {
		return null;
	}

}
