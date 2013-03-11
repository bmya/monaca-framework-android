package mobi.monaca.framework.nativeui;

import android.os.Bundle;

public class UISettings {
    
    public final String DISABLE_TOOLBAR_BORDER = "nativeui.disableToolbarBorder";
    
    public final boolean disableToolbarBorder;
    
    public UISettings(Bundle settings) {
        disableToolbarBorder = settings.containsKey(DISABLE_TOOLBAR_BORDER)
                ? settings.getBoolean(DISABLE_TOOLBAR_BORDER)
                : false;
    }

}
