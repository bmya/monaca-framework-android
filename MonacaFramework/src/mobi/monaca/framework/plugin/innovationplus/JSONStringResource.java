package mobi.monaca.framework.plugin.innovationplus;

import java.io.Serializable;

import jp.innovationplus.ipp.jsontype.IPPApplicationResource;

public class JSONStringResource extends IPPApplicationResource implements Serializable{
	public static final long serialVersionUID = 7457109045867162242L;
	public String jsonString;
	public String resourceName = "JSONObjectResource";

	public JSONStringResource() {
		super();
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getJsonString() {
		return jsonString;
	}
	@Override
	public String getResourceName() {
		return resourceName;
	}

}
