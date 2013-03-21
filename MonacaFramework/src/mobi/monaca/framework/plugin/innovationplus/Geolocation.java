package mobi.monaca.framework.plugin.innovationplus;

import jp.innovationplus.ipp.client.IPPGeoLocationClient;
import jp.innovationplus.ipp.core.IPPQueryCallback;
import jp.innovationplus.ipp.jsontype.IPPGeoLocation;

import mobi.monaca.framework.util.MyLog;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Geolocation extends CordovaPluginExecutor{
	private static final String TAG = Geolocation.class.getSimpleName();
	public Geolocation(CordovaInterface cordova) {
		super(cordova);
	}

	@Override
	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		final String authKey = AuthKeyPreferenceUtil.getAuthKey(context);

		if (authKey.equals("")) {
			callbackContext.error(InnovationPlusPlugin.ERROR_NO_AUTH_KEY);// TODO not commentouted in product
			return true;
		}

		if (action.equals("retrieveOwnResource")) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					retrieveOwnResource(args, authKey, callbackContext);
				}
			});
		}

		// TODO implement other patterns
		return false;
	}

	private void retrieveOwnResource(JSONArray args, String authKey, final CallbackContext callbackContext) {
		IPPGeoLocationClient client = new IPPGeoLocationClient(context);
		client.setAuthKey(authKey);
		client.get(new IPPQueryCallback<IPPGeoLocation>() {
			@Override
			public void ippDidFinishLoading(IPPGeoLocation arg0) {
				MyLog.d(TAG, "ippDidFinishLoading:");
				try	{
					JSONObject resultJson = new JSONObject();
					resultJson.put("resource_id", arg0.getResource_id());
					resultJson.put("latitude", arg0.getLatitude());
					resultJson.put("longitude", arg0.getLongitude());
					resultJson.put("provider", arg0.getProvider());
					resultJson.put("timestamp", arg0.getTimestamp());

					callbackContext.success(resultJson);
				} catch (JSONException e) {
					e.printStackTrace();
					callbackContext.error(InnovationPlusPlugin.ERROR_WITH_EXCEPTION);
				}
			}

			@Override
			public void ippDidError(int i) {
				MyLog.d(TAG, "ippDidError:" + i);
				callbackContext.error(i);
			}
		});
	}

}
