package mobi.monaca.framework.nativeui.component;

import android.graphics.Canvas;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.psedo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class SegmentBackgroundDrawable extends Drawable {
    protected Drawable drawable;
    protected int alpha = 255;
    protected boolean isSelected = false;
    protected ColorFilter colorFilter, pressedColorFilter;
    protected Context context;

    static enum Type {
        LEFT(R.drawable.monaca_segment_left), CENTER(
                R.drawable.monaca_segment_center), RIGHT(
                R.drawable.monaca_segment_right), SINGLE(
                R.drawable.monaca_button);

        public final int resourceId;

        private Type(int resId) {
            this.resourceId = resId;
        }
    }

    public SegmentBackgroundDrawable(Context context, Type type, int tintColor) {
        super();

        this.drawable = context.getResources().getDrawable(type.resourceId);
        this.context = context;

        colorFilter = new PorterDuffColorFilter(tintColor,
                PorterDuff.Mode.SCREEN);
        pressedColorFilter = new PorterDuffColorFilter(
                makePressedTintColor(tintColor), PorterDuff.Mode.SCREEN);
    }

    protected int makePressedTintColor(int color) {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);

        hsv[2] += hsv[2] > 0.5 ? -0.2 : 0.2;
        return UIUtil.multiplyColor(0xff999999, color);

        // return Color.HSVToColor(hsv);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = drawable.getBounds();

        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas buttonCanvas = new Canvas(bitmap);
        drawable.draw(buttonCanvas);

        // add tint color
        Bitmap clippingMask = Bitmap.createBitmap(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(isSelected ? pressedColorFilter : colorFilter);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        buttonCanvas.drawBitmap(clippingMask, 0, 0, paint);

        buttonCanvas.drawColor((alpha & 0xff) << 24, PorterDuff.Mode.DST_IN);

        canvas.drawBitmap(bitmap, 0, 0, new Paint());

        bitmap.recycle();
        clippingMask.recycle();
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

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}