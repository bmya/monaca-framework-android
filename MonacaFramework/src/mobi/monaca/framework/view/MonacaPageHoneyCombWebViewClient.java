package mobi.monaca.framework.view;

import java.io.ByteArrayInputStream;

import mobi.monaca.framework.MonacaApplication;
import mobi.monaca.framework.MonacaPageActivity;
import mobi.monaca.framework.util.MyLog;

import org.apache.cordova.CordovaWebView;

import android.annotation.TargetApi;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/** This WebViewClient is used for an WebView DroidgapActivity has. */
public class MonacaPageHoneyCombWebViewClient extends MonacaPageGingerbreadWebViewClient {

    private static final String TAG = MonacaPageHoneyCombWebViewClient.class.getSimpleName();

    public MonacaPageHoneyCombWebViewClient(String currentUrl,
            MonacaPageActivity monacaPage, CordovaWebView cordovaWebView) {
    	super(currentUrl, monacaPage, cordovaWebView);
    }

    @TargetApi(11)
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (!MonacaApplication.allowAccess(url)) {
        	MyLog.w(TAG, "Not allowing access to url:" + url);
        	MyLog.sendBloadcastDebugLog(monacaPage, "Not allowing access to " + url, "error", "error");
            WebResourceResponse response = new WebResourceResponse(
                "text/html", "UTF-8", 
                new ByteArrayInputStream("<font color='#999'>not allowed</font>".getBytes())
            );
            return response;
        }
        
        return super.shouldInterceptRequest(view, url);
    }
}
