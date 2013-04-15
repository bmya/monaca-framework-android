package mobi.monaca.framework.nativeui.component;

import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONObject;

public abstract class ToolbarComponent extends Component {

	public ToolbarComponent(JSONObject compoJSON) throws NativeUIException {
		super(compoJSON);
	}

}