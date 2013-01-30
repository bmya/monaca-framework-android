package mobi.monaca.framework.nativeui.container;

import static mobi.monaca.framework.nativeui.UIUtil.buildColor;
import static mobi.monaca.framework.nativeui.UIUtil.buildOpacity;
import static mobi.monaca.framework.nativeui.UIUtil.updateJSONObject;

import java.util.ArrayList;
import java.util.List;

import mobi.monaca.framework.nativeui.NonScaleBitmapDrawable;
import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.nativeui.component.Component;
import mobi.monaca.framework.nativeui.component.ToolbarComponent;
import mobi.monaca.framework.psedo.R;

import org.json.JSONObject;
import static mobi.monaca.framework.nativeui.UIUtil.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class ToolbarContainer implements Component {
    private static final int TOP_BOTTOM_PADDING = 5;
	protected UIContext context;
    protected ToolbarContainerView view;
    protected ToolbarComponent left, center, right;
    protected JSONObject style;
    protected AlphaAnimation animation = null;

    public ToolbarContainer(UIContext context, List<ToolbarComponent> left,
            List<ToolbarComponent> center, List<ToolbarComponent> right,
            JSONObject style) {
        view = new ToolbarContainerView(context);
        this.style = style != null ? style : new JSONObject();
        this.context = context;

        view.setLeftView(left);
        view.setRightView(right);
        view.setCenterView(center, left.size() == 0 && right.size() == 0);

        style();
    }

    public void updateStyle(JSONObject update) {
        updateJSONObject(style, update);
        style();
    }

    public JSONObject getStyle() {
        return style;
    }

    public ToolbarContainer(UIContext context, JSONObject style) {
        this(context, new ArrayList<ToolbarComponent>(),
                new ArrayList<ToolbarComponent>(),
                new ArrayList<ToolbarComponent>(), style);
    }

    public View getView() {
        return view;
    }

    /**
     * visibility: [bool] (default: true) opacity: 0.0-1.0 [float] (default:
     * 1.0) backgroundColor: #000000 [string] (default: undefined) position :
     * "fixed" | "scroll" (default: "fixed") => androidだと無理ぽい title : [string]
     * (default : "") (このスタイルが指定された場合、center属性は無視される)
     * titleImage : [string] (default : "") このスタイルが指定された時、center属性は無視)
     */
    protected void style() {
        if (isTransparent(style.optDouble("opacity", 1.0)) && view.getVisibility() != (style.optBoolean("visibility", true) ? View.VISIBLE
                : View.INVISIBLE)) {
            if (animation != null) {
//                animation.cancel();  //TODO only available in Android 4.0
            }

            animation = style.optBoolean("visibility", true) ? new AlphaAnimation(
                    0f, 1.0f) : new AlphaAnimation(1.0f, 0f);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE
                            : View.INVISIBLE);
                    ToolbarContainer.this.animation = null;
                }
            });
            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(200);

            // cause GC to prevent "stop the world" on animation.
            System.gc();

            view.startAnimation(animation);
        } else {
            view.setVisibility(style.optBoolean("visibility", true) ? View.VISIBLE : View.GONE);
        }
        
        /*
        view.setTitleSubtitle(style.optString("title"),
                style.optString("subtitle"));
                */
        
        // titleColor
        view.setTitleColor(style.optString("titleColor", "#ffffff"));
        
        // subtitleColor
        view.setSubtitleColor(style.optString("subtitleColor", "#ffffff"));
        
        // titleFontScale
        view.setTitleFontScale(style.optString("titleFontScale", ""));
        
        // subtitleFontScale
        view.setSubitleFontScale(style.optString("subtitleFontScale", ""));
        
        String titleImagePath = style.optString("titleImage", "");
        view.setTitleSubtitle(
                style.optString("title"),
                style.optString("subtitle"),
                titleImagePath.equals("") ? null : context.readScaledBitmap(titleImagePath));

        ColorFilter filter = new PorterDuffColorFilter(
                buildColor(style.optString("backgroundColor", "#000000")),
                PorterDuff.Mode.SCREEN);
        Bitmap bgBitmap = UIUtil.createBitmapWithColorFilter(context
                .getResources().getDrawable(R.drawable.monaca_toolbar_bg),
                filter);

        Drawable original = context.getResources().getDrawable(
                R.drawable.monaca_toolbar_bg);
        Rect padding = new Rect();
        original.getPadding(padding);

        view.getContentView().setBackgroundDrawable(
                new BitmapDrawable(context.getResources(), bgBitmap));
        view.getContentView().setPadding(padding.left, dip2px(context, TOP_BOTTOM_PADDING), padding.right, dip2px(context, TOP_BOTTOM_PADDING));
        view.getContentView().getBackground()
                .setAlpha(buildOpacity(style.optDouble("opacity", 1.0)));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.requestFocus();
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        view.getContentView().setBackgroundDrawable(null);
    }
    
    static public boolean isTransparent(double opacity) {
        return opacity <= 0.999;
    }
}
