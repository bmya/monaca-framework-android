package mobi.monaca.framework.transition;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.Display;

/** This class represents MonacaPageActivity's background. */
public class BackgroundDrawable extends Drawable {

    protected Bitmap bitmap;

    public BackgroundDrawable(Bitmap bitmap, Display display, int orientation) {
        super();
        this.bitmap = createBackgroundBitmap(bitmap, display, orientation);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    protected Bitmap createBackgroundBitmap(Bitmap bitmap, Display display,
            int orientation) {
        Matrix matrix;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            matrix = new Matrix();
            matrix.setRotate(-90.0f);
            Bitmap old = bitmap;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, false);
            old.recycle();
        }

        matrix = new Matrix();

        float resizedScaleWidth = (float) display.getWidth()
                / (float) bitmap.getWidth();
        float resizedScaleHeight = (float) display.getHeight()
                / (float) bitmap.getHeight();

        float scale = Math.max(resizedScaleWidth, resizedScaleHeight);
        matrix.postScale(scale, scale);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return resizedBitmap;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

}
