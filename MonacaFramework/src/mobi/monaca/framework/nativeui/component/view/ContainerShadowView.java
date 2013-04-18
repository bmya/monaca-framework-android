package mobi.monaca.framework.nativeui.component.view;

import mobi.monaca.framework.nativeui.UIContext;
import mobi.monaca.framework.nativeui.UIUtil;
import mobi.monaca.framework.psedo.R;
import android.view.View;

public class ContainerShadowView extends View{
	private static final String TAG = ContainerShadowView.class.getSimpleName();

	public ContainerShadowView(UIContext context, boolean isTop) {
		super(context);
		if(isTop){
			setBackgroundResource(R.drawable.shadow_bg);
		}else{
			setBackgroundResource(R.drawable.shadow_bg_reverse);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if(heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST){
			int resolvedWidth = resolveSize(400, widthMeasureSpec);
			int shadowHeight = UIUtil.dip2px(getContext(), 3);
			int resolvedHeight = resolveSize(shadowHeight, heightMeasureSpec);
			setMeasuredDimension(resolvedWidth, resolvedHeight);
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
