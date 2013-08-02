package mobi.monaca.framework.nativeui.component;

import mobi.monaca.framework.psedo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class ToolbarBackgroundDrawable extends Drawable {
    //private static final String TAG = ToolbarBackgroundDrawable.class.getSimpleName();
	protected Drawable drawable;
    protected int alpha = 79;
    protected ColorFilter colorFilter = null;
    protected Context context;

    public ToolbarBackgroundDrawable(Context context) {
        super();

        this.drawable = context.getResources().getDrawable(R.drawable.monaca_toolbar_bg);
        this.context = context;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = drawable.getBounds();

        if (rect.width() <= 0 || rect.height() <= 0) {
        	return;
        }

        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);

        if (colorFilter != null) {
            drawable.setColorFilter(colorFilter);
        }
        drawable.draw(new Canvas(bitmap));

        Paint paint = new Paint();
        paint.setAlpha(alpha);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        bitmap.recycle();
    }

    @Override
    public void setBounds(Rect bounds) {
        drawable.setBounds(bounds);
        super.setBounds(bounds);
        invalidateSelf();
    }

    @Override
    public void setBounds(int left, int top, int width, int height) {
        drawable.setBounds(left, top, width, height);
        super.setBounds(left, top, width, height);
        invalidateSelf();
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
        invalidateSelf();
    }


    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return drawable.getOpacity();
    }

}
