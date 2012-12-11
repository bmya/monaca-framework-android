package mobi.monaca.framework.transition;

import android.view.animation.*;

/** This class provide animation set for the screen transition. */
public class GoBackAnimationSet {

    final public Animation goIn;
    final public Animation goOut;
    final public Animation backIn;
    final public Animation backOut;

    public GoBackAnimationSet(Animation goIn, Animation goOut,
            Animation backIn, Animation backOut) {
        this.goIn = goIn;
        this.goOut = goOut;
        this.backIn = backIn;
        this.backOut = backOut;
    }

    public GoBackAnimationSet(Animation goIn, Animation goOut) {
        this.goIn = goIn;
        this.goOut = goOut;
        this.backIn = goIn;
        this.backOut = goOut;
    }

    protected static Animation bindSettings(Animation anim, int duration) {
        anim.setDuration(duration);
        anim.restrictDuration(duration);
        anim.setZAdjustment(Animation.ZORDER_BOTTOM);
        anim.setInterpolator(new LinearInterpolator());
        // anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }

    public static GoBackAnimationSet alpha() {
        int duration = 300;
        return new GoBackAnimationSet(bindSettings(new AlphaAnimation(0.0f,
                1.0f), duration), bindSettings(new AlphaAnimation(1.0f, 0.0f),
                duration));
    }

    public static GoBackAnimationSet translate() {
        int duration = 400;
        return new GoBackAnimationSet(bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f), duration), bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f), duration), bindSettings(
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f), duration),
                bindSettings(new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0f, Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f), duration));
    }

    public static GoBackAnimationSet verticalTranslate() {
        int duration = 400;
        return new GoBackAnimationSet(bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0f), duration), bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -1.0f), duration), bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0f), duration), bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                1.0f), duration));
    }

    public static GoBackAnimationSet modal() {
        int duration = 400;
        return new GoBackAnimationSet(bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0f), duration), bindSettings(
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f), duration),
                bindSettings(new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f), duration),
                bindSettings(new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 1.0f), duration));
    }

    public static GoBackAnimationSet transit() {
        int duration = 500;
        return new GoBackAnimationSet(bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f), duration), bindSettings(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f), duration), bindSettings(
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f), duration),
                bindSettings(new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0f, Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f), duration));
    }

    public static GoBackAnimationSet none() {
        return new GoBackAnimationSet(new AnimationSet(true), new AnimationSet(
                true), new AnimationSet(true), new AnimationSet(true));
    }

}
