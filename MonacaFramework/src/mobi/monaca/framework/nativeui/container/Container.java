package mobi.monaca.framework.nativeui.container;

import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.exception.DuplicateIDException;
import mobi.monaca.framework.nativeui.exception.KeyNotValidException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;

import org.json.JSONObject;

import android.view.View;

public abstract class Container extends Component{

	public Container(UIContext context, JSONObject componentJSON) throws KeyNotValidException, DuplicateIDException {
		super(context, componentJSON);
	}

	public abstract View getView();
	public abstract View getShadowView();
	public abstract boolean isTransparent();

}
