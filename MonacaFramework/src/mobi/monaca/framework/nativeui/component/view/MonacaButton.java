package mobi.monaca.framework.nativeui.component.view;

import static mobi.monaca.framework.nativeui.UIUtil.*;

import org.json.JSONObject;

import mobi.monaca.framework.nativeui.UIValidator;
import mobi.monaca.framework.nativeui.component.ButtonBackgroundDrawable;
import mobi.monaca.framework.nativeui.exception.ConversionException;
import mobi.monaca.framework.nativeui.exception.NativeUIException;
import mobi.monaca.framework.nativeui.exception.ValueNotInRangeException;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MonacaButton extends FrameLayout {

    protected Context context;
    protected JSONObject style;
    protected ImageButton innerImageButton;
    protected MonacaTextButton button;

    public MonacaButton(Context context, AttributeSet attr) throws NativeUIException {
        super(context);
        this.context = context;
        this.style = new JSONObject();
        
        button = new MonacaTextButton(context, attr);
        innerImageButton = new ImageButton(context);
        
        addView(button);
        addView(innerImageButton);

        style();
    }
    
    public MonacaButton(Context context) throws NativeUIException {
        super(context);
        this.context = context;
        this.style = new JSONObject();

        button = new MonacaTextButton(context);
        innerImageButton = new ImageButton(context);
        
        addView(button);
        addView(innerImageButton);
        
        style();
    }
    
    public ImageButton getInnerImageButton() {
        return innerImageButton;
    }
    
    public Button getButton() {
        return button;
    }

    
    public void updateStyle(JSONObject update) throws NativeUIException {
        updateJSONObject(style, update);
        style();
    }
    

    public void style() throws NativeUIException {
        if (style.optString("innerImage", "").equals("")) {
            button.setVisibility(View.VISIBLE);
            innerImageButton.setVisibility(View.GONE);
            button.updateStyle(style);
        } else {
            innerImageButton.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            styleInnerImageButton();
        }
    }
    
    protected void styleInnerImageButton() throws NativeUIException {
		int backgroundColor = UIValidator.parseAndValidateColor(context, "Button style", "backgroundColor", "#000000", style);
		
		ButtonBackgroundDrawable background = new ButtonBackgroundDrawable(
                context, backgroundColor);
		
        float opacity = UIValidator.parseAndValidateFloat(context, "Button style", "opacity", "1.0", style, "[0.0-1.0]");
		background.setAlpha(buildOpacity(opacity));
        innerImageButton.setBackgroundDrawable(new ButtonDrawable(background));

        setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE
                : View.GONE);

        innerImageButton.setEnabled(!style.optBoolean("disable", false));
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
