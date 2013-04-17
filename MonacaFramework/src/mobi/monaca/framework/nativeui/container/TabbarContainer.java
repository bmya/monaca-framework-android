package mobi.monaca.framework.nativeui.container;

import static mobi.monaca.framework.nativeui.UIUtil.buildOpacity;
import static mobi.monaca.framework.nativeui.UIUtil.updateJSONObject;

import java.util.ArrayList;

import mobi.monaca.framework.nativeui.DefaultStyleJSON;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.nativeui.UIValidator;
import mobi.monaca.framework.nativeui.component.view.ContainerShadowView;
import mobi.monaca.framework.nativeui.container.TabbarItem.TabbarItemView;
import mobi.monaca.framework.nativeui.exception.NativeUIException;
import mobi.monaca.framework.nativeui.exception.RequiredKeyNotFoundException;
import mobi.monaca.framework.psedo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class TabbarContainer extends Container {

	protected TabbarContainerView view;
	private ContainerShadowView shadowView;
	protected Integer oldActiveIndex = null;
	protected static final int mContainerViewID = 1002;

	protected static final String[] TAB_BAR_VALID_KEYS = {
		"container",
		"id",
		"style",
		"items"
	};
	protected static final String[] STYLE_VALID_KEYS = {"visibility", "opacity", "backgroundColor", "activeIndex"};

	@Override
	public String[] getValidKeys() {
		return TAB_BAR_VALID_KEYS;
	}

	public TabbarContainer(UIContext context, JSONObject tabbarJSON) throws NativeUIException, JSONException {
		super(context, tabbarJSON);
		UIValidator.validateKey(context, getComponentName() + " style", style, STYLE_VALID_KEYS);
		this.view = new TabbarContainerView(context);
		this.view.setId(mContainerViewID);
		shadowView = new ContainerShadowView(context, false);

		buildChildren();

		style();
	}

	private void buildChildren() throws NativeUIException, JSONException {
		JSONArray itemsJSON = componentJSON.optJSONArray("items");
		if(itemsJSON != null){
			for (int i = 0; i < itemsJSON.length(); i++) {
				TabbarItem tabbarItem = new TabbarItem(uiContext, itemsJSON.optJSONObject(i));
				view.addTabbarItemView(tabbarItem.getView());
			}
		}else{
			throw new RequiredKeyNotFoundException(getComponentName(), "items");
		}

	}

	public void updateStyle(JSONObject update) throws NativeUIException {
		oldActiveIndex = style.has("activeIndex") ? style.optInt("activeIndex", 0) : null;
		updateJSONObject(style, update);
		style();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(view != null && view.getContentView() != null){
			view.getContentView().setBackgroundDrawable(null);
		}
	}

	/*
	 * visibility: true / false [bool] (default: true) opacity: 0.0～1.0 [float]
	 * (default: 1.0) backgroundColor: #000000 [string] (default: #000000)
	 * activeIndex: 0 [int] (default: 0)
	 */
	protected void style() throws NativeUIException {
		view.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE : View.GONE);

		int backgroundColor = UIValidator.parseAndValidateColor(uiContext, getComponentName() + " style", "backgroundColor", "#000000", style);
		ColorFilter filter = new PorterDuffColorFilter(backgroundColor, PorterDuff.Mode.SCREEN);
		Bitmap bgBitmap = UIUtil.createBitmapWithColorFilter(view.getContentView().getBackground(), filter);

		view.getContentView().setBackgroundResource(R.drawable.monaca_tabbar_bg);
		view.getContentView().setBackgroundDrawable(new BitmapDrawable(uiContext.getResources(), bgBitmap));
		float tabbarOpacity = UIValidator.parseAndValidateFloat(uiContext, getComponentName() + " style", "opacity", "1.0", style, 0.0f, 1.0f);
		view.getContentView().getBackground().setAlpha(buildOpacity(tabbarOpacity));

		int activeIndex = UIValidator.parseAndValidateInt(uiContext, getComponentName() + " style", "activeIndex", "0", style, 0, view.getItemSize() - 1);
		if (oldActiveIndex != null && activeIndex != oldActiveIndex) {
			view.setActiveIndex(style.optInt("activeIndex", 0));
		}

		float shadowOpacity = UIValidator.parseAndValidateFloat(uiContext, getComponentName() + " style", "shadowOpacity", "0.3", style, 0.0f, 1.0f);
		double relativeShadowOpacity = tabbarOpacity * shadowOpacity;
		getShadowView().getBackground().setAlpha(buildOpacity(relativeShadowOpacity));
	}

	public View getView() {
		return view;
	}

	public class TabbarContainerView extends LinearLayout implements View.OnClickListener {

		protected ArrayList<TabbarItem.TabbarItemView> items = new ArrayList<TabbarItem.TabbarItemView>();
		protected TabbarItemView currentItemView = null;
		protected LinearLayout content;

		public TabbarContainerView(UIContext context) {
			super(context);
			setOrientation(LinearLayout.VERTICAL);

			content = new LinearLayout(context);
			content.setOrientation(LinearLayout.HORIZONTAL);

			content.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
			content.setBackgroundResource(R.drawable.monaca_tabbar_bg);

			int borderWidth = context.getSettings().disableUIContainerBorder ? 0 : 1;
            addView(createBorderView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, borderWidth));
			addView(content, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		}

		public View getContentView() {
			return content;
		}

		protected View createBorderView() {
			View v = new FrameLayout(uiContext);
			v.setBackgroundColor(0xff000000);
			return v;
		}

		public void addTabbarItemView(TabbarItem.TabbarItemView itemView) {
			items.add(itemView);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(0, 0, 0, 0);
			params.gravity = Gravity.CENTER_VERTICAL;
			params.weight = 1;

			content.addView(itemView, params);

			int activeIndex = style.optInt("activeIndex", 0);
			if (items.size() - 1 == activeIndex) {
				itemView.initializeSelected();
				currentItemView = itemView;
			}

			itemView.setOnClickListener(this);
		}

		public void setActiveIndex(int index) {
			if (items.size() <= index) {
				index = 0;
			}
			if (currentItemView != null) {
				currentItemView.switchToUnselected();
				currentItemView = null;
			}
			if (items.size() - 1 >= index) {
				currentItemView = items.get(index);
				currentItemView.switchToSelected();
			}
		}
		
		public int getItemSize(){
			return items.size();
		}

		@Override
		public void onClick(View v) {
			TabbarItemView item = (TabbarItemView) v;
			item.switchToSelected();
			item.requestFocus();

			if (items.contains(item)) {
				int newActiveIndex = items.indexOf(item);
				try {
					style.put("activeIndex", newActiveIndex);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			if (currentItemView != null) {
				currentItemView.switchToUnselected();
				currentItemView = item;
				item.switchToSelected();
			}
		}
	}

	public boolean isTransparent() {
		double opacity = style.optDouble("opacity", 1.0);
		return opacity <= 0.999;
	}

	@Override
	public View getShadowView() {
		return shadowView;
	}

	@Override
	public String getComponentName() {
		return "TabBar";
	}

	@Override
	public JSONObject getDefaultStyle() {
		return DefaultStyleJSON.tabbar();
	}
}
