package mobi.monaca.framework.nativeui.container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mobi.monaca.framework.nativeui.DefaultStyleJSON;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.component.view.ContainerShadowView;
import mobi.monaca.framework.nativeui.container.TabbarItem.TabbarItemView;
import mobi.monaca.framework.nativeui.exception.NativeUIException;
import mobi.monaca.framework.nativeui.exception.RequiredKeyNotFoundException;
import mobi.monaca.framework.psedo.R;
import mobi.monaca.framework.util.MyLog;
import static mobi.monaca.framework.nativeui.UIUtil.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
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
	protected UIContext context;
	protected Integer oldActiveIndex = null;
	protected static final int mContainerViewID = 1002;

	protected static String[] validKeys = {
		"container",
		"id",
		"style",
		"items"
	};

	@Override
	public String[] getValidKeys() {
		return validKeys;
	}

	public TabbarContainer(UIContext context, JSONObject tabbarJSON) throws NativeUIException {
		super(tabbarJSON);
		this.context = context;
		this.view = new TabbarContainerView(context);
		this.view.setId(mContainerViewID);
		shadowView = new ContainerShadowView(context, false);

		buildChildren();

		style();
	}

	private void buildChildren() throws NativeUIException {
		JSONArray itemsJSON = componentJSON.optJSONArray("items");
		if(itemsJSON != null){
			for (int i = 0; i < itemsJSON.length(); i++) {
				TabbarItem tabbarItem = new TabbarItem(context, itemsJSON.optJSONObject(i));
				view.addTabbarItemView(tabbarItem.getView());
			}
		}else{
			throw new RequiredKeyNotFoundException(getComponentName(), "items");
		}

	}

	public void updateStyle(JSONObject update) {
		oldActiveIndex = style.has("activeIndex") ? style.optInt("activeIndex", 0) : null;
		updateJSONObject(style, update);
		style();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		view.getContentView().setBackgroundDrawable(null);
	}

	/*
	 * visibility: true / false [bool] (default: true) opacity: 0.0～1.0 [float]
	 * (default: 1.0) backgroundColor: #000000 [string] (default: #000000)
	 * activeIndex: 0 [int] (default: 0)
	 */
	protected void style() {
		view.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE : View.GONE);

		ColorFilter filter = new PorterDuffColorFilter(buildColor(style.optString("backgroundColor", "#000000")), PorterDuff.Mode.SCREEN);
		Bitmap bgBitmap = UIUtil.createBitmapWithColorFilter(view.getContentView().getBackground(), filter);

		view.getContentView().setBackgroundResource(R.drawable.monaca_tabbar_bg);
		view.getContentView().setBackgroundDrawable(new BitmapDrawable(context.getResources(), bgBitmap));
		double tabbarOpacity = style.optDouble("opacity", 1.0);
		view.getContentView().getBackground().setAlpha(buildOpacity(tabbarOpacity));

		if (oldActiveIndex != null && style.optInt("activeIndex", 0) != oldActiveIndex) {
			view.setActiveIndex(style.optInt("activeIndex", 0));
		}

		double shadowOpacity = style.optDouble("shadowOpacity", 0.3);
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
		private ToolbarContainerViewListener mContainerSizeListener;

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
			View v = new FrameLayout(context);
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
					MyLog.d(TAG, "unexpected exception has occurred");
					e.printStackTrace();
				}
			}

			if (currentItemView != null) {
				currentItemView.switchToUnselected();
				currentItemView = item;
				item.switchToSelected();
			}
		}
		
		@Override
		public void setVisibility(int visibility) {
			if (getVisibility() != visibility) {
				if (mContainerSizeListener != null) {
					mContainerSizeListener.onVisibilityChanged(visibility);
				}
			}
			super.setVisibility(visibility);
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			if(mContainerSizeListener != null){
				mContainerSizeListener.onSizeChanged(w, h, oldw, oldh);
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
