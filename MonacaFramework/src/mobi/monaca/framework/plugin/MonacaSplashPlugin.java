package mobi.monaca.framework.plugin;

import mobi.monaca.framework.MonacaPageActivity;
import mobi.monaca.framework.transition.TransitionParams;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;


/**
 * called by cordova.exec(null, null, "MonacaSplashScreen", "show" or "hide", []);
 *
 */

public class MonacaSplashPlugin extends Plugin {

    protected MonacaPageActivity getMonacaPageActivity() {
        return (MonacaPageActivity) cordova.getActivity();
    }

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
        // push
        if (action.equals("show")) {
        	getMonacaPageActivity().showMonacaSplash();
            return new PluginResult(PluginResult.Status.OK);
        }

        // push
        if (action.equals("hide")) {
        	getMonacaPageActivity().removeMonacaSplash();
            return new PluginResult(PluginResult.Status.OK);
        }
        return new PluginResult(Status.INVALID_ACTION);
	}

}
