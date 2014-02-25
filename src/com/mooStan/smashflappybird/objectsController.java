package com.mooStan.smashflappybird;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mooStan.smashflappybird.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mooStan.smashflappybird.soundsController;
import com.mooStan.smashflappybird.popupBox;
import com.mooStan.smashflappybird.gameEngine;

public class objectsController {

	private Context myContext;
	private Activity myActivity;
	
	private LinearLayout level_list;
	private ScrollView scrollList;
	private RelativeLayout slashScreen, mainMenu, sub_menu, gameStage, leaderBoard, howToPlay;
	
	private int curGloLevel = 0;
	
	private int sdk = 0;
	public soundsController soundsController;
	public popupBox popupBox;
	public gameEngine gameEngine;
	
	private GlobalVars globalVariable;

	objectsController(Context context, Activity myActivityReference) {
		myContext = context;
		myActivity = myActivityReference;
		
		soundsController = new soundsController(myContext,myActivity);
		popupBox = new popupBox(myContext,myActivity);
		gameEngine = new gameEngine(myContext,myActivity);
		
		level_list = (LinearLayout) myActivity.findViewById(R.id.level_list);
		scrollList = (ScrollView) myActivity.findViewById(R.id.scrollList);

		slashScreen = (RelativeLayout) myActivity.findViewById(R.id.slashScreen);
		mainMenu = (RelativeLayout) myActivity.findViewById(R.id.mainMenu);
		sub_menu = (RelativeLayout) myActivity.findViewById(R.id.sub_menu);
		gameStage = (RelativeLayout) myActivity.findViewById(R.id.gameStage);
		leaderBoard = (RelativeLayout) myActivity.findViewById(R.id.leaderBoard);
		howToPlay = (RelativeLayout) myActivity.findViewById(R.id.howToPlay);
		
		globalVariable = (GlobalVars) myActivity.getApplicationContext();
	}
	
	public void setWindowDetails(int sw,int sh,boolean sc,int tsdk){
		sdk = tsdk;
	}
	
	public void createLevelOptions(int curLevel){
		level_list.removeAllViewsInLayout();

		curGloLevel = curLevel;
		
		int maxLevel = curLevel + 3;

		if(maxLevel >= 15){
			maxLevel = 15;
		}
		
		//-----retrieve all saved scores-----
		String curScores = globalVariable.getYunizScores();
		String[] levelScores = curScores.split("[|]");
		
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, levelScores);

		for(int i = 0; i <= maxLevel; i++){			
			if(i < levelScores.length){
				if(levelScores[i].equals("")){
					list.add(i, "0");
				}else{
					list.add(i, levelScores[i]);
				}
			}else{
				list.add(i, "0");
			}
		}
		levelScores = list.toArray(new String[list.size()]);
		//-----retrieve all saved scores-----
		
		for(int i = 1;i <= maxLevel;i++){
			ImageView levelEgg = new ImageView(myActivity);
			LinearLayout levelBox = new LinearLayout(myActivity);
			LinearLayout levelDetails = new LinearLayout(myActivity);
			TextView levelText = new TextView(myActivity);
			TextView scoresText = new TextView(myActivity);
		
			levelBox.setGravity(Gravity.LEFT);
			levelDetails.setOrientation(LinearLayout.VERTICAL);
			
			Typeface tf = Typeface.createFromAsset(myActivity.getAssets(), "fonts/Cookies.ttf");
			levelText.setTypeface(tf);
			levelText.setTextSize(25);
			levelText.setPadding(20, 10, 20, 0);
			
			levelText.setText("Level " + i);
			
			scoresText.setTypeface(tf);
			scoresText.setTextSize(20);
			scoresText.setPadding(20, 10, 20, 10);
			
			scoresText.setText(levelScores[(i-1)] + " pts");
			
			if( i <= curLevel){
				levelEgg.setImageResource(R.drawable.ic_egg_enable);
				levelText.setTextColor(Color.parseColor("#b60202"));
				scoresText.setTextColor(Color.parseColor("#ff3b3b"));
			}else{
				levelEgg.setImageResource(R.drawable.ic_egg_disable);
				levelText.setTextColor(Color.parseColor("#656060"));
				scoresText.setTextColor(Color.parseColor("#656060"));
			}

			levelBox.setOnClickListener(new OnClickListener(){
		        public void onClick(View v) 
		        {

		        	if(globalVariable.getLevel() == 0){
		            	stageController(howToPlay);
		            	setStageBackground(howToPlay,"backgrounds/how_to_play.jpg");
		            	globalVariable.curentStage = 3;
	            	}else{
	            		gotoCurrentLevel(v.getId());
	            	}
		        	
		        }
		    });
			
			levelDetails.addView(levelText);
			levelDetails.addView(scoresText);
			
			levelBox.setId(i);
			levelBox.addView(levelEgg);
			levelBox.addView(levelDetails);
			
			level_list.addView(levelBox);
		}
		
		
		scrollList.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	scrollList.smoothScrollTo(0, level_list.getBottom());
	        }
	    }, 500);

		System.gc();
		
	}
	
	private void gotoCurrentLevel(int level){
		if( level <= (curGloLevel + 1) ){
			soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
			
			gameEngine.startGameEngine(level,sdk);
			
        	globalVariable.curentStage = 3;
			
		}else{
			popupBox.showPopBox("You are not allow to jump across levels, please play them one by one.",0);
		}
		
	}
	
	private void stageController(RelativeLayout thisStage){
		slashScreen.setVisibility(View.INVISIBLE);
		mainMenu.setVisibility(View.INVISIBLE);
		sub_menu.setVisibility(View.INVISIBLE);
		gameStage.setVisibility(View.INVISIBLE);
		leaderBoard.setVisibility(View.INVISIBLE);
		howToPlay.setVisibility(View.INVISIBLE);
		
		slashScreen.setClickable(false);
		mainMenu.setClickable(false);
		sub_menu.setClickable(false);
		gameStage.setClickable(false);
		leaderBoard.setClickable(false);
		howToPlay.setVisibility(View.INVISIBLE);
		
		thisStage.setVisibility(View.VISIBLE);
		thisStage.setClickable(true);
		
		System.gc();
	}
	
	@SuppressLint("NewApi")
	public void setStageBackground_linear(LinearLayout thisStage, String fileName){
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
	public void setStageBackground_scrollView(ScrollView thisStage, String fileName){
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
