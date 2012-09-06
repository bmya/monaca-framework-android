package mobi.monaca.framework.nativeui.component;

import mobi.monaca.framework.nativeui.ComponentEventer;

import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.psedo.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Configuration;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import static mobi.monaca.framework.nativeui.UIUtil.*;

public class SearchBoxComponent implements ToolbarComponent, UIContext.OnRotateListener {

    protected UIContext context;
    protected JSONObject style;
    protected EditText searchEditText;
    protected FrameLayout layout;
    protected Button clearButton;
    protected ComponentEventer eventer;

    public SearchBoxComponent(UIContext context, JSONObject style,
            final ComponentEventer eventer) {
        this.context = context;
        this.style = style != null ? style : new JSONObject();
        this.eventer = eventer;

        initView();

        style();
        try {
            style.put("focus", false);
        } catch (JSONException e) {
        	throw new RuntimeException(e);
        }
        
        context.addOnRotateListener(this);
    }
    
    @Override
    public void onRotate(int orientation) {
        updateWidthForOrientation(orientation);
    }
    
    protected void updateWidthForOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            searchEditText.setWidth(UIUtil.dip2px(context, 136));
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            searchEditText.setWidth(UIUtil.dip2px(context, 80));
        }
        searchEditText.invalidate();
    }
    
    public void updateStyle(JSONObject update) {
        updateJSONObject(style, update);
        style();
    }

    public JSONObject getStyle() {
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
        layout = new FrameLayout(context);
        
        searchEditText = new EditText(context);
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    SpannableStringBuilder sp = (SpannableStringBuilder) searchEditText
                            .getText();
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
                    e.printStackTrace();
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
        
        clearButton = new Button(context);
        clearButton.setBackgroundResource(R.drawable.monaca_search_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
                searchEditText.requestFocus();
                context.showSoftInput(searchEditText);
            }
        });
        
        Drawable drawable = context.getResources().getDrawable(R.drawable.monaca_search_clear);
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(
            drawable.getMinimumWidth(),
            drawable.getMinimumHeight(),
            Gravity.RIGHT | Gravity.CENTER_VERTICAL
        );
        layout.addView(clearButton, p);
    }

    /*
     * style memo: value placeHolder visibility disable textColor opacity
     * backgroundColor focus
     */
    protected void style() {
        searchEditText.setText(style.optString("value", ""));
        searchEditText.setHint(style.optString("placeHolder", ""));
        searchEditText.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE
                : View.INVISIBLE);
        searchEditText.setEnabled(!style.optBoolean("disable", false));

        searchEditText.setTextColor(buildColor(style.optString("textColor", "#000000")));
        searchEditText.setTextColor(searchEditText.getTextColors().withAlpha(
                buildOpacity(style.optDouble("opacity", 1.0))));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getFontSizeFromDip(Component.LABEL_TEXT_DIP));

        searchEditText.setWidth(dip2px(context, 65));

        if (style.has("backgroundColor")) {
            searchEditText.getBackground().setColorFilter(
                    buildColor(style.optString("backgroundColor", "#ffffff")),
                    Mode.MULTIPLY);
        }

        searchEditText.getBackground().setAlpha(
                buildOpacity(style.optDouble("opacity", 1.0)));
        clearButton.getBackground().setAlpha(
                buildOpacity(style.optDouble("opacity", 1.0)));
        searchEditText.setHintTextColor(searchEditText.getHintTextColors().withAlpha(
                buildOpacity(style.optDouble("opacity", 1.0))));
        updateWidthForOrientation(context.getUIOrientation());
    }

}
