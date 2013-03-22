package mobi.monaca.framework.plugin.innovationplus;

import jp.innovationplus.ipp.client.IPPApplicationResourceClient;
import jp.innovationplus.ipp.core.IPPQueryCallback;
import mobi.monaca.framework.util.MyLog;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//TODO not tested
public class ApplicationResource extends CordovaPluginExecutor {
	private static final String TAG = ApplicationResource.class.getSimpleName();
	public ApplicationResource(CordovaInterface cordova) {
		super(cordova);
	}

	@Override
	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		final String authKey = AuthKeyPreferenceUtil.getAuthKey(context);

		if (authKey.equals("")) {
			callbackContext.error(InnovationPlusPlugin.ERROR_NO_AUTH_KEY);
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
		if (action.equals("createResource")) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					createResource(args, authKey, callbackContext);
				}
			});
			return true;
		}
		if (action.equals("deleteResource")) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					deleteResource(args, authKey, callbackContext);
				}
			});
			return true;
		}

		return false;
	}
	private void deleteResource(JSONArray args, String authKey, final CallbackContext callbackContext) {
		String resourceId;
		try {
			resourceId = args.getString(0);
		} catch (JSONException e) {
			e.printStackTrace();
			callbackContext.error(InnovationPlusPlugin.ERROR_INVALID_PARAMETER);
			return;
		}
		IPPApplicationResourceClient client = new IPPApplicationResourceClient(context);
		client.setAuthKey(authKey);
		client.delete(JSONObjectResource.class, resourceId, new IPPQueryCallback<String>() {
			@Override
			public void ippDidError(int i) {
				MyLog.d(TAG, "ippDidError:" + i);
				callbackContext.error(i);
			}
			@Override
			public void ippDidFinishLoading(String arg0) {
				MyLog.d(TAG, "ippDidFinishLoading :" + arg0);
				callbackContext.success(arg0);
			}
		});
	}

	private void createResource(JSONArray args, String authKey, final CallbackContext callbackContext) {
		JSONObject content;
		try {
			content = args.getJSONObject(0);
		} catch (JSONException e) {
			// maybe JSONArray, goto createPluralResource
			createPluralResource(args, authKey, callbackContext);
			return;
		}

		JSONObjectResource resource = new JSONObjectResource();

		resource.setJson(content);
		IPPApplicationResourceClient client = new IPPApplicationResourceClient(context);
		client.setAuthKey(authKey);
		client.create(JSONObjectResource.class, resource, new IPPQueryCallback<String>() {
			@Override
			public void ippDidError(int i) {
				MyLog.d(TAG, "ippDidError:" + i);
				callbackContext.error(i);
			}
			@Override
			public void ippDidFinishLoading(String arg0) {
				MyLog.d(TAG, "ippDidFinishLoading :" + arg0);
				callbackContext.success(arg0);
			}
		});
	}

	private void createPluralResource(JSONArray args, String authKey, final CallbackContext callbackContext) {
		JSONArray content;
		try {
			content = args.getJSONArray(0);
		} catch (JSONException e) {
			e.printStackTrace();
			callbackContext.error(InnovationPlusPlugin.ERROR_INVALID_PARAMETER);
			return;
		}

		final int length = content.length();
		if (length < 1) {
			callbackContext.error(InnovationPlusPlugin.ERROR_INVALID_PARAMETER);
			return;
		}

		JSONObjectResource[] resources = new JSONObjectResource[length];

		try {
			for (int i = 0; i < length; i++) {
				resources[i] = new JSONObjectResource();
				resources[i].setJson(content.getJSONObject(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			callbackContext.error(InnovationPlusPlugin.ERROR_INVALID_PARAMETER);
			return;
		}

		IPPApplicationResourceClient client = new IPPApplicationResourceClient(context);
		client.setAuthKey(authKey);
		client.createAll(JSONObjectResource.class, resources, new IPPQueryCallback<Void>() {
			@Override
			public void ippDidError(int i) {
				MyLog.d(TAG, "ippDidError:" + i);
				callbackContext.error(i);
			}
			@Override
			public void ippDidFinishLoading(Void arg0) {
				MyLog.d(TAG, "ippDidFinishLoading");
				try {
					callbackContext.success(new JSONObject().put("resultCount", length));
				} catch (JSONException e) {
					e.printStackTrace();
					callbackContext.error(InnovationPlusPlugin.ERROR_WITH_EXCEPTION);
				}
			}
		});
	}

	private void retrieveResource(JSONArray args, String authKey, final CallbackContext callbackContext) {
		IPPApplicationResourceClient client = new IPPApplicationResourceClient(context);
		String resourceId;
		try {
			resourceId = args.getString(0);
		} catch (JSONException e) {
			e.printStackTrace();
			callbackContext.error(InnovationPlusPlugin.ERROR_INVALID_PARAMETER);
			return;
		}

		client.get(JSONObjectResource.class, resourceId, new IPPQueryCallback<JSONObjectResource>() {
			@Override
			public void ippDidError(int i) {
				MyLog.d(TAG, "ippDidError:" + i);
				callbackContext.error(i);
			}
			@Override
			public void ippDidFinishLoading(JSONObjectResource arg0) {
				MyLog.d(TAG, "ippDidFinishLoading :" + arg0.getJson().toString());
				callbackContext.success(arg0.getJson());
			}
		});
	}

	private void retrieveQueryResource(JSONArray args, String authKey, final CallbackContext callbackContext) {
		IPPApplicationResourceClient client = new IPPApplicationResourceClient(context);
		JSONObject param;
		try {
			param = args.getJSONObject(0);
		} catch (JSONException e) {
			e.printStackTrace();
			callbackContext.error(InnovationPlusPlugin.ERROR_INVALID_PARAMETER);
			return;
		}
		IPPApplicationResourceClient.QueryCondition condition = new IPPApplicationResourceClient.QueryCondition();
		try {
			condition.setCount(param.getInt("count"));
		} catch (JSONException e) {
		}
		try {
			condition.setSince(param.getLong("since"));
		} catch (JSONException e) {
		}
		try {
			condition.setUntil(param.getInt("until"));
		} catch (JSONException e) {
		}
		try {
			condition.setCount(param.getInt("count"));
		} catch (JSONException e) {
		}

		client.query(JSONObjectResource.class, condition, new IPPQueryCallback<JSONObjectResource[]>() {
			@Override
			public void ippDidError(int i) {
				MyLog.d(TAG, "ippDidError:" + i);
				callbackContext.error(i);
			}
			@Override
			public void ippDidFinishLoading(JSONObjectResource[] arg0) {
				JSONObject response = new JSONObject();
				try {
					int length = arg0.length;
					response.put("resultCount", length);
					JSONArray resultArray  = new JSONArray();
					for (int i = 0; 1 < length; i++) {
						resultArray.put(arg0[i].getJson());
					}
					response.put("result", resultArray);
					callbackContext.success(response);
				} catch (JSONException e) {
					e.printStackTrace();
					callbackContext.error(InnovationPlusPlugin.ERROR_WITH_EXCEPTION);
				}

			}
		});
	}
}
