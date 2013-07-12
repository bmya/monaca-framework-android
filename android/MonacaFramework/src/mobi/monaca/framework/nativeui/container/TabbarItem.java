package mobi.monaca.framework.nativeui.container;

import static mobi.monaca.framework.nativeui.UIUtil.TAG;
import static mobi.monaca.framework.nativeui.UIUtil.dip2px;
import static mobi.monaca.framework.nativeui.UIUtil.updateJSONObject;

import java.io.IOException;

import mobi.monaca.framework.nativeui.DefaultStyleJSON;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.nativeui.UIValidator;
import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.exception.NativeUIException;
import mobi.monaca.framework.nativeui.exception.NativeUIIOException;
import mobi.monaca.framework.psedo.R;
import mobi.monaca.framework.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabbarItem extends Component {

	protected Drawable drawable;
	protected TabbarItemView view;
	protected String link;
	protected Handler handler;

	protected static final String[] TABBAR_ITEM_VALID_KYES = { "component", "style", "link", "id" };
	protected static final String[] STYLE_VALID_KYES = { "text", "backgroundColor", "image", "badgeText" };

	@Override
	public String[] getValidKeys() {
		return TABBAR_ITEM_VALID_KYES;
	}

	public TabbarItem(UIContext context, JSONObject tabbarItemJSON) throws NativeUIException, JSONException {
		super(context, tabbarItemJSON);
		UIValidator.validateKey(context, getComponentName() + " style", style, STYLE_VALID_KYES);
		this.view = new TabbarItemView(context);
		this.link = tabbarItemJSON.optString("link");
		this.handler = new Handler();

		style();
	}

	public TabbarItemView getView() {
		return view;
	}

	@Override
	public void updateStyle(JSONObject update) throws NativeUIIOException {
		updateJSONObject(style, update);
		style();
	}

	/*
	 * text: テキスト [string] (default: "") image: 画像への相対パス [string] (default: "")
	 * badgeText: バッジのテキスト [string] (default: "")
	 */
	protected void style() throws NativeUIIOException {
		view.setText(style.optString("text"));
		view.setBadgeText(style.optString("badgeText"));

		Bitmap bitmap = readImage("image");
		if(bitmap != null){
			view.setIconBitmap(bitmap);
		}
	}
	
	private Bitmap readImage(String imageKeyName) throws NativeUIIOException {
		String imagePath = style.optString(imageKeyName);
		if (!TextUtils.isEmpty(imagePath)) {
			try {
				Bitmap bitmap = uiContext.readScaledBitmap(imagePath);
				return bitmap;
			} catch (IOException e) {
				NativeUIIOException exception = new NativeUIIOException(getComponentName() + " style", imageKeyName, imagePath, e.getMessage());
				throw exception;
			}
		}
		return null;
	}

	public class TabbarItemView extends LinearLayout {

		protected TextView textView;
		protected boolean isSelected = true;
		protected TextView badgeTextView;
		protected ImageView imageView;

		public TabbarItemView(UIContext context) {
			super(context);

			setOrientation(LinearLayout.VERTICAL);

			FrameLayout layout = new FrameLayout(context);

			setBackgroundResource(R.drawable.monaca_selected_tab_bg);

			textView = new TextView(context);
			textView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
			textView.setTextColor(0xffffffff);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getFontSizeFromDip(Component.TAB_TEXT_DIP));
			textView.setShadowLayer(1f, 0f, -1f, 0xcc000000);

			imageView = new ImageView(context);
			imageView.setPadding(UIUtil.dip2px(context, 16), 0, UIUtil.dip2px(context, 16), 0);
			imageView.setColorFilter(0xffffffff, PorterDuff.Mode.SRC_IN);

			badgeTextView = new TextView(context);
			badgeTextView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
			badgeTextView.setVisibility(View.GONE);
			badgeTextView.setBackgroundColor(0xff990000);
			badgeTextView.setTextColor(0xffffffff);
			badgeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getFontSizeFromDip(Component.TAB_BADGE_TEXT_DIP));
			badgeTextView.setShadowLayer(1.0f, 0f, 1f, 0x99000000);
			badgeTextView.setTypeface(Typeface.DEFAULT_BOLD);
			badgeTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.monaca_tab_badge));

			layout.addView(imageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

			layout.addView(badgeTextView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,
					Gravity.RIGHT));

			setGravity(Gravity.CENTER);

			addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

			addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

			switchToUnselected();
		}

		public void setIconBitmap(Bitmap bitmap) {
			bitmap = UIUtil.resizeBitmap(bitmap, dip2px(uiContext, 28));
			Drawable icon = new BitmapDrawable(uiContext.getResources(), bitmap);
			icon.setColorFilter(isSelected() ? 0xffffffff : 0x99ffffff, PorterDuff.Mode.SRC_IN);
			imageView.setImageDrawable(icon);
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			imageView.setImageDrawable(null);
		}

		public void setBadgeText(String text) {
			badgeTextView.setText(text);

			if (text.length() == 0) {
				badgeTextView.setVisibility(View.GONE);
			} else {
				badgeTextView.setVisibility(View.VISIBLE);
			}
		}

		public void setText(String text) {
			textView.setText(text);
		}

		public void setTextColor(int color) {
			textView.setTextColor(color);
		}

		public void initializeSelected() {
			if (!isSelected) {
				getBackground().setAlpha(0x33);

				textView.setTextColor(0xffffffff);
				isSelected = true;

				uiContext.changeCurrentUri(link);
			}
			imageView.setAlpha(0xff);
			invalidate();
		}

		public void switchToSelected() {
			MyLog.v(TAG, "switchToSelected() link:" + link);
			if (!isSelected) {
				getBackground().setAlpha(0x33);

				textView.setTextColor(0xffffffff);
				isSelected = true;
				handler.post(new Runnable() {
					@Override
					public void run() {
						uiContext.loadRelativePathWithoutUIFile(link);
					}
				});
			}
			imageView.setAlpha(0xff);
			invalidate();
		}

		public void switchToUnselected() {
			if (isSelected) {
				getBackground().setAlpha(0);

				textView.setTextColor(0x99ffffff);
				isSelected = false;
			}

			imageView.setAlpha(0x66);
			invalidate();
		}

		public boolean isSelected() {
			return isSelected;
		}
	}

	@Override
	public String getComponentName() {
		return TabbarItem.class.getSimpleName();
	}

	@Override
	public JSONObject getDefaultStyle() {
		return DefaultStyleJSON.tabbarItem();
	}

}
