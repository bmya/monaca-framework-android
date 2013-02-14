package mobi.monaca.framework.nativeui;

import java.util.Iterator;
import java.util.regex.Pattern;

import mobi.monaca.framework.util.MyLog;
import mobi.monaca.utils.TimeStamp;
import mobi.monaca.utils.log.LogItem;
import mobi.monaca.utils.log.LogItem.LogLevel;
import mobi.monaca.utils.log.LogItem.Source;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.util.TypedValue;

/** This class has utility methods for native component framework. */
public class UIUtil {

    static protected Pattern colorPattern = Pattern
            .compile("^#[0-9a-fA-F]{6}$");

    public static final String TAG = "PhoneGapLog";

    static protected SparseIntArray computedFontSizeCache = new SparseIntArray();

    /** Build color integer from color string and opacity value. */
    public static int buildColor(String colorString, double opacity) {
        int baseColor = 0;

        if (opacity > 1.0) {
            opacity = 1.0;
        }

        if (colorPattern.matcher(colorString).matches()) {
            baseColor = Integer.parseInt(colorString.substring(1, 7), 16);
        } else if (colorString.length() == 0) {
            baseColor = 0xcccccc;
        }

        long opacityLong = (Math.round((opacity / 1.0) * 0xff) & 0xff) << 24;

        return (int) (baseColor + opacityLong);
    }

    public static int buildColor(String color) {
        return UIUtil.buildColor(color, 1.0);
    }

