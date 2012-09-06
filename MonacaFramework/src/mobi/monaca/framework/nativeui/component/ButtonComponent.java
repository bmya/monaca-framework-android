package mobi.monaca.framework.nativeui.component;

import mobi.monaca.framework.nativeui.ComponentEventer;
import mobi.monaca.framework.nativeui.NonScaleBitmapDrawable;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.nativeui.component.view.MonacaButton;

import org.json.JSONObject;
import static mobi.monaca.framework.nativeui.UIUtil.*;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class ButtonComponent implements ToolbarComponent {

    protected UIContext context;
    protected JSONObject style;
    protected FrameLayout layout;
    protected MonacaButton button;
    protected Button imageButton;
    protected ComponentEventer eventer;

    public ButtonComponent(UIContext context, JSONObject style,
            final ComponentEventer eventer) {
        this.context = context;
        this.style = style != null ? style : new JSONObject();
        this.eventer = eventer;

        initView();
    }

    public ComponentEventer getUIEventer() {
        return eventer;
    }

    protected void initView() {
        layout = new FrameLayout(context);
        layout.setClickable(true);

        button = new MonacaButton(context);
        button.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventer.onTap();
            }
        });
        button.getInnerImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventer.onTap();
            }
        });

        imageButton = new Button(context);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventer.onTap();
            }
        });

        layout.addView(button);
        layout.addView(imageButton);

        style();
    }

    public View getView() {
        return layout;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        imageButton.setBackgroundDrawable(null);
    }

    /**
     * visibility: true / false [bool] (default: true) disable: true / false
     * [bool] (default: false) opacity: 0.0～1.0 [float] (default: 1.0)
     * backgroundColor: #000000 [string] (default: #000000) activeColor: #000000
     * [string] (default: #0000FF) textColor: #000000 [string] (default:
     * #FFFFFF) image: hoge.png (このファイルからみたときの相対パス) [string] text: テキスト [string]
     */
    protected void style() {
        if (style.optString("image").length() > 0) {
            button.setVisibility(View.GONE);
            imageButton.setVisibility(View.VISIBLE);
            styleImageButton();
        } else {
            button.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.GONE);
            styleButton();
        }
    }

    protected void styleButton() {
        button.updateStyle(style);
        button.style();
        
        if (!style.optString("innerImage", "").equals("")) {
            ImageButton imageButton = button.getInnerImageButton();
            imageButton.setImageBitmap(context.readBitmap(style.optString("innerImage")));
        }
    }

    protected void styleImageButton() {
        imageButton
                .setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE
                        : View.GONE);
        imageButton.setBackgroundColor(0);
        imageButton.setEnabled(!style.optBoolean("disable", false));

        Bitmap bitmap = context.readBitmap(style.optString("image", ""));
        if (bitmap != null) {
            int limitHeight = UIUtil.dip2px(context, 40);

            if (bitmap.getHeight() > limitHeight) {
                bitmap = UIUtil.resizeBitmap(bitmap, limitHeight);
            }
            Drawable drawable = new ImageButtonDrawable(
                    new NonScaleBitmapDrawable(bitmap));

            imageButton.setBackgroundDrawable(drawable);
            imageButton.setPadding(0, 0, 0, 0);
        } else {
            imageButton.setBackgroundDrawable(null);
        }

    }

    @Override
    public void updateStyle(JSONObject update) {
        updateJSONObject(style, update);
        style();
    }

    public JSONObject getStyle() {
        return style;
    }

    public static class ButtonDrawable extends LayerDrawable {
        protected int backgroundColor, pressedBackgroundColor;

        private ButtonDrawable(Drawable drawable) {
            super(new Drawable[] { drawable });
        }

        @Override
        protected boolean onStateChange(int[] states) {
            for (int state : states) {
                if (state == android.R.attr.state_pressed) {
                    super.setColorFilter(0x66000000, Mode.MULTIPLY);
                } else {
                    super.clearColorFilter();
                }
            }
            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }

    }

    public class ImageButtonDrawable extends StateListDrawable {
        protected int backgroundColor, pressedBackgroundColor;

        private ImageButtonDrawable(Drawable drawable) {
            super();
            Drawable pressed = new BitmapDrawable(context.getResources(),
                    createBitmapWithColorFilter(drawable,
                            new PorterDuffColorFilter(0x66000000,
                                    PorterDuff.Mode.MULTIPLY)));
            Drawable disabled = new BitmapDrawable(context.getResources(),
                    createBitmapWithColorFilter(drawable,
                            new PorterDuffColorFilter(0x66000000,
                                    PorterDuff.Mode.MULTIPLY)));

            addState(new int[] { android.R.attr.state_pressed }, pressed);
            addState(new int[] { -android.R.attr.state_enabled }, disabled);
            addState(new int[0], drawable.mutate());
        }
    }
}
