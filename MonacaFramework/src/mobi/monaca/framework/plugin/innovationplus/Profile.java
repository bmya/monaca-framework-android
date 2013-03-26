package mobi.monaca.framework.plugin.innovationplus;

import jp.innovationplus.ipp.client.IPPProfileClient;
import jp.innovationplus.ipp.client.IPPProfileClient.Fields;
import jp.innovationplus.ipp.core.IPPQueryCallback;
import jp.innovationplus.ipp.jsontype.IPPProfile;
import jp.innovationplus.ipp.jsontype.IPPProfile.IPPAddress;
import mobi.monaca.framework.util.MyLog;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//TODO not tested
public class Profile extends CordovaPluginExecutor{
	public Profile(CordovaInterface cordova) {
		super(cordova);
	}

	@Override
	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		final String authKey = AuthKeyPreferenceUtil.getAuthKey(context);

		if (authKey.equals("")) {
			callbackContext.error(InnovationPlusPlugin.ERROR_NO_AUTH_KEY);// TODO not commentouted in product
			return true;
		}

		if (action.equals("retrieveResource")) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					retrieveResource(args, authKey, callbackContext);
				}
			});
			return true;
		}
		if (action.equals("retrieveQueryResource")) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					retrieveQueryResource(args, authKey, callbackContext);
				}
			});
			return true;
		}
		return false;
	}

	private void retrieveQueryResource(JSONArray executeArg, String authKey, final CallbackContext callbackContext) {
		IPPProfileClient client = new IPPProfileClient(context);
		client.setAuthKey(authKey);
		String[] fields = Util.jsonArrayToStringArray(executeArg.optJSONArray(0));
		if (fields != null) {
			client.query(new Fields().setFields(fields), new IPPQueryCallback<IPPProfile[]>() {
				@Override
				public void ippDidError(int i) {
					MyLog.d("profile", "retrieveQueryResource - ippDidError :" + i);
					callbackContext.error(i);
				}
				@Override
				public void ippDidFinishLoading(IPPProfile[] arg0) {
					try{
						MyLog.d("profile", "retrieveQueryResource - ippDidFinishLoading");
						callbackContext.success(buildResultJson(arg0));
					}catch (JSONException e) {
						e.printStackTrace();
						callbackContext.error(InnovationPlusPlugin.ERROR_WITH_EXCEPTION);
					}
				}
			});
		} else {
			client.query(new IPPQueryCallback<IPPProfile[]>() {
				@Override
				public void ippDidError(int i) {
					MyLog.d("profile", "retrieveQueryResource - ippDidError :" + i);
					callbackContext.error(i);
				}
				@Override
				public void ippDidFinishLoading(IPPProfile[] arg0) {
					MyLog.d("profile", "retrieveQueryResource - ippDidFinishLoading");
					try {
						callbackContext.success(buildResultJson(arg0));
					} catch (JSONException e) {
						callbackContext.error(InnovationPlusPlugin.ERROR_WITH_EXCEPTION);
						e.printStackTrace();
					}
				}
			});
		}

	}

	private void retrieveResource(JSONArray executeArg, String authKey, final CallbackContext callbackContext) {
		IPPProfileClient client = new IPPProfileClient(context);
		client.setAuthKey(authKey);
		String[] fields = Util.jsonArrayToStringArray(executeArg.optJSONArray(0));
		//if ("".equals("")) return;
		if (fields != null) {
			client.get(new Fields().setFields(fields), new IPPQueryCallback<IPPProfile>() {
				@Override
				public void ippDidError(int i) {
					MyLog.d("profile", "retrieveResource - ippDidError :" + i);
					callbackContext.error(i);
				}
				@Override
				public void ippDidFinishLoading(IPPProfile arg0) {
					try{
						MyLog.d("profile", "retrieveResource - ippDidFinishLoading");

						callbackContext.success(buildResultJson(arg0));
					}catch (JSONException e) {
						e.printStackTrace();
						callbackContext.error(InnovationPlusPlugin.ERROR_WITH_EXCEPTION);
					}
				}
			});
		} else {
			client.get(new IPPQueryCallback<IPPProfile>() {
				@Override
				public void ippDidError(int i) {
					callbackContext.error(i);
				}
				@Override
				public void ippDidFinishLoading(IPPProfile arg0) {
					try {
						callbackContext.success(buildResultJson(arg0));
					} catch (JSONException e) {
						callbackContext.error(InnovationPlusPlugin.ERROR_WITH_EXCEPTION);
						e.printStackTrace();
					}
				}
			});
		}
	}

	private JSONObject buildResultJson(IPPProfile[] arg0) throws JSONException {
		int size = arg0.length;
		JSONObject resultJson = new JSONObject();
		resultJson.put("resultCount", size);

		JSONArray resultArray = new JSONArray();
		for (int i = 0; i < size; i++) {
			resultArray.put(buildResultJson(arg0[i]));
		}

		resultJson.put("result", resultArray);
		return resultJson;
	}

	private JSONObject buildResultJson(IPPProfile arg0) throws JSONException {
		JSONObject resultJson = new JSONObject();
		resultJson.putOpt("timestamp", arg0.getTimestamp());
		resultJson.putOpt("screenName", arg0.getScreenName());
		resultJson.putOpt("firstName", arg0.getFirstName());
		resultJson.putOpt("lastName", arg0.getLastName());
		resultJson.putOpt("gender", arg0.getGender());
		resultJson.putOpt("birth", arg0.getBirth());

		IPPAddress address = arg0.getAddress();
		if (address != null) {
			JSONObject addressJson = new JSONObject();
			addressJson.putOpt("zipcode", address.getZipcode());
			addressJson.putOpt("state", address.getState());
			addressJson.putOpt("city", address.getCity());
			addressJson.putOpt("streetAddress", address.getStreetAddress());
			resultJson.putOpt("address", addressJson);
		}
		return resultJson;
	}
}
