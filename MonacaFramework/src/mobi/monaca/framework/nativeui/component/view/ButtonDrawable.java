package mobi.monaca.framework.nativeui.component.view;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

class ButtonDrawable extends LayerDrawable {
    protected int backgroundColor, pressedBackgroundColor;

    public ButtonDrawable(Drawable drawable) {
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
