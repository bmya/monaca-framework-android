package mobi.monaca.framework;

import mobi.monaca.framework.util.MyLog;

import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;

import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;

public class MonacaChromeClient extends CordovaChromeClient {
	private static final String TAG = MonacaChromeClient.class.getSimpleName();
	private CordovaInterface cordova;

	public MonacaChromeClient(CordovaInterface ctx) {
		super(ctx);
		this.cordova = ctx;
	}

	public MonacaChromeClient(CordovaInterface ctx, CordovaWebView webView) {
		super(ctx, webView);
		this.cordova = ctx;
	}

	@Override
	public boolean onJsPrompt(WebView webView, String url, String message, String defaultValue, JsPromptResult jsPromtResult) {
//		MyLog.v(MonacaPageActivity.TAG, "onJsPromt:arg1:" + url + ", arg2:" + message + ", arg3:" + defaultValue);
		if (url.equalsIgnoreCase("uri")) {
			// MyLog.v(TAG, "url null-> return true");
			return true;
		}

		return super.onJsPrompt(webView, url, message, defaultValue, jsPromtResult);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		if (cordova.getActivity().isFinishing()) {
			Log.w(TAG, "Trying to show alert dialog while activity is finishing!! -> ignore");
			result.cancel();
			return true;
		}
		return super.onJsAlert(view, url, message, result);
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
		if (cordova.getActivity().isFinishing()) {
			Log.w(TAG, "Trying to show confirm dialog while activity is finishing!! -> ignore");
			result.cancel();
			return true;
		}
		return super.onJsConfirm(view, url, message, result);
	}

}