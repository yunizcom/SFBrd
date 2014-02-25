package com.mooStan.smashflappybird;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mooStan.smashflappybird.soundsController;
import com.mooStan.smashflappybird.R;
import com.revmob.RevMob;
import com.revmob.RevMobTestingMode;

public class gameEngine extends Activity {

	private Context myContext;
	private Activity myActivity;
	
	private TextView stageLevelShow, stageTimeShow, ic_trophy_game_scores,ic_mybomb_txt;
	private RelativeLayout slashScreen, mainMenu, sub_menu, gameStage,leaderBoard;
	private LinearLayout gameObjects;
	private ImageView ic_mybomb,ic_time_bar;
	
	public soundsController soundsController;
	public popupBox popupBox;
	
	private GlobalVars globalVariable;
	
	private int currentLevel = 1, screenWidth = 0, screenHeight = 0, sdk = 0, createdBirds = 100;

	Timer WFT = new Timer();
	
	private RevMob revmob;
	
	gameEngine(Context context, Activity myActivityReference) {
		myContext = context;
		myActivity = myActivityReference;
		
		globalVariable = (GlobalVars) myActivity.getApplicationContext();
		
		soundsController = new soundsController(myContext,myActivity);
		popupBox = new popupBox(myContext,myActivity);
		
		stageLevelShow = (TextView) myActivity.findViewById(R.id.stageLevelShow);
		stageTimeShow = (TextView) myActivity.findViewById(R.id.stageTimeShow);
		ic_trophy_game_scores = (TextView) myActivity.findViewById(R.id.ic_trophy_game_scores);
		ic_mybomb_txt = (TextView) myActivity.findViewById(R.id.ic_mybomb_txt);
		
		ic_time_bar = (ImageView) myActivity.findViewById(R.id.ic_time_bar);
		
		ic_mybomb = (ImageView) myActivity.findViewById(R.id.ic_mybomb);

		slashScreen = (RelativeLayout) myActivity.findViewById(R.id.slashScreen);
		mainMenu = (RelativeLayout) myActivity.findViewById(R.id.mainMenu);
		sub_menu = (RelativeLayout) myActivity.findViewById(R.id.sub_menu);
		gameStage = (RelativeLayout) myActivity.findViewById(R.id.gameStage);
		gameObjects = (LinearLayout) myActivity.findViewById(R.id.gameObjects);
		leaderBoard = (RelativeLayout) myActivity.findViewById(R.id.leaderBoard);

		ic_mybomb.setVisibility(View.GONE);
		ic_mybomb_txt.setVisibility(View.GONE);
		
		/*ic_mybomb.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	ic_mybomb.setAlpha(180);
		            	soundsController.objSoundClip("sounds/smack_effects.mp3");
		            	
		            	if(globalVariable.getBomb() > 0){
			            	int curBombUnits = globalVariable.getBomb();
		            		curBombUnits--;
		            		
		            		globalVariable.saveYunizSaved(globalVariable.getLevel(), curBombUnits, globalVariable.getPlayerName(), globalVariable.getShare());
		            		
		            		ic_mybomb_txt.setText("x" + curBombUnits);
			            	
		            		for(int i = (createdBirds - 10);i < createdBirds; i++ ){

				            	globalVariable.scores = globalVariable.scores + 1;
				            	
				            	ic_trophy_game_scores.setText(globalVariable.scores + "");
			            	
				            	ImageView levelEgg = (ImageView) myActivity.findViewById(100 + i);
				            	levelEgg.setImageResource(R.drawable.ic_smash_effect);
				            	
				            	globalVariable.toCLean.add((100 + i) + "");
		            		}
		            		
		            	}
		            	
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_mybomb.setAlpha(255);
		            }
	            }
	            return true;
	        }
	    });*/
		
		/*----RevMob Ads----*/
		revmob = RevMob.start(myActivity);
//revmob.setTestingMode(RevMobTestingMode.WITH_ADS);
        /*----RevMob Ads----*/
	}

