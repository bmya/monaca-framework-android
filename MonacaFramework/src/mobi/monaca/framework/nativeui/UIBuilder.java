package mobi.monaca.framework.nativeui;

import static mobi.monaca.framework.nativeui.UIUtil.reportInvalidComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mobi.monaca.framework.nativeui.component.BackButtonComponent;
import mobi.monaca.framework.nativeui.component.ButtonComponent;
import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.component.LabelComponent;
import mobi.monaca.framework.nativeui.component.SearchBoxComponent;
import mobi.monaca.framework.nativeui.component.SegmentComponent;
import mobi.monaca.framework.nativeui.component.ToolbarComponent;
import mobi.monaca.framework.nativeui.container.TabbarContainer;
import mobi.monaca.framework.nativeui.container.TabbarItem;
import mobi.monaca.framework.nativeui.container.ToolbarContainer;
import mobi.monaca.framework.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

/** JSONをパースしてネイティブUIを構築するクラス */
public class UIBuilder {

	public class ResultSet {
		public View topView;
		public View bottomView;
		public Component top = null;
		public Component bottom = null;
		public ComponentEventer backButtonEventer = null;
		public HashMap<String, Component> dict;
		public UIEventer eventer;
		public String menuName;
		public JSONObject pageStyle;

		public String toString() {
			return "menuName:" + menuName + ", topView:" + topView
					+ ", bottomView:" + bottomView + ", top:" + top
					+ ", bottom" + bottom + ", backButtonEventer:" + backButtonEventer
					+ ", eventer:" + eventer
					+ ", dict:" + dict;
		};
	}

	private static final String TAG = UIBuilder.class.getSimpleName();

	protected UIContext context;
	protected JSONObject uiJSON;

	public UIBuilder(UIContext context, JSONObject uiJSON) {
//		MyLog.v(TAG, "UIBuilder. constructor: uiJson:" + uiJSON);
		this.context = context;
		this.uiJSON = uiJSON;
	}

	public List<ToolbarComponent> buildToolbarComponents(JSONArray json, ResultSet resultSet) {
//		MyLog.v(TAG, "buildToolbarComponents(). json=" + json + ", resultSet:" + resultSet);
		ArrayList<ToolbarComponent> result = new ArrayList<ToolbarComponent>();

		if (json != null) {
			for (int i = 0; i < json.length(); i++) {
				JSONObject obj = json.optJSONObject(i);
				for (ToolbarComponent component : buildToolbarComponent(obj, resultSet)) {
					result.add(component);
				}
			}
		}

		return result;
	}

	public List<ToolbarComponent> buildToolbarComponent(JSONObject json, ResultSet resultSet) {
//		MyLog.v(TAG, "buildToolbarComponent(). json=" + json + ", resultSet:" + resultSet);
		if (json == null) {
			return Collections.emptyList();
		}

		ToolbarComponent component;
		String componentId = json.optString("component");

		if (componentId.equals("backButton")) {
			resultSet.backButtonEventer = new ComponentEventer(context, json.optJSONObject("event"));

			component = new BackButtonComponent(context, buildStyleJSONObject(json), new ComponentEventer(context, json.optJSONObject("event")));
		} else if (componentId.equals("button")) {
			component = new ButtonComponent(context, buildStyleJSONObject(json), new ComponentEventer(context, json.optJSONObject("event")));
		} else if (componentId.equals("searchBox")) {
			component = new SearchBoxComponent(context, buildStyleJSONObject(json), new ComponentEventer(context, json.optJSONObject("event")));
		} else if (componentId.equals("label")) {
			component = new LabelComponent(context, buildStyleJSONObject(json));
		} else if (componentId.equals("segment")) {
			component = new SegmentComponent(context, buildStyleJSONObject(json), new ComponentEventer(context, json.optJSONObject("event")));
		} else {
			// fail
			return Collections.emptyList();
		}

		if (json.optString("id", "").length() > 0) {
			resultSet.dict.put(json.optString("id", ""), component);
		}

		ArrayList<ToolbarComponent> result = new ArrayList<ToolbarComponent>();
		result.add(component);
		return result;
	}

