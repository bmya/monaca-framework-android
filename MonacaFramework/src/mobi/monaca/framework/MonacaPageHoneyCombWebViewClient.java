package mobi.monaca.framework;

import java.io.ByteArrayInputStream;

import org.apache.cordova.CordovaWebView;

import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/** This WebViewClient is used for an WebView DroidgapActivity has. */
public class MonacaPageHoneyCombWebViewClient extends MonacaPageGingerbreadWebViewClient {

    private static final String TAG = MonacaPageHoneyCombWebViewClient.class.getSimpleName();

    public MonacaPageHoneyCombWebViewClient(String currentUrl,
            MonacaPageActivity monacaPage, CordovaWebView cordovaWebView) {
    	super(currentUrl, monacaPage, cordovaWebView);
    }

    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (!MonacaApplication.allowAccess(url)) {
            WebResourceResponse response = new WebResourceResponse(
                "text/html", "UTF-8", 
                new ByteArrayInputStream("<font color='#999'>not allowed</font>".getBytes())
            );
            return response;
        }
        
        return super.shouldInterceptRequest(view, url);
    }
}
