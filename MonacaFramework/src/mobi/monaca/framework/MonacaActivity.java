package mobi.monaca.framework;

import mobi.monaca.framework.bootloader.LocalFileBootloader;
import mobi.monaca.framework.transition.TransitionParams;
import mobi.monaca.framework.transition.TransitionParams.TransitionAnimationType;
import mobi.monaca.framework.util.MyLog;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

/** This class represent Monaca's application start entry. */
public class MonacaActivity extends Activity {
	public static String TAG = MonacaActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstance) {
    	MyLog.i(TAG, "onCreate");
    	
        super.onCreate(savedInstance);
        if (LocalFileBootloader.needToUseLocalFileBootloader()) {
        	MyLog.v(TAG, "needToUseLocalFileBootloader");
            LocalFileBootloader.setup(this, new Runnable() {
                @Override
                public void run() {
                    Intent intent = createIntent();
                    intent.putExtra(MonacaPageActivity.TRANSITION_PARAM_NAME,
                            new TransitionParams(TransitionAnimationType.NONE,
                                    null,
                                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
                    intent.putExtra(MonacaPageActivity.URL_PARAM_NAME,
                            "file://" + getApplicationInfo().dataDir
                                    + "/www/index.html");
                    startActivity(intent);
                    finish();
                }
            }, new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MonacaActivity.this,
                            "Application launch fail...", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        } else {
            Intent intent = createIntent();
            intent.putExtra(MonacaPageActivity.TRANSITION_PARAM_NAME,
                    new TransitionParams(TransitionAnimationType.NONE, null,
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
            startActivity(intent);
            finish();
        }
    }

    protected Intent createIntent() {
        return new Intent(this, MonacaPageActivity.class);
    }

}
