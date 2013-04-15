package mobi.monaca.framework.nativeui.container;

import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONObject;

import android.view.View;

public abstract class Container extends Component{

	public Container(JSONObject componentJSON) throws NativeUIException {
		super(componentJSON);
	}

	public abstract View getView();
	public abstract View getShadowView();
	public abstract boolean isTransparent();

}
