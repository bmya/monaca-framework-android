package mobi.monaca.framework.transition;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.pm.ActivityInfo;

public class TransitionParams implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum TransitionAnimationType {
        TRANSIT, MODAL, NONE, POP, DISMISS
    }

    public final TransitionAnimationType animationType;
    public final String backgroundImagePath;
    public final int requestedOrientation;

    public TransitionParams(TransitionAnimationType animationType,
            String backgroundImagePath, int requestedOrientation) {
        this.animationType = animationType;
        this.backgroundImagePath = backgroundImagePath;
        this.requestedOrientation = requestedOrientation;
    }

    public Boolean hasBackgroundImage() {
        return backgroundImagePath != null && backgroundImagePath.length() > 0;
    }

    public int getRequestedOrientation() {
        return requestedOrientation;
    }

    public static TransitionParams from(JSONObject json,
            String animationTypeString) {
        String backgroundImagePath = json.optString("bg", "");
        String orientationString = json.optString("orientation", "portrait");
        TransitionAnimationType animationType = null;

        if (animationTypeString.equals("transit")) {
            animationType = TransitionAnimationType.TRANSIT;
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

        return new TransitionParams(animationType, backgroundImagePath,
                orientation);
    }

    public static TransitionParams createDefaultParams() {
        return new TransitionParams(TransitionAnimationType.TRANSIT, "",
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
