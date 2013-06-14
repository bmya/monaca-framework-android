package mobi.monaca.framework.nativeui.menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mobi.monaca.framework.bootloader.LocalFileBootloader;
import mobi.monaca.framework.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/** This class build MenuRepresentation instace from json file. */
public class MenuRepresentationBuilder {

    private static final String TAG = MenuRepresentationBuilder.class.getSimpleName();
	protected Context context;

    public MenuRepresentationBuilder(Context context) {
        this.context = context;
    }


    public Map<String, MenuRepresentation> build(Context context,
            JSONObject json) {
        HashMap<String, MenuRepresentation> map = new HashMap<String, MenuRepresentation>();
        MenuRepresentationBuilder builder = getMenuRepresentationBuilder(context);

        for (Iterator<?> keys = json.keys(); keys.hasNext();) {
            String key = String.valueOf(keys.next());
            map.put(key, builder.buildMenu(json.optJSONArray(key)));
        }

        return map;
    }

    // To be overridden in Debugger
	protected MenuRepresentationBuilder getMenuRepresentationBuilder(Context context) {
		return new MenuRepresentationBuilder(
                context);
	}

    /** Build MenuRepresentation object from JSONArray. */
    protected MenuRepresentation buildMenu(JSONArray menu) {
        if (menu == null) {
            menu = new JSONArray();
        }

        ArrayList<MenuItemRepresentation> itemList = new ArrayList<MenuItemRepresentation>();

        for (int i = 0; i < menu.length(); i++) {
            itemList.add(buildMenuItem(menu.optJSONObject(i)));
        }

        return new MenuRepresentation(itemList);
    }

    /** Build MenuItemRepresentation object from JSONArray. */
    protected MenuItemRepresentation buildMenuItem(JSONObject json) {
        MenuItemRepresentation menuItem = new MenuItemRepresentation(
                json.optString("name", ""));
        menuItem.setAction(json.optString("action", ""));
        menuItem.setIconImagePath(getWWWPath() + json.optString("image", ""));

        return menuItem;
    }

    // Overriden in Debugger to provide project directory.
    // Using in setIconImagePath
    protected String getWWWPath() {
		return "";
	}

    public Map<String, MenuRepresentation> buildFromAssets(
            Context context, String jsonFilePath) {
        String jsonString = getStringFromAssets(context,
                jsonFilePath);

        if(jsonString.trim().equals("")){
        	 return build(context, new JSONObject());
        }

        try {
            return build(context, new JSONObject(jsonString));
        } catch (JSONException e) {
        	MyLog.e(MenuRepresentationBuilder.class.getSimpleName(),
                    "app.menu loading fail: " + jsonFilePath  + ". " + e);
            return build(context, new JSONObject());
        }
    }

    protected String getStringFromAssets(Context context,
            String assetFilePath) {
        InputStream stream;
        try {
        	stream = LocalFileBootloader.openAsset(context, assetFilePath);
        } catch (IOException e) {
        	MyLog.e(TAG, "exception in getStringFromAssets");
            return "";
        }
        InputStreamReader reader = new InputStreamReader(stream);
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

}
