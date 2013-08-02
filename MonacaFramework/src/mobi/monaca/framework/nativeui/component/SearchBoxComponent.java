package mobi.monaca.framework.nativeui.component;

import static mobi.monaca.framework.nativeui.UIUtil.TAG;
import static mobi.monaca.framework.nativeui.UIUtil.buildOpacity;
import static mobi.monaca.framework.nativeui.UIUtil.dip2px;
import static mobi.monaca.framework.nativeui.UIUtil.updateJSONObject;
import mobi.monaca.framework.nativeui.ComponentEventer;
import mobi.monaca.framework.nativeui.DefaultStyleJSON;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.nativeui.UIValidator;
import mobi.monaca.framework.nativeui.exception.NativeUIException;
import mobi.monaca.framework.psedo.R;
import mobi.monaca.framework.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Configuration;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class SearchBoxComponent extends ToolbarComponent implements UIContext.OnRotateListener {

	protected EditText searchEditText;
	protected FrameLayout layout;
	protected Button clearButton;
	protected ComponentEventer eventer;

	protected static final String[] SEARCH_BOX_VALID_KEYS = { "component", "style", "iosStyle", "androidStyle", "id", "event" };
	protected static final String[] STYLE_VALID_KEYS = { "visibility", "disable", "opacity", "backgroundColor", "textColor", "placeholder", "focus", "value" };

	@Override
	public String[] getValidKeys() {
		return SEARCH_BOX_VALID_KEYS;
	}

	public SearchBoxComponent(UIContext context, JSONObject searchBoxJSON) throws NativeUIException, JSONException {
		super(context, searchBoxJSON);
		UIValidator.validateKey(context, getComponentName() + " style", style, STYLE_VALID_KEYS);
		
		buildEventer();
		initView();
		style();

		try {
			searchBoxJSON.put("focus", false);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		context.addOnRotateListener(this);
	}

	private void buildEventer() throws NativeUIException, JSONException {
		this.eventer = new ComponentEventer(uiContext, getComponentJSON().optJSONObject("event"));
	}

	@Override
	public void onRotate(int orientation) {
		updateWidthForOrientation(orientation);
	}

	protected void updateWidthForOrientation(int orientation) {
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			searchEditText.setWidth(UIUtil.dip2px(uiContext, 136));
		} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			searchEditText.setWidth(UIUtil.dip2px(uiContext, 80));
		}
		searchEditText.invalidate();
	}

	public void updateStyle(JSONObject update) throws NativeUIException {
		updateJSONObject(style, update);
		style();
	}

	public JSONObject getStyle() {
		// update editTextValue
		try {
			style.put("value", searchEditText.getText().toString());
		} catch (JSONException e) {
			Log.w(TAG, "update value failed");
		}
		return style;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public View getView() {
		return layout;
	}

	protected void initView() {
		layout = new FrameLayout(uiContext);

		searchEditText = new EditText(uiContext);
		searchEditText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
					SpannableStringBuilder sp = (SpannableStringBuilder) searchEditText.getText();
					String keyword = sp.toString();
					eventer.onSearch(searchEditText, keyword);
					return true;
				}
				return false;
			}
		});
		searchEditText.setInputType(InputType.TYPE_CLASS_TEXT);
		searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		searchEditText.setBackgroundResource(R.drawable.monaca_searchbox_bg);
		searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				try {
					style.put("focus", hasFocus);
				} catch (JSONException e) {
					MyLog.e(TAG, e.getMessage());
				}
			}
		});
		searchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				clearButton.setVisibility(searchEditText.getText().toString().equals("") ? View.GONE : View.VISIBLE);
			}
		});

		layout.addView(searchEditText);

		clearButton = new Button(uiContext);
		clearButton.setBackgroundResource(R.drawable.monaca_search_clear);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchEditText.setText("");
				searchEditText.requestFocus();
				uiContext.showSoftInput(searchEditText);
			}
		});

		Drawable drawable = uiContext.getResources().getDrawable(R.drawable.monaca_search_clear);
		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(drawable.getMinimumWidth(), drawable.getMinimumHeight(), Gravity.RIGHT
				| Gravity.CENTER_VERTICAL);
		layout.addView(clearButton, p);
	}

	/*
	 * style memo: value placeHolder visibility disable textColor opacity
	 * backgroundColor focus
	 */
	protected void style() throws NativeUIException {
		searchEditText.setText(style.optString("value", ""));
		searchEditText.setHint(style.optString("placeHolder", ""));
		searchEditText.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE : View.INVISIBLE);
		searchEditText.setEnabled(!style.optBoolean("disable", false));

		int color = UIValidator.parseAndValidateColor(uiContext, getComponentName() + " style", "textColor", "#000000", style);
		searchEditText.setTextColor(color);
		float opacity = UIValidator.parseAndValidateFloat(uiContext, getComponentName() + " style", "opacity", "1.0", style, 0.0f, 1.0f);
		int integerOpacity = buildOpacity(opacity);
		searchEditText.setTextColor(searchEditText.getTextColors().withAlpha(integerOpacity));
		searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, uiContext.getFontSizeFromDip(Component.LABEL_TEXT_DIP));

		searchEditText.setWidth(dip2px(uiContext, 65));

		if (style.has("backgroundColor")) {
			int backgroundColor = UIValidator.parseAndValidateColor(uiContext, getComponentName() + " style", "backgroundColor", "#ffffff", style);
			searchEditText.getBackground().setColorFilter(backgroundColor, Mode.MULTIPLY);
		}

		searchEditText.getBackground().setAlpha(integerOpacity);
		clearButton.getBackground().setAlpha(integerOpacity);
		searchEditText.setHintTextColor(searchEditText.getHintTextColors().withAlpha(integerOpacity));
		updateWidthForOrientation(uiContext.getUIOrientation());
	}

	@Override
	public String getComponentName() {
		return "SearchBox";
	}

	@Override
	public JSONObject getDefaultStyle() {
		return DefaultStyleJSON.searchBox();
	}

}
