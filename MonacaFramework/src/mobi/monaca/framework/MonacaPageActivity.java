package mobi.monaca.framework;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobi.monaca.framework.nativeui.UIBuilder;
import mobi.monaca.framework.nativeui.UIBuilder.ResultSet;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.nativeui.UpdateStyleQuery;
import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.container.ToolbarContainer;
import mobi.monaca.framework.nativeui.menu.MenuRepresentation;
import mobi.monaca.framework.psedo.R;
import mobi.monaca.framework.template.AssetTemplateResource;
import mobi.monaca.framework.template.TemplateEngine;
import mobi.monaca.framework.template.model.MonacaApplicationInfo;
import mobi.monaca.framework.transition.BackgroundDrawable;
import mobi.monaca.framework.transition.ClosePageIntent;
import mobi.monaca.framework.transition.TransitionParams;
import mobi.monaca.framework.util.AssetUriUtil;
import mobi.monaca.framework.util.BenchmarkTimer;
import mobi.monaca.framework.util.InputStreamLoader;
import mobi.monaca.framework.util.MyLog;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.DroidGap;
import org.apache.cordova.api.CordovaInterface;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * This class represent a page of Monaca application.
 */
public class MonacaPageActivity extends DroidGap {

	public static final String TRANSITION_PARAM_NAME = "monaca.transition";
	public static final String URL_PARAM_NAME = "monaca.url";
	public static final String TAG = MonacaPageActivity.class.getSimpleName();

	protected String userId = null, password = null, uri = null;

	protected BackgroundDrawable background = null;

	protected DefaultHttpClient httpClient;

	protected HashMap<String, Component> dict;

	protected Handler handler = new Handler();

	protected TemplateEngine templateEngine;

	protected UIBuilder.ResultSet uiBuilderResult = null;

	protected int pageIndex = 0;

