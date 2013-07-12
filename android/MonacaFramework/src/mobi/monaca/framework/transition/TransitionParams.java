package mobi.monaca.framework.transition;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.pm.ActivityInfo;

public class TransitionParams implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum TransitionAnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, MODAL, NONE, POP, DISMISS
    }

    public final TransitionAnimationType animationType;
    public final String backgroundImagePath;
    public final int requestedOrientation;
    public final boolean clearsStack;

    public TransitionParams(TransitionAnimationType animationType,
            String backgroundImagePath, int requestedOrientation, boolean clearsStack) {
        this.animationType = animationType;
        this.backgroundImagePath = backgroundImagePath;
        this.requestedOrientation = requestedOrientation;
        this.clearsStack = clearsStack;
    }

    public TransitionParams(TransitionAnimationType animationType,
            String backgroundImagePath, int requestedOrientation) {
        this(animationType, backgroundImagePath, requestedOrientation, false);
    }

    public Boolean hasBackgroundImage() {
        return backgroundImagePath != null && backgroundImagePath.length() > 0;
    }

    public int getRequestedOrientation() {
        return requestedOrientation;
    }

    public boolean needsToClearStack() {
        return clearsStack;
    }

    public static TransitionParams from(JSONObject json,
            String animationTypeString) {
        String backgroundImagePath = json.optString("bg", "");
        String orientationString = json.optString("orientation", "portrait");
        TransitionAnimationType animationType = null;

        if (animationTypeString.equals("slideLeft")) {
            animationType = TransitionAnimationType.SLIDE_LEFT;
        } else if (animationTypeString.equals("slideRight")) {
        	animationType = TransitionAnimationType.SLIDE_RIGHT;
        } else if (animationTypeString.equals("modal")) {
            animationType = TransitionAnimationType.MODAL;
        } else if (animationTypeString.equals("none")) {
            animationType = TransitionAnimationType.NONE;
        } else if (animationTypeString.equals("pop")) {
            animationType = TransitionAnimationType.POP;
        } else if (animationTypeString.equals("dismiss")) {
            animationType = TransitionAnimationType.DISMISS;
        } else {
            animationType = TransitionAnimationType.NONE;
        }

        int orientation;
        if (orientationString.equals("unspecified")) {
            orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        } else if (orientationString.equals("portrait")) {
            orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else if (orientationString.equals("landscape")) {
            orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }

        boolean clearsStack = json.optBoolean("clearStack", false);

        return new TransitionParams(animationType, backgroundImagePath,
                orientation, clearsStack);
    }

    public static TransitionParams createDefaultParams(int orientation) {
        return new TransitionParams(TransitionAnimationType.SLIDE_LEFT, "",
               orientation);
    }
}
