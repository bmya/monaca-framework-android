package mobi.monaca.framework.nativeui.component;

import android.graphics.Canvas;
import mobi.monaca.framework.psedo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class ButtonBackgroundDrawable extends Drawable {
    protected Drawable drawable;
    protected int alpha = 255;
    protected boolean isPressed = false, isEnabled = true;
    protected ColorFilter colorFilter, pressedColorFilter, disabledColorFilter;
    protected Context context;

    public ButtonBackgroundDrawable(Context context, int tintColor) {
        super();

        this.drawable = context.getResources().getDrawable(
                R.drawable.monaca_button);
        this.context = context;

        colorFilter = new PorterDuffColorFilter(tintColor,
                PorterDuff.Mode.SCREEN);
        pressedColorFilter = new PorterDuffColorFilter(
                makePressedTintColor(tintColor), PorterDuff.Mode.SCREEN);
        disabledColorFilter = new PorterDuffColorFilter(
                makeDisabledTintColor(tintColor), PorterDuff.Mode.SCREEN);
    }

    protected int makePressedTintColor(int color) {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);

        hsv[2] += hsv[2] > 0.5 ? -0.2 : 0.2;

        return Color.HSVToColor(hsv);
    }

    protected int makeDisabledTintColor(int color) {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);

        hsv[2] -= 0.2;
        if (hsv[2] < 0) {
            hsv[2] = 0;
        }

        return Color.HSVToColor(hsv);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = drawable.getBounds();

        if(rect.width() <= 0 || rect.height() <= 0){
        	return;
        }
        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas buttonCanvas = new Canvas(bitmap);
        drawable.draw(buttonCanvas);

        // add tint color
        Bitmap clippingMask = Bitmap.createBitmap(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(isEnabled ? (isPressed ? pressedColorFilter
                : colorFilter) : disabledColorFilter);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        buttonCanvas.drawBitmap(clippingMask, 0, 0, paint);

        // draw frame
        Bitmap frameBitmap = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.monaca_button_frame);
        NinePatch frameNinePatch = new NinePatch(frameBitmap,
                frameBitmap.getNinePatchChunk(), null);
        paint = new Paint();
        paint.setAlpha(0xcc);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        frameNinePatch.setPaint(paint);
        frameNinePatch.draw(buttonCanvas, drawable.getBounds());

        buttonCanvas.drawColor((alpha & 0xff) << 24, PorterDuff.Mode.DST_IN);

        canvas.drawBitmap(bitmap, 0, 0, new Paint());

        bitmap.recycle();
        clippingMask.recycle();
        frameBitmap.recycle();
    }

    @Override
    public void setBounds(Rect bounds) {
        drawable.setBounds(bounds);
    }

    @Override
    public void setBounds(int left, int top, int width, int height) {
        drawable.setBounds(left, top, width, height);
    }

    @Override
    public int getIntrinsicHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getMinimumHeight() {
        return drawable.getMinimumHeight();
    }

    @Override
    public int getMinimumWidth() {
        return drawable.getMinimumWidth();
    }

    @Override
    public boolean getPadding(Rect padding) {
        return drawable.getPadding(padding);
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = 0xff & alpha;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return drawable.getOpacity();
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean isPressedTemp = false;
        boolean isEnabledTemp = false;

        for (int state : states) {
            if (state == android.R.attr.state_pressed) {
                isPressedTemp = true;
            }
            if (state == android.R.attr.state_enabled) {
                isEnabledTemp = true;
            }
        }

        boolean changed = isPressed != isPressedTemp
                || isEnabled != isEnabledTemp;
        isPressed = isPressedTemp;
        isEnabled = isEnabledTemp;

        if (changed) {
            invalidateSelf();
            return true;
        }

        return super.onStateChange(states);
    }

}
