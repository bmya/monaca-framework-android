package mobi.monaca.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.monaca.framework.transition.TransitionParams;

import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

public class MonacaTransitPlugin extends Plugin {

    protected Handler handler = new Handler();

    protected MonacaPageActivity getMonacaPageActivity() {
        return (MonacaPageActivity) cordova.getActivity();
    }

    @Override
    public PluginResult execute(String action, final JSONArray args,
            String callbackId) {

        // push
        if (action.equals("push")) {
            getMonacaPageActivity().pushPageAsync(args.optString(0),
                    TransitionParams.from(args.optJSONObject(1), "transit"));
            return new PluginResult(com.phonegap.api.PluginResult.Status.OK);
        }

        // modal
        if (action.equals("modal")) {
            getMonacaPageActivity().pushPageAsync(args.optString(0),
                    TransitionParams.from(args.optJSONObject(1), "modal"));
            return new PluginResult(com.phonegap.api.PluginResult.Status.OK);
        }

        // link
        if (action.equals("link")) {
            JSONObject obj = args.optJSONObject(1);
            obj = obj != null ? obj : new JSONObject();
            getMonacaPageActivity().loadRelativePathAsync(args.optString(0));
            return new PluginResult(com.phonegap.api.PluginResult.Status.OK);
        }

        // back
        /*
         * if (action.equals("pop") || action.equals("dismiss")) {
         * getMonacaPageActivity().popPageAsync(); return new
         * PluginResult(com.phonegap.api.PluginResult.Status.OK); }
         */

        if (action.equals("pop")) {
            getMonacaPageActivity().popPageAsync(
                    TransitionParams.from(new JSONObject(), "pop"));
            return new PluginResult(com.phonegap.api.PluginResult.Status.OK);
        }

        if (action.equals("dismiss")) {
            getMonacaPageActivity().popPageAsync(
                    TransitionParams.from(new JSONObject(), "dismiss"));
            return new PluginResult(com.phonegap.api.PluginResult.Status.OK);
        }

        // execute browser
        if (action.equals("browse")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Uri uri = Uri.parse(args.optString(0));
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    cordova.getActivity().startActivity(i);
                }
            });
            return new PluginResult(com.phonegap.api.PluginResult.Status.OK);
        }

        // go to home
        if (action.equals("home")) {
            getMonacaPageActivity().goHomeAsync(args.optJSONObject(0));
            return new PluginResult(com.phonegap.api.PluginResult.Status.OK);
        }
        
        if (action.equals("clearWithoutTop")) {
            clearWithoutTop();
            return new PluginResult(com.phonegap.api.PluginResult.Status.OK);
        }

        return new PluginResult(Status.INVALID_ACTION);
    }

    protected void pushPage(String url, TransitionParams params) {
        getMonacaPageActivity().pushPageAsync(url, params);
    }
    
    protected void clearWithoutTop() {
        List<MonacaPageActivity> pages = new ArrayList<MonacaPageActivity>(MonacaApplication.getPages());
        pages = pages.subList(0, pages.size() - 1);
        Collections.reverse(pages);
        
        for (MonacaPageActivity page : pages) {
            page.finish();
        }
    }

}