	private void setTimeBarGraphic(boolean style){

		RelativeLayout.LayoutParams params;
		
		if(style == true){
			float f_CD = globalVariable.countDowns;
			
			if(f_CD < 0){
				f_CD = 0;
			}
			
			float f_TCD = globalVariable.countTotalDowns;
			int currentTimePercent = (int)(f_CD / f_TCD * 100);
			float c_TBW = globalVariable.timerBarOriWidth;
			int curWidthIs = (int)(c_TBW / 100 * currentTimePercent);
			
			params = new RelativeLayout.LayoutParams
			          (curWidthIs,(int) LayoutParams.WRAP_CONTENT);
			
			params.setMargins(0, 0, (globalVariable.timerBarOriWidth - curWidthIs), 0);
		}else{
			params = new RelativeLayout.LayoutParams
			          ((int) LayoutParams.WRAP_CONTENT,(int) LayoutParams.WRAP_CONTENT);
		}
		
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, ic_time_bar.getId());
		params.addRule(RelativeLayout.BELOW, stageTimeShow.getId());
		
		ic_time_bar.setLayoutParams(params);
		
		if(style == false && globalVariable.timerBarOriWidth == 0){
	    	globalVariable.timerBarOriWidth = ic_time_bar.getWidth();
		}
		
	}
	
	public void gameActivity(boolean status){
		if(globalVariable.inGameMode == false){
			return;
		}
		
		if(status == true){
			globalVariable.stopCounter = false;
			
			setTimeBarGraphic(true);

			setCountDownTimerBar(globalVariable.countDowns);
			
			timerCountDown();
	    	timerObjectCreate();
		}else{
			//ic_time_bar.clearAnimation();
			setTimeBarGraphic(true);
			globalVariable.stopCounter = true;
		}
		
	}
	
	public void startGameEngine(int level, int p_sdk){
		setTimeBarGraphic(false);
		gameObjects.removeAllViewsInLayout();
		System.gc();
		
		sdk = p_sdk;
		currentLevel = level;
		stageLevelShow.setText("Lvl " + currentLevel);
		
		createdBirds = 0;
		
		globalVariable.scores = 0;
		
		globalVariable.curTypedWord = "";
		
		ic_trophy_game_scores.setText(globalVariable.scores + "");
		ic_mybomb_txt.setText("x" + globalVariable.getBomb());
		
		stageController(gameStage);
    	setStageBackground(gameStage,"backgrounds/sub_menu.jpg");
    	
    	globalVariable.currentLevels = currentLevel;
    	globalVariable.currentToDelayed = 1000;
    	globalVariable.countDowns = (50 + currentLevel) * 1;
    	
    	//globalVariable.countDowns = 30 + (currentLevel * 5);
		globalVariable.countTotalDowns = globalVariable.countDowns;
		stageTimeShow.setText("Time : " + secToString(globalVariable.countDowns));
		setCountDownTimerBar(globalVariable.countDowns);
    	
    	globalVariable.curShownObject = 0;
    	globalVariable.currentTill = 0;
    	
    	globalVariable.inGameMode = true;
   	
    	globalVariable.stopCounter = false;
    	timerCountDown();
    	timerObjectCreate();
    	createGameObjects();
    	
    	setScanLoadWFT();
    	
    	globalVariable.saveYunizSaved(globalVariable.getLevel(), globalVariable.getBomb(), globalVariable.getPlayerName(), 0);
	}
	
	private int randomNumber(int min, int max){
		Random r = new Random();
    	int i1 = r.nextInt(max - min + 1) + min;
    	return i1;
	}

	private void forfeitObject(int index){
		String[] arraylist = globalVariable.currentLevelChallenge;
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, arraylist);
		
		list.remove(index);
		gameObjects.removeViewAt((gameObjects.getChildCount() - 1) - index);
		globalVariable.curShownObject--;
		
		globalVariable.currentLevelChallenge = list.toArray(new String[list.size()]);
	}
	
	private void gameOver(int types){
		stopGameStage();

		ic_trophy_game_scores.setText(globalVariable.scores + "");
		
		int topScore = Integer.valueOf(globalVariable.getSelectedYunizScores(globalVariable.currentLevels));
		
		if(topScore >= globalVariable.scores){
			
			popupBox.showPopBox("",2);
			
		}else if(globalVariable.scores < ((globalVariable.currentLevels * 10) + globalVariable.currentLevels) * 2){
			
			popupBox.showPopBox("",2);
			
		}else{

			if(globalVariable.currentLevels > globalVariable.getLevel()){
				globalVariable.saveYunizSaved(globalVariable.currentLevels, globalVariable.getBomb(), globalVariable.getPlayerName(), globalVariable.getShare());
			}else{
				globalVariable.saveYunizSaved(globalVariable.getLevel(), globalVariable.getBomb(), globalVariable.getPlayerName(), globalVariable.getShare());
			}
		
			globalVariable.saveYunizScores(globalVariable.currentLevels,globalVariable.scores);
			
			popupBox.showPopBox("",1);
		}

		revmob.showFullscreen(myActivity);
		
	}
	
