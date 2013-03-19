package mobi.monaca.framework.plugin.innovationplus;

import jp.innovationplus.ipp.client.IPPLoginClient;
import jp.innovationplus.ipp.core.IPPQueryCallback;
import jp.innovationplus.ipp.jsontype.IPPLoginResult;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InnovationPlusPlugin extends CordovaPlugin {
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("user.login")) {
			JSONObject loginSet = args.optJSONObject(0);
			if (loginSet != null) {
				doLogin(loginSet, callbackContext);
				return true;
			}
		}
		//TODO fill other patterns
		return false;
	}

	private void doLogin(JSONObject loginSet, final CallbackContext callback) throws JSONException {
		IPPLoginClient loginClient = new IPPLoginClient(cordova.getActivity());
		loginClient.login(loginSet.getString("username"), loginSet.getString("password"), new IPPQueryCallback<IPPLoginResult>() {
			@Override
			public void ippDidFinishLoading(IPPLoginResult arg0) {
				JSONObject result = new JSONObject();
				try {
					String authKey = arg0.getAuth_key();
					result.put("auth_key", authKey);
					AuthKeyPreferenceUtil.saveAuthKey(cordova.getActivity(), authKey);
					callback.success(result);
				} catch (JSONException e) {
					callback.error(-20);
				}
			}
			@Override
			public void ippDidError(int arg0) {
				callback.error(arg0);
			}
		});
	}
}
