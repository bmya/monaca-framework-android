package mobi.monaca.framework.plugin.innovationplus;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

public abstract class CordovaPluginExecutor {
	protected CordovaInterface cordova;
	protected Context context;

	public CordovaPluginExecutor(CordovaInterface cordova) {
		this.cordova = cordova;
		this.context = cordova.getActivity();
	}

	protected void runOnUiThread(final Runnable runnable) {
		cordova.getActivity().runOnUiThread(runnable);
	}

	public abstract boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException;
}
