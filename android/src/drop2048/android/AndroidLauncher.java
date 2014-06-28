package drop2048.android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import drop2048.Drop2048;
import drop2048.IActivityRequestHandler;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
	protected AdView adView;
	private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    EasyTracker tracker;
	
	protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                    adView.setVisibility(View.VISIBLE);
                    break;
                case HIDE_ADS:
                    adView.setVisibility(View.GONE);
                    break;
            }
        }
    };
	
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(new Drop2048(this), config);

        // Create and setup the AdMob view
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-3934997582029072/4099321345");
        adView.setAdSize(AdSize.SMART_BANNER);

        AdRequest adRequest = new AdRequest.Builder().build();
        
        adView.loadAd(adRequest);

        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        layout.addView(adView, adParams);
        adView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        // Hook it all up
        setContentView(layout);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	tracker = EasyTracker.getInstance(this);
    	tracker.activityStart(this);
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	tracker.activityStop(this);  // Add this method.
//      EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }
    
//	@Override
//	protected void onCreate (Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		initialize(new Drop2048(this), config);
//	}
	
	// This is the callback that posts a message for the handler
    @Override
    public void showAds(boolean show) {
       handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
	
	@Override
	public int getHeightAd() {
		return adView.getHeight();
	}
}
