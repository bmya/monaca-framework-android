package mobi.monaca.framework;

import java.io.IOException;
import java.io.InputStream;

import mobi.monaca.framework.bootloader.LocalFileBootloader;
import mobi.monaca.framework.util.MyLog;
import mobi.monaca.utils.gcm.GCMPushDataset;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class MonacaSplashActivity extends Activity {
	private static final String TAG = MonacaSplashActivity.class.getSimpleName();
    protected static final String SPLASH_IMAGE_PATH = "android/splash_default.png";

    /**
     * modify "false" to "true" this if want to use LocalFileBootloader
     **/
    @SuppressWarnings("unused")
	protected static final boolean usesLocalFileBootloader = (false && LocalFileBootloader.needToUseLocalFileBootloader());

	protected ImageView splashView;

	protected Handler handler;
	protected Runnable pageLauncher;
	protected MonacaApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MonacaApplication)getApplication();
        loadAppJson();
        registerGCM();

        setup();
    }

    protected void setup() {
    	Runnable pageActivityLauncher = new Runnable() {
    		@Override
    		public void run() {
    			if (hasSplashScreenExists()) {
    				splashView = new ImageView(getApplicationContext());
    				splashView.setScaleType(ScaleType.FIT_CENTER);
    				InputStream stream = getSplashFileStream();
    				splashView.setImageBitmap(BitmapFactory.decodeStream(stream));
    				splashView.setBackgroundColor(app.getAppJsonSetting().getSplashBackgroundColor());
    				try {
    					stream.close();
    				} catch (Exception e) {
    				}
    				setContentView(splashView);

    				overridePendingTransition(mobi.monaca.framework.psedo.R.anim.monaca_none, mobi.monaca.framework.psedo.R.anim.monaca_slide_close_exit);

    				handler = new Handler();
    				pageLauncher = new Runnable() {
    					@Override
    					public void run() {
    						Intent intent = createActivityIntent();
    						startActivity(intent);
    						finish();
    					}
    				};

    				handler.postDelayed(pageLauncher, 1000);
    			} else {
    				goNextActivityWithoutSplash();
    			}
    		}
    	};

    	if (!usesLocalFileBootloader) {
    		pageActivityLauncher.run();
    	} else {
    		LocalFileBootloader.setup(this, pageActivityLauncher, new Runnable() {
    			@Override
    			public void run() {
    				Toast.makeText(MonacaSplashActivity.this, "Application launch fail...", Toast.LENGTH_LONG).show();
    				finish();
    			}
    		});
    	}
    }

    protected void loadAppJson() {
    	app.loadAppJsonSetting();
    }

    protected void registerGCM() {
		try {
			String senderId = app.getAppJsonSetting().getSenderId();
			// GCM registration process
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(this);
			if (regId.equals("")) {
				GCMRegistrar.register(this, senderId);
			} else {
				((MonacaApplication)getApplication()).sendGCMRegisterIdToAppAPI(regId);
			}

		} catch (Exception e) {
			MyLog.d(TAG, "this device or application does not support GCM");
			e.printStackTrace();
		}
    }

    protected Intent createActivityIntent() {
		Intent intent = new Intent(MonacaSplashActivity.this,
                MonacaPageActivity.class);

    	Intent received = getIntent();
    	Bundle bundle = received.getExtras();
    	if (received != null && bundle != null) {
    		GCMPushDataset pushdata = (GCMPushDataset)bundle.get(GCMPushDataset.KEY);
    		if (pushdata != null) {
        		intent.putExtra(GCMPushDataset.KEY, pushdata);
    		}
    	}
		return intent;
	}

    protected void goNextActivityWithoutSplash() {
        Intent intent = createActivityIntent();
        intent.putExtra("launchedWithoutSplash", true);
        startActivity(intent);
        finish();
    }

    protected boolean hasSplashScreenExists() {
    	if (app.getAppJsonSetting().getAutoHide() == false) {
    		return false;
    	}
        try {
            InputStream stream = getResources().getAssets().open(
                    SPLASH_IMAGE_PATH);
            stream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
    	if (handler != null && pageLauncher != null) {
    		handler.removeCallbacks(pageLauncher);
    	}
    	super.onBackPressed();
    }

    protected InputStream getSplashFileStream() {
        try {
            return getResources().getAssets().open(SPLASH_IMAGE_PATH);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
