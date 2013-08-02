package mobi.monaca.framework.nativeui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class NonScaleBitmapDrawable extends Drawable {

    protected Bitmap bitmap;

    public NonScaleBitmapDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
        setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getIntrinsicHeight() {
        return bitmap.getHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getMinimumHeight() {
        return getIntrinsicHeight();
    }

    @Override
    public int getMinimumWidth() {
        return getIntrinsicWidth();
    }
}
