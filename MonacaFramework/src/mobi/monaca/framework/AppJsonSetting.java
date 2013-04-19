package mobi.monaca.framework;

import mobi.monaca.framework.util.MyLog;
import mobi.monaca.utils.TimeStamp;
import mobi.monaca.utils.log.LogItem;
import mobi.monaca.utils.log.LogItem.LogLevel;
import mobi.monaca.utils.log.LogItem.Source;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;

/**
 * this represents application setting set in appJson
 */
public class AppJsonSetting {
	protected JSONObject appJson;

	private boolean sendsBroadcast = false;
	private Context context;

	//push
	protected String pushProjectId = "";
	protected String senderId = "";

	//splash
	protected int splashColor;
	protected boolean autoHide;

	//security
	protected boolean disableCookie;

	public AppJsonSetting(JSONObject appJson) {
		this.appJson = appJson;
		parseSplash();
		parsePush();
		parseSecurity();
	}

	protected void parseSplash() {
		JSONObject splash;
		try {
			splash =  appJson.getJSONObject("splash").getJSONObject("android");
		} catch (JSONException e) {
			splash = new JSONObject();
		}

		String backgroundColorString = splash.optString("background", "#00FFFFFF"); //#00FFFFFF means Color.TRANSPARENT
		if (!backgroundColorString.startsWith("#")) {
			backgroundColorString = "#" + backgroundColorString;
		}
		try {
			splashColor = Color.parseColor(backgroundColorString);
		} catch (IllegalArgumentException e) {
			if (sendsBroadcast && context != null) {
				String logMessage = "Invalid color string:" + backgroundColorString + ". Please correct app.json file";
				LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.DEBUG, logMessage, "", 0);
				MyLog.sendBroadcastDebugLog(context, logItem);
			}
			splashColor = Color.TRANSPARENT;
		}

		autoHide = splash.optBoolean("autoHide", true);
	}
	protected void parsePush() {
		JSONObject puthNotification;
		try {
			puthNotification = appJson.getJSONObject("pushNotification");
		} catch (JSONException e1) {
			puthNotification = new JSONObject();
		}

		pushProjectId = puthNotification.optString("pushProjectId", "");
		try {
			senderId = puthNotification.getJSONObject("android").optString("senderId", "");
		} catch (JSONException e) {
		}
	}

	protected void parseSecurity() {
		JSONObject security;
		try {
			security = appJson.getJSONObject("security");
		} catch (JSONException e) {
			security = new JSONObject();
		}

		disableCookie = security.optBoolean("disableCookie", false);
	}

	/**
	 * set flag to send BroadcastDebugLog. used for validation
	 * @param sendsBroadcast
	 * @param context
	 */
	public void sendsBroadcastDebugLog(boolean sendsBroadcast, Context context) {
		this.sendsBroadcast = sendsBroadcast;
		this.context = context;
	}

	public boolean getAutoHide() {
		return autoHide;
	}
	public String getSenderId() {
		return senderId;
	}

	public String getPushProjectId() {
		return pushProjectId;
	}

	public int getSplashBackgroundColor() {
		return splashColor;
	}

	public boolean getDisableCookie() {
		return disableCookie;
	}

}
