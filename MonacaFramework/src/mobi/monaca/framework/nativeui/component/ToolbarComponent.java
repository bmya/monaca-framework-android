package mobi.monaca.framework.nativeui.component;

import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.exception.DuplicateIDException;
import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ToolbarComponent extends Component {

	public ToolbarComponent(UIContext uiContext, JSONObject compoJSON) throws KeyNotValidException, DuplicateIDException, JSONException {
		super(uiContext, compoJSON);
	}

}