	public View buildToolbar(JSONObject json, boolean isTop, ResultSet resultSet) {
//		MyLog.v(TAG, "buildToolbar(). json=" + json + ", isTop=" + isTop + ", resultSet:" + resultSet);
		if (json == null) {
			return null;
		}

		if (!json.has("container")) {
			UIUtil.reportInvalidContainer(context, "Cannot buildToolbar because json has no key:'container'");
		}

		if (json.optString("container").equals("toolbar")) {

			ToolbarContainer toolbar = new ToolbarContainer(context, buildToolbarComponents(json.optJSONArray("left"), resultSet),
																	buildToolbarComponents(json.optJSONArray("center"), resultSet),
																	buildToolbarComponents(json.optJSONArray("right"), resultSet),
																	buildStyleJSONObject(json));
			if (json.optString("id", "").length() > 0) {
				resultSet.dict.put(json.optString("id", ""), toolbar);
			}

			if (isTop) {
				resultSet.top = toolbar;
			} else {
				resultSet.bottom = toolbar;
			}
			return toolbar.getView();
		}

		if (json.optString("container").equals("tabbar")) {

			TabbarContainer tabbar = new TabbarContainer(context, buildTabbarItems(json.optJSONArray("items"), resultSet), buildStyleJSONObject(json));
			if (json.optString("id").length() > 0) {
				resultSet.dict.put(json.optString("id"), tabbar);
			}

			if (isTop) {
				resultSet.top = tabbar;
			} else {
				resultSet.bottom = tabbar;
			}
			return tabbar.getView();
		}

		return null;
	}

	protected List<TabbarItem> buildTabbarItems(JSONArray json, ResultSet resultSet) {
//		MyLog.v(TAG, "buildTabbarItems(). json=" + json + ", resultSet:" + resultSet);
		if (json == null) {
			return new ArrayList<TabbarItem>();
		}

		ArrayList<TabbarItem> items = new ArrayList<TabbarItem>();
		for (int i = 0; i < json.length(); i++) {
			TabbarItem item = buildTabbarItem(json.optJSONObject(i), resultSet);
			if (item != null) {
				items.add(item);
			}
		}

		return items;
	}

	protected TabbarItem buildTabbarItem(JSONObject json, ResultSet resultSet) {
//		MyLog.v(TAG, "buildTabbarItem(). json=" + json + ", resultSet:" + resultSet);
		String componentId = json.optString("component");
		JSONObject style = buildStyleJSONObject(json);

		if (componentId.equals("tabbarItem")) {
			TabbarItem item = new TabbarItem(context, json.optString("link"), style);
			if (json.optString("id").length() > 0) {
				resultSet.dict.put(json.optString("id"), item);
			}
			return item;
		}

		reportInvalidComponent(context, "no such tabbar component: " + componentId);

		return null;
	}

	public ResultSet build() {
		MyLog.v(TAG, "build()");
		ResultSet resultSet = new ResultSet();
		resultSet.dict = new HashMap<String, Component>();
		resultSet.topView = buildToolbar(uiJSON.optJSONObject("top"), true, resultSet);
		resultSet.bottomView = buildToolbar(uiJSON.optJSONObject("bottom"), false, resultSet);
		resultSet.eventer = new UIEventer(context, uiJSON.optJSONObject("event"));
		resultSet.menuName = uiJSON.optString("menu", "");
		resultSet.pageStyle = uiJSON.optJSONObject("style");

		return resultSet;
	}

	public class UIBuilderException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UIBuilderException() {
			super();
		}

		public UIBuilderException(String msg) {
			super(msg);
		}
	}

	protected JSONObject buildStyleJSONObject(JSONObject component) {
//		MyLog.v(TAG, "buildStyleJSONObject(). component=" + component);
		JSONObject style = component.optJSONObject("style");
		style = style != null ? style : new JSONObject();

		JSONObject androidStyle = component.optJSONObject("androidStyle");
		androidStyle = androidStyle != null ? androidStyle : new JSONObject();

		JSONObject result = new JSONObject();

		Iterator<String> keys = style.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			try {
				result.put(key, style.get(key));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		keys = androidStyle.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			try {
				result.put(key, androidStyle.get(key));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}

		return result;
	}
}
