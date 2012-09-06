package mobi.monaca.framework;

import java.io.IOException;
import java.io.InputStream;

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

public class MonacaSplashActivity extends Activity {

    protected static final String SPLASH_IMAGE_PATH = "android/splash_default.png";
	protected ImageView splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (hasSplashScreenExists()) {
            splashView = new ImageView(this);
            splashView.setScaleType(ScaleType.FIT_CENTER);
            InputStream stream = getSplashFileStream();
            splashView.setImageBitmap(BitmapFactory.decodeStream(stream));
            
            splashView.setBackgroundColor(getBackgroundColor());
            
            try {
                stream.close();
            } catch (Exception e) {
            }
            setContentView(splashView);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MonacaSplashActivity.this,
                            getNextActivity());
                    if (getIntent().getExtras() != null) {
            			intent.putExtras(getIntent().getExtras());
            		}
                    
                    startActivity(intent);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                }
            }, 1000);
        } else {
            Intent intent = new Intent(this, getNextActivity());
            startActivity(intent);
            finish();
        }
    }
    
    protected int getBackgroundColor() {
		try {
			InputStream stream = getResources().getAssets().open("app.json");
			byte[] buffer = new byte[stream.available()];
			stream.read(buffer);
			JSONObject appJson = new JSONObject(new String(buffer,"UTF-8"));
			String backgroundColorString = appJson.getJSONObject("splash").getJSONObject("android").getString("background");
			if(!backgroundColorString.startsWith("#")){
				backgroundColorString = "#" + backgroundColorString;
			}
			int backbroundColor = Color.parseColor(backgroundColorString);
			return backbroundColor;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return Color.TRANSPARENT;
	}

	/*
     * To be overrided by MonacaDebuggerSplashActivity so that it will launch MonacaDebuggerPageActivity instead
     */
    protected Class<MonacaPageActivity> getNextActivity(){
    	return MonacaPageActivity.class;
    }

    protected boolean hasSplashScreenExists() {
        try {
            InputStream stream = getResources().getAssets().open(
                    SPLASH_IMAGE_PATH);
            stream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
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
