package mobi.monaca.framework.plugin;

import java.util.ArrayList;

import mobi.monaca.framework.MonacaApplication;
import mobi.monaca.framework.MonacaPageActivity;
import mobi.monaca.framework.nativeui.UpdateStyleQuery;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MonacaNativeUIPlugin extends Plugin {

	protected MonacaPageActivity getMonacaPageActivity() {
		return (MonacaPageActivity) cordova.getActivity();
	}

	@Override
	public PluginResult execute(String action, final JSONArray args, String callbackId) {
		if (action.equals("retrieve")) {
			return retrieveStyle(args.optString(0));
		} else if (action.equals("update")) {
			JSONArray ids = args.optJSONArray(0);
			String id = args.optString(0);
			if (ids == null) {
				ids = new JSONArray();
				try {
					ids.put(0, id);
				} catch (JSONException e) {
					ids = new JSONArray();
				}
			}

			if (args.length() == 3) {
				String styleString = "{" + args.optString(1) + ":'" + args.optString(2) + "'}";
				try {
					JSONObject styleObject = new JSONObject(styleString);
					return updateStyle(ids, styleObject);
				} catch (JSONException e) {
					e.printStackTrace();
					return new PluginResult(Status.JSON_EXCEPTION);
				}
			} else {
				return updateStyle(ids, args.optJSONObject(1));
			}

		} else if (action.equals("updateBulkily")) {
			return updateStyleBulkily(args);
		} else if (action.equals("info")) {
			return getInfoForJavaScript();
		} else {
			if (action.equalsIgnoreCase("showSpinner")) {
				getMonacaPageActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MonacaApplication application = (MonacaApplication) getMonacaPageActivity().getApplication();
						try {
							application.showMonacaSpinnerDialog(getMonacaPageActivity().getUiContext(), args);
							// application.showMonacaSpinnerDialog();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				return new PluginResult(PluginResult.Status.OK);
			}

			if (action.equalsIgnoreCase("updateSpinnerTitle")) {
				getMonacaPageActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MonacaApplication application = (MonacaApplication) getMonacaPageActivity().getApplication();
						try {
							application.updateSpinnerTitle(args.optString(0));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				return new PluginResult(PluginResult.Status.OK);
			}

			if (action.equalsIgnoreCase("hideSpinner")) {
				getMonacaPageActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MonacaApplication application = (MonacaApplication) getMonacaPageActivity().getApplication();
						application.hideMonacaSpinnerDialog();
					}
				});

				return new PluginResult(PluginResult.Status.OK);
			}

		}
		return new PluginResult(Status.INVALID_ACTION);
	}

	public PluginResult retrieveStyle(String componentId) {
		JSONObject style = ((MonacaPageActivity) cordova.getActivity()).getStyle(componentId);

		return style != null ? new PluginResult(Status.OK, style) : new PluginResult(Status.ERROR);
	}

	public PluginResult updateStyle(JSONArray ids, JSONObject style) {
		((MonacaPageActivity) cordova.getActivity()).updateStyle(new UpdateStyleQuery(ids, style));
		return new PluginResult(Status.OK);
	}

	public PluginResult getInfoForJavaScript() {
		JSONObject info = ((MonacaPageActivity) cordova.getActivity()).getInfoForJavaScript();

		return info != null ? new PluginResult(Status.OK, info) : new PluginResult(Status.ERROR);
	}

	public PluginResult updateStyleBulkily(JSONArray args) {
		JSONArray queriesJson = args.optJSONArray(0);
		if (queriesJson != null) {
			ArrayList<UpdateStyleQuery> queries = new ArrayList<UpdateStyleQuery>();
			for (int i = 0; i < queriesJson.length(); i++) {
				JSONObject queryJson = queriesJson.optJSONObject(i);
				JSONArray ids = queryJson.optJSONArray("ids");
				JSONObject style = queryJson.optJSONObject("style");
				queries.add(new UpdateStyleQuery(ids, style));
			}
			((MonacaPageActivity) cordova.getActivity()).updateStyleBulkily(queries);

			return new PluginResult(Status.OK);
		} else {
			return new PluginResult(Status.JSON_EXCEPTION);
		}
	}

}