	protected BroadcastReceiver closePageReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			if (pageIndex >= level) {
				finish();
			}
			Log.d(MonacaPageActivity.this.getClass().getSimpleName(), "close intent received: " + uri);
			Log.d(MonacaPageActivity.this.getClass().getSimpleName(), "page index: " + pageIndex);
		}
	};

	/** If this flag is true, activity is capable of transition. */
	protected boolean isCapableForTransition = true;

	protected UIContext uiContext = null;

	protected TransitionParams transitionParams;

	protected JSONObject infoForJavaScript = new JSONObject();
	public String mCurrentHtml;

	@Override
	public void onCreate(Bundle savedInstance) {
		prepare();
		super.onCreate(savedInstance);

		MyLog.v(TAG, "MonacaApplication.getPages().size():" + MonacaApplication.getPages().size());
		if (MonacaApplication.getPages().size() == 1) {
			init();
			loadCurrentUri();
		} else {
			init();
			loadUiFile(uri);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					loadCurrentUriWithoutUIFile();
				}
			}, 100);
		}

		appView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});

		// dirty fix for android4's strange bug
		if (transitionParams.animationType == TransitionParams.TransitionAnimationType.MODAL) {
			overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_dialog_open_enter, mobi.monaca.framework.psedo.R.anim.monaca_dialog_open_exit);
		} else if (transitionParams.animationType == TransitionParams.TransitionAnimationType.TRANSIT) {
			overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_slide_open_enter, mobi.monaca.framework.psedo.R.anim.monaca_slide_open_exit);
		} else if (transitionParams.animationType == TransitionParams.TransitionAnimationType.NONE) {
			overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_none, mobi.monaca.framework.psedo.R.anim.monaca_none);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MyLog.v(TAG, "onPrepareOptionMenu()");
		if (uiBuilderResult != null) {
			MyLog.v(TAG, "building menu");

			menu.clear();
			MenuRepresentation menuRepresentation = MonacaApplication.findMenuRepresentation(uiBuilderResult.menuName);

			MyLog.v(TAG, "menuRepresentation:" + menuRepresentation);
			if (menuRepresentation != null) {
				menuRepresentation.configureMenu(uiContext, menu);
			}

			return true;
		} else {
			return false;
		}
	}

	protected void prepare() {
		loadParams();

		MonacaApplication.addPage(this);
		pageIndex = MonacaApplication.getPages().size() - 1;
		registerReceiver(closePageReceiver, ClosePageIntent.createIntentFilter());
		uiContext = new UIContext(uri, this);

		// setup orientation
		setRequestedOrientation(transitionParams.requestedOrientation);

		// override theme
		if (transitionParams.animationType == TransitionParams.TransitionAnimationType.NONE) {
			setTheme(android.R.style.Theme_Black_NoTitleBar);
		} else if (transitionParams.animationType == TransitionParams.TransitionAnimationType.MODAL) {
			setTheme(mobi.monaca.framework.psedo.R.style.MonacaDialogTheme);
		} else if (transitionParams.animationType == TransitionParams.TransitionAnimationType.TRANSIT) {
			setTheme(mobi.monaca.framework.psedo.R.style.MonacaSlideTheme);
		} else {
			setTheme(android.R.style.Theme_Black_NoTitleBar);
		}

		// template engine settings
		initTemplateEngine();

		try {
			infoForJavaScript.put("display", createDisplayInfo());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		loadBackground(getResources().getConfiguration());
	}

	private void initTemplateEngine() {
		if (usingTemplatgeEngine()) {

			int wwwIndex = uri.indexOf("/www/");
			String appRoot = uri.substring(0, wwwIndex + 5);
			MonacaApplicationInfo applicationInfo = new MonacaApplicationInfo(appRoot);

			if (UrlUtil.isUrl(uri)) {
				templateEngine = new TemplateEngine(this, new RemoteTemplateResource(httpClient), applicationInfo);
			} else if (AssetUriUtil.isAssetUri(uri)) {
				templateEngine = new TemplateEngine(this, new AssetTemplateResource(this), applicationInfo);
			} else if (uri.startsWith("file:///")) {
				MyLog.v(TAG, "Using FileTemplateResource");
				templateEngine = new TemplateEngine(this, new FileTemplateResource(), applicationInfo);
			} else {
				throw new RuntimeException("unsupported such uri: " + uri);
			}
		} else {
			templateEngine = null;
		}
	}

	/** Load background drawable from transition params and device orientation. */
	protected void loadBackground(Configuration config) {
		if (transitionParams != null && transitionParams.hasBackgroundImage()) {
			String path = null;
			String preferedPath = "www/" + UIContext.getPreferredPath(transitionParams.backgroundImagePath);
			if (existsAsset(this, preferedPath)) {
				path = preferedPath;
			} else {
				path = "www/" + transitionParams.backgroundImagePath;
			}

			try {
				Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(path));
				background = new BackgroundDrawable(bitmap, getWindowManager().getDefaultDisplay(), config.orientation);
			} catch (Exception e) {
			}
		}
	}

	/** Release background drawable. */
	protected void unloadBackground() {
		if (background != null) {
			appView.setBackgroundDrawable(null);
			root.setBackgroundDrawable(null);
			background.setCallback(null);
			background = null;
			System.gc();
		}
	}

	@Override
	public void init() {
		CordovaWebView webView = new CordovaWebView(this);
		CordovaWebViewClient webViewClient = (CordovaWebViewClient) createWebViewClient(uri, this, webView);
		MonacaChromeClient webChromeClient = new MonacaChromeClient(this, webView);
		this.init(webView, webViewClient, webChromeClient);

		appView.setFocusable(true);
		appView.setFocusableInTouchMode(true);
		if (hasAuthInfo()) {
			appView.setHttpAuthUsernamePassword(Monaca.DAV_DOMAIN, // TODO
																	// change to
																	// project
																	// url
																	// returned
																	// from
																	// server
					Monaca.DAV_AUTH_NAME, userId, password);
			appView.setHttpAuthUsernamePassword(Monaca.DAV_DOMAIN + ":443", Monaca.DAV_AUTH_NAME, userId, password);
		}

		setupBackground();

		loadUrl("about:blank?");

		root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				try {
					int height = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() - root.getHeight();
					infoForJavaScript.put("statusbarHeight", height);
				} catch (JSONException e) {
					Log.e(getClass().getSimpleName(), "fail to get statusbar height.");
				}
			}
		});

		setupBackground();

	}

	class MonacaChromeClient extends CordovaChromeClient {

		public MonacaChromeClient(CordovaInterface ctx) {
			super(ctx);
		}

		public MonacaChromeClient(MonacaPageActivity monacaPageActivity, CordovaWebView webView) {
			super(monacaPageActivity, webView);
		}

		@Override
		public boolean onJsPrompt(WebView arg0, String arg1, String arg2, String arg3, JsPromptResult arg4) {
			// MyLog.v(TAG, "onJsPromt:arg1:" + arg1 + ", arg2:" + arg2 +
			// ", arg3:" + arg3);
			if (arg1.equalsIgnoreCase("uri")) {
				// MyLog.v(TAG, "url null-> return true");
				return true;
			}

			return super.onJsPrompt(arg0, arg1, arg2, arg3, arg4);
		}

	}

	/** Setup background drawable for app View and root view. */
	protected void setupBackground() {
		if (background != null) {
			if (appView != null) {
				appView.setBackgroundDrawable(background);
			}

			if (root != null) {
				root.setBackgroundDrawable(background);

				if (root.getParent() == null) {
					setContentView(root);
				}
			}
		}
	}

	protected void loadLayoutInformation() {
		appView.loadUrl("javascript: window.__layout = " + infoForJavaScript.toString());
	}

	protected JSONObject createDisplayInfo() {
		JSONObject result = new JSONObject();

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Display display = getWindowManager().getDefaultDisplay();
		try {
			result.put("width", display.getWidth());
			result.put("height", display.getHeight());
		} catch (JSONException e) {
		}

		return result;
	}

	protected void loadParams() {
		Intent intent = getIntent();
		transitionParams = (TransitionParams) intent.getSerializableExtra(TRANSITION_PARAM_NAME);

		if (transitionParams == null) {
			transitionParams = TransitionParams.createDefaultParams();
		}

		uri = intent.hasExtra(URL_PARAM_NAME) ? intent.getStringExtra(URL_PARAM_NAME) : "file:///android_asset/www/index.html";

		MyLog.v(TAG, "uri:" + uri);

		userId = userId != null ? userId : getIntent().getStringExtra(Monaca.USERID_PARAM);
		password = password != null ? password : getIntent().getStringExtra(Monaca.PASSWORD_PARAM);

		if (userId != null && password != null) {
			httpClient = buildHttpClient();
		}

	}

	protected boolean usingTemplatgeEngine() {
		try {
			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			if (appInfo.metaData == null || !appInfo.metaData.containsKey("disable_monaca_template_engine")) {
				return true;
			}
			return !appInfo.metaData.getBoolean("disable_monaca_template_engine");
		} catch (NameNotFoundException e) {
			return true;
		}
	}

	public boolean existsAsset(Context context, String assetPath) {
		try {
			InputStream stream = getAssets().open(assetPath);
			stream.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public JSONObject getInfoForJavaScript() {
		return infoForJavaScript;
	}

	protected boolean hasOpacityBar(ResultSet resultSet) {
        if (resultSet.top != null && ToolbarContainer.isTransparent(resultSet.top.getStyle().optDouble("opacity", 1.0))) {
            return true;
        }
        
        if (resultSet.bottom != null && ToolbarContainer.isTransparent(resultSet.bottom.getStyle().optDouble("opacity", 1.0))) {
            return true;
        }
        
        return false;
	}

	/** Load local ui file */
	protected void loadUiFile(String uri) {
		MyLog.v(TAG, "loadUiFile()");
		String uiString = getUIFile(getUIFileUrl(uri));
		MyLog.v(TAG, "uiString:" + uiString);

		JSONObject uiJSON;
		try {
			uiJSON = new JSONObject(uiString);
		} catch (JSONException e) {
			UIUtil.reportJSONParseError(getApplicationContext(), e.getMessage());
			// uiJSON = new JSONObject();
			return;
		}

		ResultSet result = null;
		try {
			result = new UIBuilder(uiContext, uiJSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.sendBloadcastDebugLog(getApplicationContext(), "NativeComponent:" + e.getMessage(), "error");
			return;
		}

		uiBuilderResult = result;
		this.dict = result.dict;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 0);

		if (result.bottomView != null || result.topView != null) {
			MyLog.v(TAG, "result.bottomView != null || result.topView != null");

			if (hasOpacityBar(result)) {
				MyLog.v(TAG, "hasOpacityBar");

				FrameLayout frame = new FrameLayout(this);
				LinearLayout newRoot = new LinearLayout(this);
				newRoot.setOrientation(LinearLayout.VERTICAL);

				root.removeAllViews();
				MyLog.v(TAG, "root.removeAllViews()");
				root.addView(frame, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

				ViewGroup appViewParent = ((ViewGroup) appView.getParent());
				if (appViewParent != null) {
					appViewParent.removeAllViews();
				}

				frame.addView(appView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
				frame.addView(newRoot, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));

				// top bar view
				newRoot.addView(result.topView != null ? result.topView : new FrameLayout(this), 0, params);

				// center
				newRoot.addView(new LinearLayout(this), 1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

				// bottom bar view
				newRoot.addView(result.bottomView != null ? result.bottomView : new FrameLayout(this), 2, params);

				if (result.topView != null) {
					MyLog.v(TAG, "result.topView != null");
					result.topView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
					int topViewHeight = result.topView.getMeasuredHeight();
					try {
						infoForJavaScript.put("topViewHeight", topViewHeight);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (result.bottomView != null) {
					MyLog.v(TAG, "result.bottomView != null");
					result.bottomView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
					int bottomViewHeight = result.bottomView.getMeasuredHeight();
					try {
						infoForJavaScript.put("bottomViewHeight", bottomViewHeight);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else {
				MyLog.v(TAG, "noOpacityBar");
				root.removeAllViews();
				MyLog.v(TAG, "root.removeAllViews()");

				// top bar view
				root.addView(result.topView != null ? result.topView : new FrameLayout(this), 0, params);

				// center
				ViewGroup appViewParent = (ViewGroup) appView.getParent();
				if (appViewParent != null) {
					appViewParent.removeView(appView);
				}

				root.addView(appView, 1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

				// bottom bar view
				root.addView(result.bottomView != null ? result.bottomView : new FrameLayout(this), 2, params);
			}
			getWindow().getDecorView().invalidate();
		} else {
			MyLog.v(TAG, "Reverse of result.bottomView != null || result.topView != null");
			((ViewGroup) appView.getParent()).removeView(appView);
			root.removeAllViews();
			MyLog.v(TAG, "root.removeAllViews()");
			root.addView(appView);
			this.dict = new HashMap<String, Component>();
		}
	}

	protected String getUIFile(String path) {
		Reader reader;
		InputStream stream = null;

		if (path == null) {
			return "";
		}

		Log.d(getClass().getSimpleName(), "ui file loading: " + path);

		if (path.startsWith("file:///android_asset/")) {
			try {
				stream = getAssets().open(path.substring("file:///android_asset/".length()));
				reader = new InputStreamReader(stream);
			} catch (Exception e) {
				Log.d(getClass().getSimpleName(), "ui file loading fail: " + path);
				return "";
			}
		} else if (path.startsWith("file://")) {
			try {
				path = new File(path.substring(7)).getCanonicalPath();
				reader = new FileReader(new File(path));
			} catch (Exception e) {
				Log.d(getClass().getSimpleName(), "ui file loading fail: " + path);
				return "";
			}
		} else {
			try {
				stream = InputStreamLoader.loadAssetFile(this, path);
				reader = new InputStreamReader(stream, "UTF-8");
			} catch (Exception e) {
				Log.d(getClass().getSimpleName(), "ui file loading fail: " + path);
				return "";
			}
		}
		Writer writer = new StringWriter();

		char[] buffer = new char[1024];
		try {
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} catch (IOException e) {

		} finally {
			try {
				reader.close();
				if (stream != null) {
					stream.close();
				}
			} catch (Exception e) {
			}
		}

		return writer.toString();
	}

	/** Retrieve a style of Native UI Framework component. */
	public JSONObject getStyle(String componentId) {
		if (dict.containsKey(componentId)) {
			return dict.get(componentId).getStyle();
		}

		return null;
	}

	/** Update a style of Native UI Framework component. */
	public void updateStyle(final UpdateStyleQuery query) {
		List<UpdateStyleQuery> queries = new ArrayList<UpdateStyleQuery>();
		queries.add(query);
		updateStyleBulkily(queries);
	}

	/** Update bulkily the styles of Native UI Framework components. */
	public void updateStyleBulkily(final List<UpdateStyleQuery> queries) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Log.d(MonacaPageActivity.class.getSimpleName(), "updateStyleBulkily() start");
				for (UpdateStyleQuery query : queries) {
					for (int i = 0; i < query.ids.length(); i++) {
						String componentId = query.ids.optString(i, "");

						if (dict != null && dict.containsKey(componentId)) {
							Component component = dict.get(componentId);
							if (component != null) {
								component.updateStyle(query.style);
								Log.d(MonacaPageActivity.class.getSimpleName(), "updated => id: " + componentId + ", style: " + query.style.toString());
							} else {
								Log.e(MonacaPageActivity.class.getSimpleName(), "update fail => id: " + componentId + ", style: " + query.style.toString());
							}
						} else {
							Log.e(MonacaPageActivity.class.getSimpleName(), "no such component id: " + componentId);
						}
					}
				}
				Log.d(MonacaPageActivity.class.getSimpleName(), "updateStyleBulkily() done");
			}
		});
	}

	public void onPageFinished(View view, String url) {
		// for android4's strange bug.
		sendJavascript("console.log(' ');");

		// check if this is 404 page
		String errorUrl = getIntent().getStringExtra("error_url");

		if (errorUrl != null && url.endsWith("/404/404.html")) {
			String backButtonText = getString(R.string.back_button_text);
			errorUrl = UrlUtil.cutHostInUri(errorUrl);
			MyLog.v(TAG, "error url:" + errorUrl);
			appView.loadUrl("javascript:$('#url').html(\"" + errorUrl + "\"); $('#backButton').html('" + backButtonText + "')");
		}
	}

	public void onPageStarted(View view, String url) {
	    ViewGroup.LayoutParams params = this.appView.getLayoutParams();
	    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
	    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
	    this.appView.setLayoutParams(params);
	}

	public String getCurrentUrl() {
		return uri;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		loadBackground(getResources().getConfiguration());
		setupBackground();
		if (background != null) {
			background.invalidateSelf();
		}
	}

	@Override
	protected void onResume() {
		try {
			WebView.class.getMethod("onResume").invoke(this);
		} catch (Exception e) {
		}

		 if (appView != null && appView.callbackServer != null && appView.pluginManager !=
		 null) {
			 appView.loadUrl("javascript: window.onReactivate && onReactivate();");
		 }
		isCapableForTransition = true;

		super.onResume();
	}

	@Override
	public void onDestroy() {
		appView.setBackgroundDrawable(null);
		root.setBackgroundDrawable(null);

		super.onDestroy();

		MonacaApplication.removePage(this);
		unregisterReceiver(closePageReceiver);

		if (background != null) {
			background.setCallback(null);
			background = null;
		}

		userId = password = null;
		if (dict != null) {
			dict.clear();
		}
		dict = null;
		uiBuilderResult = null;
		httpClient = null;
		templateEngine = null;
		appView.setBackgroundDrawable(null);
		root.setBackgroundDrawable(null);
		closePageReceiver = null;
	}

	/** Reload current URI. */
	public void reload() {
		appView.stopLoading();
		initTemplateEngine();
		loadCurrentUri();
	}

	public void loadCurrentUri() {
		loadCurrentUri(false);
	}

	public void loadCurrentUriWithoutUIFile() {
		loadCurrentUri(true);
	}

	public String getCurrentHtml() {
		return mCurrentHtml;
	}

	/** Load current URI. */
	public void loadCurrentUri(final boolean withoutUIFile) {
		MyLog.v(TAG, "loadCurrentUri() uri:" + uri);
		// check for 404
		if (uri.equalsIgnoreCase("file:///android_asset/www/404/404.html")) {
			String failingUrl = getIntent().getStringExtra("error_url");
			show404Page(failingUrl);
			return;
		}

		if (usingTemplatgeEngine()) {
			if (!withoutUIFile) {
				loadUiFile(uri);
			}

			try {
				String result = processTemplateEngine();
				mCurrentHtml = result;
				MyLog.v(TAG, "result:" + result);

				appView.setBackgroundColor(0x00000000);
				setupBackground();
				loadLayoutInformation();

				appView.loadDataWithBaseURL(uri, result, "text/html", "UTF-8", "uri");
			} catch (Exception e) {
				MyLog.e(TAG, e.getClass().getSimpleName() + ":" + e.getCause().getMessage());
				show404Page(uri);
			}

			// appView.clearView();
			// appView.invalidate();
		} else {
			MyLog.v(TAG, "not using templateEngine");
			if (!withoutUIFile) {
				loadUiFile(uri);
			}

			appView.setBackgroundColor(0x00000000);
			setupBackground();
			loadLayoutInformation();

			appView.loadUrl(uri);
			appView.clearView();
			appView.invalidate();

			String goodHtmlPath = uri.replaceFirst("file://", "");
			try {
				mCurrentHtml = FileUtils.readFileToString(new File(goodHtmlPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void show404Page(String failingUrl) {
		try {
			InputStream is = getResources().openRawResource(R.raw.error404);
			String html = IOUtils.toString(is);
			html = html.replaceFirst("url_place_holder", UrlUtil.cutHostInUri(failingUrl));
			html = html.replaceFirst("back_button_text", getString(R.string.back_button_text));
//			appView.loadDataWithBaseURL("file:///android_asset/404/404.html", html, "text/html", "utf-8", "uri");
			appView.loadDataWithBaseURL("file:///android_res/raw/error404.html", html, "text/html", "utf-8", "uri");
//			appView.loadUrl("file:///android_res/raw/error404.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void push404Page(String errorUrl) {
		Intent intent = new Intent(this, getClass());
		intent.putExtra(URL_PARAM_NAME, "file:///android_asset/www/404/404.html");
		intent.putExtra("error_url", errorUrl);
		
		TransitionParams params = TransitionParams.from(new JSONObject(), "none");
		intent.putExtra(TRANSITION_PARAM_NAME, params);
		startActivity(intent);
		finish();
	}

	protected String processTemplateEngine() {
		String templatePath = uri.startsWith("file:///android_asset/") ? uri.substring("file:///android_asset/".length()) : uri;
		String result = templateEngine.execute(templatePath);
		return result;
	}

	/** Get if the intent has user's auth info. */
	protected boolean hasAuthInfo() {
		return userId != null && password != null;
	}

	public void pushPage(String url, TransitionParams params) {
		pushPageWithIntent(url, params);
	}

	public void pushPage(String url) {
		pushPageWithIntent(url, null);
	}

	protected void pushPageWithIntent(String url, TransitionParams params) {
		if (isCapableForTransition) {
			Intent intent = new Intent(this, getClass());

			intent.putExtra(URL_PARAM_NAME, getResolvedUrl(url));
			if (params != null) {
				intent.putExtra(TRANSITION_PARAM_NAME, params);
			}

			isCapableForTransition = false;
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (uiBuilderResult != null && uiBuilderResult.eventer.hasOnTapBackButtonAction()) {
				uiBuilderResult.eventer.onTapBackButton();
			} else if (uiBuilderResult != null && uiBuilderResult.backButtonEventer != null) {
				uiBuilderResult.backButtonEventer.onTap();
			} else {
				popPage();
			}
			return true;
		}
		return false;
	}

	public void pushPageAsync(String relativePath, final TransitionParams params) {
		final String url = uri + "/../" + relativePath;

		BenchmarkTimer.mark("pushPageAsync");
		handler.postAtFrontOfQueue(new Runnable() {
			@Override
			public void run() {
				BenchmarkTimer.mark("monaca.pushPage prev");
				pushPageWithIntent(url, params);
			}
		});
	}

	protected String getResolvedUrl(String url) {
		if (url.startsWith("file://")) {
			try {
				return "file://" + new File(url.substring("file://".length())).getCanonicalPath();
			} catch (Exception e) {
			}
		}
		return url;
	}

	public void loadRelativePathAsync(String relativePath) {
		MyLog.v(TAG, "loadRelativePathAsync. relativePath:" + relativePath);
		uri = uri + "/../" + relativePath;
		MyLog.v(TAG, "uri unresolved=" + uri);
		if (uri.startsWith("file://")) {
			try {
				uri = "file://" + new File(uri.substring(7)).getCanonicalPath();
				MyLog.v(TAG, "uri resolved=" + uri);
			} catch (Exception e) {
			}
		}

		handler.post(new Runnable() {
			@Override
			public void run() {
				loadCurrentUri();
			}
		});
	}

	public void loadRelativePathWithoutUIFile(String relativePath) {
		MyLog.v(TAG, "loadRelativePathWithoutUIFile. relativePath:" + relativePath);
		uri = uri + "/../" + relativePath;
		MyLog.v(TAG, "uri unresolved=" + uri);
		if (uri.startsWith("file://")) {
			try {
				uri = "file://" + new File(uri.substring(7)).getCanonicalPath();
				MyLog.v(TAG, "uri resolved=" + uri);
			} catch (Exception e) {
			}
		}
		loadCurrentUriWithoutUIFile();
	}

	public void popPage() {
		int pageNum = MonacaApplication.getPages().size();
		finish();

		if (pageNum > 1) {
			// dirty fix for android4's strange bug
			if (transitionParams.animationType == TransitionParams.TransitionAnimationType.MODAL) {
				overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_dialog_close_enter,
						mobi.monaca.framework.psedo.R.anim.monaca_dialog_close_exit);
			} else if (transitionParams.animationType == TransitionParams.TransitionAnimationType.TRANSIT) {
				overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_slide_close_enter,
						mobi.monaca.framework.psedo.R.anim.monaca_slide_close_exit);
			} else if (transitionParams.animationType == TransitionParams.TransitionAnimationType.NONE) {
				overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_none, mobi.monaca.framework.psedo.R.anim.monaca_none);
			}
		}
	}

	public void _popPage() {
		int pageNum = MonacaApplication.getPages().size();
		finish();

		if (pageNum > 1) {
			// dirty fix for android4's strange bug
			overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_slide_close_enter, mobi.monaca.framework.psedo.R.anim.monaca_slide_close_exit);
		}
	}

	public void dismissPage() {
		int pageNum = MonacaApplication.getPages().size();
		finish();

		if (pageNum > 1) {
			overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_dialog_close_enter, mobi.monaca.framework.psedo.R.anim.monaca_dialog_close_exit);
		}
	}

	public void popPageAsync() {
		handler.postAtFrontOfQueue(new Runnable() {
			@Override
			public void run() {
				popPage();
			}
		});
	}

	public void popPageAsync(final TransitionParams params) {
		handler.postAtFrontOfQueue(new Runnable() {
			@Override
			public void run() {

				if (params.animationType == TransitionParams.TransitionAnimationType.POP) {
					_popPage();
				} else if (params.animationType == TransitionParams.TransitionAnimationType.DISMISS) {
					dismissPage();
				} else {
					_popPage();
				}
			}
		});
	}

	public void goHomeAsync(JSONObject options) {
		final String homeUrl = getHomeUrl(options);
		MyLog.v(TAG, "homeurl:" + homeUrl);

		handler.post(new Runnable() {
			@Override
			public void run() {
				pushPage(homeUrl, new TransitionParams(TransitionParams.TransitionAnimationType.NONE, null, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
				sendBroadcast(new ClosePageIntent());
				finish();
				overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_none, mobi.monaca.framework.psedo.R.anim.monaca_none);
			}
		});
	}

	protected String getHomeUrl(JSONObject options) {
		if (options == null) {
			return "file:///android_asset/www/index.html";
		}
		return options.optString("url", "").equals("") ? "file:///android_asset/www/index.html" : uri + "/../" + options.optString("url");
	}

	protected WebViewClient createWebViewClient(String url, MonacaPageActivity page, CordovaWebView webView) {
		if (Integer.valueOf(android.os.Build.VERSION.SDK) < 11) {
			return new MonacaPageGingerbreadWebViewClient(url, page, webView);
		} else {
			return new MonacaPageHoneyCombWebViewClient(url, page, webView);
		}
	}

	/** Build a http client instance. */
	protected DefaultHttpClient buildHttpClient() {
		DefaultHttpClient httpClient = MonacaApplication.buildHttpClient();

		if (hasAuthInfo()) {
			httpClient.getCredentialsProvider().setCredentials(new AuthScope(Monaca.DAV_DOMAIN, 80), new UsernamePasswordCredentials(userId, password));
			httpClient.getCredentialsProvider().setCredentials(new AuthScope(Monaca.DAV_DOMAIN, 443), new UsernamePasswordCredentials(userId, password));
		}

		return httpClient;
	}

	@Override
	protected void onStop() {
		super.onStop();
		unloadBackground();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Log.d(getClass().getSimpleName(), "onConfigurationChanged()");

		// handling orieantation change for background image.
		if (background != null) {
			loadBackground(newConfig);
			setupBackground();
			if (background != null) {
				background.invalidateSelf();
			}
		}

		uiContext.fireOnRotateListeners(newConfig.orientation);

		appView.clearView();
		appView.invalidate();

		Display display = getWindowManager().getDefaultDisplay();
		Log.d(getClass().getSimpleName(), "metrics width: " + display.getWidth() + ", height: " + display.getHeight());
	}

	protected String getUIFileUrl(String url) {
		if (url.endsWith(".html")) {
			return url.substring(0, url.length() - 4) + "ui";
		}

		if (url.endsWith(".htm")) {
			return url.substring(0, url.length() - 3) + "ui";
		}

		return null;
	}

	public boolean setHttpAuthUsernamePassword(String host, String realm, String username, String password) {
		if (null == appView) {
			return false;
		}

		appView.setHttpAuthUsernamePassword(host, realm, username, password);

		return true;
	}

	/*
	 * Called from WebViewClient -> can be used in DeubggerPageActivity to
	 * publish log message
	 */
	public void onLoadResource(WebView view, String url) {
	}

	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	}

	/**
	 * Try loading as application html.
	 * 
	 * @param uri
	 *            URI string to load as application html.
	 * @return succeed to load the uri or not.
	 */
	public boolean tryLoadUrlAsApplicationHtml(String uri) {
		if (uri.startsWith("file:///android_asset/") || uri.startsWith("file://" + getApplicationInfo().dataDir + "/www/")) {
			this.uri = uri;
			loadCurrentUriWithoutUIFile();
			return true;
		}

		return false;
	}
}
