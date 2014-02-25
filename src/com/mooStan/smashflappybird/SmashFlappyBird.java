package com.mooStan.smashflappybird;

import com.facebook.Session;
import com.mooStan.smashflappybird.GlobalVars;
import com.mooStan.smashflappybird.initSetup;
import com.mooStan.smashflappybird.soundsController;
import com.mooStan.smashflappybird.R;
import com.revmob.RevMob;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;

public class SmashFlappyBird extends Activity {

	public initSetup initSetup;
	public soundsController soundsController;

	private GlobalVars globalVariable;
	
	private RevMob revmob;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smash_flappy_bird);
		
		initSetup = new initSetup(this.getApplicationContext(),this);
		soundsController = new soundsController(this.getApplicationContext(),this);
		
		globalVariable = (GlobalVars) getApplicationContext();
		
		initSetup.basicDetection();
		
		/*
		 * PackageInfo info;
		try {
		    info = getPackageManager().getPackageInfo("com.mooStan.typingcrush", PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md;
		        md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        String something = new String(Base64.encode(md.digest(), 0));
		        //String something = new String(Base64.encodeBytes(md.digest()));
		        Log.e("hash key", something);
		    }
		} catch (NameNotFoundException e1) {
		    Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
		    Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
		    Log.e("exception", e.toString());
		}
		 */
		
		/*----RevMob Ads----*/
		revmob = RevMob.start(this);
//revmob.setTestingMode(RevMobTestingMode.WITH_ADS);
		revmob.showFullscreen(this);
        /*----RevMob Ads----*/
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if(data != null){
	    	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	    }
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
		ImageView ic_time_bar = (ImageView) findViewById(R.id.ic_time_bar);
		globalVariable.gameStage_TimeBar_Width = ic_time_bar.getWidth();
	}
	
	@Override
    public void onBackPressed()
    {
		if(globalVariable.curentStage == 1){
			System.gc();
			//super.onBackPressed();
			System.exit(0);
			//finish();
		}else{
			initSetup.handleNativeBackAction();
		}
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	
		soundsController.destroyBgMusic();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		initSetup.pauseGame();
		
		soundsController.pauseBgMusic();
	}
	
	protected void onResume() {
		super.onResume();
		
		initSetup.resumeGame();
		
		soundsController.resumeBgMusic();
	}
	
}
