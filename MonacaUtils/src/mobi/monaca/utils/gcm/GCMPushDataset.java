package mobi.monaca.utils.gcm;

import java.io.Serializable;

public class GCMPushDataset implements Serializable{
	private static final long serialVersionUID = -2053523995979076876L;

	/**
	 * used to pass GCMPushDataset between Activities
	 */
	public static final String KEY = "get_pushdata_key";

	private String pushProjectId;
	private String extraJsonString;

	public GCMPushDataset(String pushProjectId, String extraJsonString) {
		this.pushProjectId = pushProjectId;
		this.extraJsonString = extraJsonString;
	}

	public String getExtraJSONString() {
		return extraJsonString;
	}

	public String getPushProjectId() {
		return pushProjectId;
	}
}
