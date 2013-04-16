package mobi.monaca.framework.nativeui.component;

import java.util.Set;

import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.exception.DuplicateIDException;
import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public class NullComponent extends ToolbarComponent {

	protected View view;

	public NullComponent(UIContext context) throws KeyNotValidException, DuplicateIDException {
		super(context, new JSONObject());
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
