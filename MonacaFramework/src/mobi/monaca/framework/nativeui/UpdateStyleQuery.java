package mobi.monaca.framework.nativeui;

import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateStyleQuery {

    public final JSONArray ids;
    public final JSONObject style;

    public UpdateStyleQuery(JSONArray ids, JSONObject style) {
        this.ids = ids;
        this.style = style;
    }

    @Override
    public String toString() {
    	return "ids:" + ids + ", style:" + style;
    }
}