    public static void updateJSONObject(JSONObject target, JSONObject source) {
        Iterator<String> iterator = source.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                target.put(key, source.get(key));
            } catch (JSONException e) {
            }
        }
    }

    public static int buildOpacity(double opacity) {
        opacity = opacity < 1.0 ? opacity : 1.0;
        return Math.round((int) (255 * (opacity / 1.0)));
    }

    public static int dip2px(Context context, int dip) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources()
                        .getDisplayMetrics()));
    }

    public static void reportJSONParseError(Context context, String msg) {
    	MyLog.e(TAG, "JSONParseError: " + msg);
    	LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR, "NativeComponent:JSONParseError:" + msg, "", 0);
    	MyLog.sendBloadcastDebugLog(context, logItem);
    }

    public static void reportInvalidJSONStructure(Context context, String msg) {
    	MyLog.e(TAG, "InvalidJSONStructure: " + msg);
        LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR, "NativeComponent:InvalidJSONStructure:" + msg, "", 0);
    	MyLog.sendBloadcastDebugLog(context, logItem);
    }

    public static void reportInvalidComponent(Context context, String msg) {
    	MyLog.e(TAG, "InvalidComponent: " + msg);
        LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR, "NativeComponent:InvalidComponent:" + msg, "", 0);
     	MyLog.sendBloadcastDebugLog(context, logItem);
  
    }

    public static void reportInvalidContainer(Context context, String msg) {
    	MyLog.e(TAG, "InvalidContainer: " + msg);
        LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR, "NativeComponent:InvalidContainer:" + msg, "", 0);
     	MyLog.sendBloadcastDebugLog(context, logItem);
  
    }

    public static void reportUndefinedProperty(Context context, String msg) {
    	MyLog.e(TAG, "UndefinedProperty: " + msg);
        LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR, "NativeComponent:UndefinedProperty:" + msg, "", 0);
     	MyLog.sendBloadcastDebugLog(context, logItem);
    }

    public static void reportInvalidStyleProperty(Context context, String msg) {
    	MyLog.e(TAG, "InvalidStyleProperty: " + msg);
        LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR, "NativeComponent:InvalidStyleProperty:" + msg, "", 0);
     	MyLog.sendBloadcastDebugLog(context, logItem);
  
    }

    public static void reportIgnoredStyleProperty(Context context, String msg) {
    	MyLog.e(TAG, "IgnoredStyleProperty: " + msg);
        LogItem logItem = new LogItem(TimeStamp.getCurrentTimeStamp(), Source.SYSTEM, LogLevel.ERROR, "NativeComponent:IgnoredStyleProperty:" + msg, "", 0);
     	MyLog.sendBloadcastDebugLog(context, logItem);
  
    }

    public static int multiplyColor(int base, int filter) {
        int left, right, result;

        left = 255 & base;
        right = 255 & filter;

        result = (255 & Math.round(left * right / 255));

        left = 255 & (base >> 8);
        right = 255 & (filter >> 8);

        result += (255 & Math.round(left * right / 255)) << 8;

        left = 255 & (base >> 16);
        right = 255 & (filter >> 16);

        result += (255 & Math.round(left * right / 255)) << 16;

        return result + 0xff000000;
    }

    /** Create Bitmap instance from Drawable. */
    public static Bitmap createBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        drawable.setBounds(0, 0, width, height);
        drawable.draw(new Canvas(bitmap));

        return bitmap;
    }

    /** Create drawable instance from drawable and setted color filter. */
    public static Bitmap createBitmapWithColorFilter(Drawable drawable,
            ColorFilter colorFilter) {

        Bitmap bitmap = createBitmapFromDrawable(drawable);
        return createBitmapWithColorFilter(bitmap, colorFilter);
    }

    /** Create drawable instance from drawable and setted color filter. */
    public static Bitmap createBitmapWithColorFilter(Bitmap bitmap,
            ColorFilter colorFilter) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // paint filtered bitmap
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, width, height).copy(
                Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(result);

        Paint paint = new Paint();
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        paint.setColorFilter(colorFilter);
        canvas.drawPaint(paint);

        return result;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int resizeWidth,
            int resizeHeight) {
        float resizeScaleWidth;
        float resizeScaleHeight;

        Matrix matrix = new Matrix();
        resizeScaleWidth = (float) resizeWidth / (float) bitmap.getWidth();
        resizeScaleHeight = (float) resizeHeight / (float) bitmap.getHeight();
        matrix.postScale(resizeScaleWidth, resizeScaleHeight);

        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return resizeBitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int resizeHeight) {
        float resizeScaleHeight;

        Matrix matrix = new Matrix();
        resizeScaleHeight = (float) resizeHeight / (float) bitmap.getHeight();
        matrix.postScale(resizeScaleHeight, resizeScaleHeight);

        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return resizeBitmap;
    }

    public static Bitmap resizeButtonImageBitmap(DisplayMetrics metrics,
            Bitmap bitmap) {

            // xhdpi
            // do nothing
    	
         if (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
            // hdpi
            bitmap = resizeBitmap(bitmap,
                    (int) Math.round(bitmap.getWidth() * 0.75),
                    (int) Math.round(bitmap.getHeight() * 0.75));
        } else if (metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
            // mdpi
            bitmap = resizeBitmap(bitmap,
                    (int) Math.round(bitmap.getWidth() * 0.5),
                    (int) Math.round(bitmap.getHeight() * 0.5));
        } else if (metrics.densityDpi == DisplayMetrics.DENSITY_LOW) {
            // ldpi
            bitmap = resizeBitmap(bitmap,
                    (int) Math.round(bitmap.getWidth() * 0.375),
                    (int) Math.round(bitmap.getHeight() * 0.375));
        }

        return bitmap;
    }

    public static int getFontSizeFromDip(Context context, int dip) {
        if (computedFontSizeCache.get(dip, -1) != -1) {
            return computedFontSizeCache.get(dip);
        }
        Integer result = computeFontSizeFromFontHeightDip(context, dip);

        return result;
    }

    protected static int computeFontSizeFromFontHeightDip(Context context,
            int dip) {
        int targetHeight = UIUtil.dip2px(context, dip);
        float resultTextSize = 0;
        float oldHeight = 0;

        for (int i = 0; i < 100; i++) {
            Paint paint = new Paint();
            paint.setTextSize(i);
            FontMetrics metrics = paint.getFontMetrics();

            float height = metrics.descent - metrics.ascent;

            if (Math.abs(targetHeight - oldHeight) > Math.abs(targetHeight
                    - height)) {
                resultTextSize = i;
            }
            oldHeight = height;
        }

        return (int) resultTextSize;
    }
}
