package mobi.monaca.framework.view;

import java.io.IOException;

import mobi.monaca.framework.util.MyLog;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.LOG;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class MonacaWebView extends CordovaWebView {
	public static String TAG = MonacaWebView.class.getSimpleName();

	protected Context context;
	private boolean notBackButton = true;

	public MonacaWebView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
		this.context = context;
		init();
	}
	public MonacaWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	public MonacaWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	public MonacaWebView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	protected void init() {
		loadCordovaConfigurationFromManifest();
		notBackButton = true;
	}

	@Override
	public void goBack() {
		MyLog.d(TAG, "called goBack");
		super.goBack();
	}

	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
		MyLog.d(TAG, "onKeyUp");
		MyLog.d(TAG, "canGoBack is " + Boolean.toString(this.canGoBack()));

		//to prevent from calling backHistory() by backbutton
		// TODO find smarter way
		notBackButton = !(this.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK);

		boolean supersReturn = super.onKeyUp(keyCode, event);
		MyLog.d(TAG, "onKeyUp value is " + Boolean.toString(supersReturn));

		notBackButton = true;
		return supersReturn;
	}

	@Override
	public boolean backHistory() {
		MyLog.d(TAG, "backHistory()");
		//to prevent from calling backHistory() by backbutton
		// TODO find smarter way
		if (notBackButton) {
			return super.backHistory();
		} else {
			notBackButton = true;
			return false;
		}
	}


	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// MonacaPageActivity uses BACK_BUTTON
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void loadCordovaConfigurationFromManifest() {
        AssetManager am;
        XmlResourceParser parser = null;

		try {
			am = context.createPackageContext(context.getPackageName(), 0).getAssets();
			parser = am.openXmlResourceParser("AndroidManifest.xml");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		parseCordovaTagFromXml(parser);
	}

	public void parseCordovaTagFromXml(XmlPullParser parser) {

		if (parser == null) {
			return;
		}

		 int eventType;
		 String tag;
         try {
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				tag = parser.getName();
			     if (eventType == XmlPullParser.START_TAG && "cordova".equals(tag)) {
			    	 MyLog.d(TAG,"found cordova tag");
						boolean continues = true;

						while (continues) {
							switch (eventType) {
							case XmlPullParser.START_TAG:
								tag = parser.getName();

								if ("access".equals(tag)) {
									String origin = parser.getAttributeValue(null, "origin");
									String subdomains = parser.getAttributeValue(null, "subdomains");

									 if (origin != null) {
										 addWhiteListEntry(origin, subdomains != null && subdomains.compareToIgnoreCase("true") == 0);
									 }

									MyLog.d(TAG, "addWhiteList : " +  origin);
								}

								if ("log".equals(tag)) {
					                   String level = parser.getAttributeValue(null, "level");
					                   if (level != null) {
					                        LOG.setLogLevel(level);
					                   }
								}
								if ("preference".equals(tag)) {
				                    String name = parser.getAttributeValue(null, "name");
				                    String value = parser.getAttributeValue(null, "value");

				                    LOG.i("CordovaLog", "Found preference for %s=%s", name, value);
				                    MyLog.d("CordovaLog", "Found preference for " + name + "=" + value);

				                    // Save preferences in Intent
				                    try {
				                    	((Activity)context).getIntent().putExtra(name, value);
				                    } catch (Exception e) {

				                    }
				                }
								break;
							case XmlPullParser.END_TAG:
								tag = parser.getName();
								continues = !"cordova".equals(tag);
								break;
							}
							eventType = parser.next();
						}
					}
			 }
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