private void createGameObjects(){
		
		int wordPos = randomNumber(0,2), unitPerLine = randomNumber(1,globalVariable.currentLevels);

		LinearLayout levelBox = new LinearLayout(myActivity);
		levelBox.setOrientation(LinearLayout.HORIZONTAL);
		
		switch(wordPos){
			case 0:{
				levelBox.setGravity(Gravity.LEFT);
				break;
			}
			case 1:{
				levelBox.setGravity(Gravity.CENTER);
				break;
			}
			case 2:{
				levelBox.setGravity(Gravity.RIGHT);
				break;
			}
		}
		
		for(int i = 0;i < unitPerLine;i++){
			createdBirds++;
			
			ImageView levelEgg = new ImageView(myActivity);
	
			levelEgg.setImageResource(R.drawable.ic_flappybird_mod);
			levelEgg.setId(100 + createdBirds);
			levelBox.addView(levelEgg);
			
			levelEgg.setOnTouchListener(new View.OnTouchListener() {
		        @Override
		        public boolean onTouch(View arg0, MotionEvent arg1) {
		            switch (arg1.getAction()) {
			            case MotionEvent.ACTION_DOWN: {
			            	
			            	soundsController.shortSoundClip("sounds/smack_effects.mp3");
			            	
			            	globalVariable.scores = globalVariable.scores + 1;
			            	
			            	ic_trophy_game_scores.setText(globalVariable.scores + "");
		            	
			            	ImageView levelEgg = (ImageView) myActivity.findViewById(arg0.getId());
			            	levelEgg.setImageResource(R.drawable.ic_smash_effect);
			            	
			            	int curID = arg0.getId();
			            	
			            	globalVariable.toCLean.add(curID + "");
			            	
			            }
			            case MotionEvent.ACTION_UP:{

			            }
		            }
		            return true;
		        }
		    });
		}
		
		gameObjects.addView(levelBox,0);
		
	}
	
	public void setScanLoadWFT() {
	    WFT.schedule(new TimerTask() {          
	        @Override
	        public void run() {
	            WFTTimerMethod();
	        }
	    }, 1000); // 4 seconds delay
	}
	
	private void WFTTimerMethod() {
	    this.runOnUiThread(Timer_Tick);
	}
	
	private Runnable Timer_Tick = new Runnable() {
	    public void run() {
	    	
	    	if(globalVariable.toCLean.size() > 0){
				for(int i = 0; i < globalVariable.toCLean.size(); i++){
					ImageView levelEgg = (ImageView) myActivity.findViewById( Integer.valueOf(globalVariable.toCLean.get(i)) );
					levelEgg.setVisibility(View.GONE);
					globalVariable.toCLean.remove(i);
				}
	    	}
			
	    	setScanLoadWFT();
	    }
	};

	public void stopGameStage(){
		globalVariable.inGameMode = false;
		//ic_time_bar.clearAnimation();
		setTimeBarGraphic(true);
		globalVariable.stopCounter = true;
	}
	
	private void setCountDownTimerBar(int size){
		/*// removed because no longer needed animation to animate the timebar
		Animation animationScale = new ScaleAnimation(1, 0, 1, 1);
    	animationScale.setDuration((size + 2) * 1000);

		AnimationSet animSet = new AnimationSet(true);
		animSet.setFillAfter(true);
		animSet.addAnimation(animationScale);
		
		ic_time_bar.startAnimation(animSet);
		*/
	}
	
	private void timerCountDown(){
		if(globalVariable.stopCounter == true){
			return;
		}

		ic_time_bar.postDelayed(new Runnable() {
	        @Override
	        public void run() {
        	
	        	if(globalVariable.stopCounter == true){
	    			return;
	    		}
	        	
	        	globalVariable.countDowns--;
      	
	        	stageTimeShow.setText("Time : " + secToString(globalVariable.countDowns));
	        	
	        	setTimeBarGraphic(true);

        		if(globalVariable.countDowns > 0){
	        		timerCountDown();
	        	}else{
	        		gameOver(1);
	        	}

	        }
	    }, 1000);
	}
	
	private void timerObjectCreate(){
		if(globalVariable.stopCounter == true){
			return;
		}

		ic_trophy_game_scores.postDelayed(new Runnable() {
	        @Override
	        public void run() {
        	
	        	if(globalVariable.stopCounter == true){
	    			return;
	    		}
	        	
	        	soundsController.objSoundClip("sounds/createWords.mp3");
	        	
	        	int howManyLine = randomNumber(1,1);
	        	
	        	for(int i = 0;i < howManyLine;i++){
	        		createGameObjects();
	        	}
	        	
	        	timerObjectCreate();
	        	
	        	/*if( globalVariable.currentLevelChallenge.length <= 9 ){
					gameOver(0);
				}else{
					timerObjectCreate();
				}*/

	        }
	    }, globalVariable.currentToDelayed);
	}
	
	private String secToString(int secStr){
		String returnStr = "00:00";
		
		int minVal = (int)(globalVariable.countDowns / 60);
    	int secVal = (int)(globalVariable.countDowns % 60);
    	
    	String minValStr = minVal + "";
    	String secValStr = secVal + "";
    	
    	if(minVal < 10)
    		minValStr = "0" + minValStr;
    	
    	if(secVal < 10)
    		secValStr = "0" + secValStr;
    	
    	returnStr = minValStr + ":" + secValStr;
		
		return returnStr;
	}
	
	private void stageController(RelativeLayout thisStage){
		slashScreen.setVisibility(View.INVISIBLE);
		mainMenu.setVisibility(View.INVISIBLE);
		sub_menu.setVisibility(View.INVISIBLE);
		gameStage.setVisibility(View.INVISIBLE);
		leaderBoard.setVisibility(View.INVISIBLE);
		
		slashScreen.setClickable(false);
		mainMenu.setClickable(false);
		sub_menu.setClickable(false);
		gameStage.setClickable(false);
		leaderBoard.setClickable(false);
		
		thisStage.setVisibility(View.VISIBLE);
		thisStage.setClickable(true);
		
		System.gc();
	}
	
	@SuppressLint("NewApi")
	public void setStageBackground_text(TextView thisStage, String fileName){
		if(thisStage.getBackground() != null){
			return;
		}
		
		try 
		{
			InputStream ims = myActivity.getAssets().open(fileName);
		    Drawable d = Drawable.createFromStream(ims, null);
		    
		    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    	thisStage.setBackgroundDrawable(d);
		    } else {
		    	thisStage.setBackground(d);
		    }
		    
		    ims = null;
		    d = null;

		}
		catch(IOException ex) 
		{
		    return;
		}
		
	}
	
	@SuppressLint("NewApi")
	public void setStageBackground(RelativeLayout thisStage, String fileName){
		if(thisStage.getBackground() != null){
			return;
		}
		
		try 
		{
			InputStream ims = myActivity.getAssets().open(fileName);
		    Drawable d = Drawable.createFromStream(ims, null);
		    
		    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    	thisStage.setBackgroundDrawable(d);
		    } else {
		    	thisStage.setBackground(d);
		    }
		    
		    ims = null;
		    d = null;

		}
		catch(IOException ex) 
		{
		    return;
		}
		
	}
}
