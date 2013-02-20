package mobi.monaca.utils.gcm;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class GCMPushDataset implements Serializable{
	private static final long serialVersionUID = -2053523995979076876L;
	public static final String KEY = "get_pushdata_key";

	private String pushProjectId;
	private String message;
	private String extraJsonString;

	public GCMPushDataset(String pushProjectId, String message, String extraJsonString) {
		this.pushProjectId = pushProjectId;
		this.message = message;
		this.extraJsonString = extraJsonString;
	}

	public JSONObject getExtraJSON(){
		try {
			return new JSONObject(extraJsonString);
		} catch (JSONException e) {
			return null;
		}
	}
}
