package mobi.monaca.framework.plugin.innovationplus;

import jp.innovationplus.ipp.jsontype.IPPApplicationResource;

import org.json.JSONObject;

public class JSONObjectResource extends IPPApplicationResource {
	private JSONObject json;

	public JSONObjectResource() {
		super();
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public JSONObject getJson() {
		return json;
	}
	@Override
	public String getResourceName() {
		return "JSONObjectResource";
	}

}
