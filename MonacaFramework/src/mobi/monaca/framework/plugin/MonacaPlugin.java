package mobi.monaca.framework.plugin;

import mobi.monaca.utils.MonacaDevice;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MonacaPlugin extends CordovaPlugin {
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if(action.equals("getRuntimeConfiguration")){
			JSONObject resultJson = new JSONObject();
			resultJson.put("deviceId", MonacaDevice.getDeviceId(cordova.getActivity()));
			callbackContext.success(resultJson);
			return true;
		}
		
		// invalid action
		return false;
	}
}
