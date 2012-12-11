package mobi.monaca.framework.transition;

import android.content.Intent;
import android.content.IntentFilter;

/** This class represent a intent to close MonacaPageActivity. */
public class ClosePageIntent extends Intent {

    public static final String DATA_TYPE = "mobi.monaca/activity";
    public static final String ACTION = "close";

    public ClosePageIntent(int level) {
        super(ACTION);
        setType(DATA_TYPE);
        putExtra("level", level);
    }

    public ClosePageIntent() {
        this(0);
    }

    public static IntentFilter createIntentFilter() {
        return IntentFilter.create(ACTION, DATA_TYPE);
    }

}