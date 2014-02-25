package com.mooStan.smashflappybird;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.mooStan.smashflappybird.R;
import com.mooStan.smashflappybird.serverComm;
import com.mooStan.smashflappybird.soundsController;
import com.mooStan.smashflappybird.objectsController;
import com.mooStan.smashflappybird.popupBox;
import com.mooStan.smashflappybird.gameEngine;

public class initSetup {
	
	

	public serverComm serverComm;
	public soundsController soundsController;
	public objectsController objectsController;
	public popupBox popupBox;
	public gameEngine gameEngine;
	
	private GlobalVars globalVariable;

	private Context myContext;
	private Activity myActivity;
	
	private int screenWidth = 0, screenHeight = 0, sdk = 0;
	private boolean smallScreen = false;
	
	private RelativeLayout slashScreen, mainMenu, sub_menu, gameStage, leaderBoard, howToPlay;
	private LinearLayout scores_list;
	private ScrollView leaderBoardList;
	
	private ImageView ic_invitefriends, fb_fanpage_btn, trophy_btn, ic_play_btn, ic_continue_btn, ic_gomenu, ic_navi_left, ic_navi_right, ic_how_btn, ic_howto_continue_btn, ic_howto_menu_btn;
	
	private TextView stageLevelShow,stageTimeShow,ic_trophy_game_scores,ic_mybomb_txt;

	initSetup(Context context, Activity myActivityReference) {
		myContext = context;
		myActivity = myActivityReference;
		
		serverComm = new serverComm(myContext,myActivity);
		soundsController = new soundsController(myContext,myActivity);
		objectsController = new objectsController(myContext,myActivity);
		popupBox = new popupBox(myContext,myActivity);
		gameEngine = new gameEngine(myContext,myActivity);
		
		globalVariable = (GlobalVars) myActivity.getApplicationContext();
	}
	
	@SuppressLint("NewApi")
	public void basicDetection(){

		myActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		sdk = android.os.Build.VERSION.SDK_INT;
		
		//----------detect device setting and adapt environment
		Display display = myActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();

		smallScreen = false;
		try
		{ 
			display.getSize(size); 
			screenWidth = size.x; 
			screenHeight = size.y; 
			smallScreen = false;
		} 
		catch (NoSuchMethodError e) 
		{ 
			screenWidth = display.getWidth(); 
			screenHeight = display.getHeight(); 
			smallScreen = true;
		}
	
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		//----------detect device setting and adapt environment
	 
	    slashScreen = (RelativeLayout) myActivity.findViewById(R.id.slashScreen);
		mainMenu = (RelativeLayout) myActivity.findViewById(R.id.mainMenu);
		sub_menu = (RelativeLayout) myActivity.findViewById(R.id.sub_menu);
		gameStage = (RelativeLayout) myActivity.findViewById(R.id.gameStage);
		leaderBoard = (RelativeLayout) myActivity.findViewById(R.id.leaderBoard);
		howToPlay = (RelativeLayout) myActivity.findViewById(R.id.howToPlay);
		
		scores_list = (LinearLayout) myActivity.findViewById(R.id.scores_list);
		leaderBoardList = (ScrollView) myActivity.findViewById(R.id.leaderBoardList);
		
		ic_invitefriends = (ImageView) myActivity.findViewById(R.id.ic_invitefriends);
		fb_fanpage_btn = (ImageView) myActivity.findViewById(R.id.fb_fanpage_btn);
		trophy_btn = (ImageView) myActivity.findViewById(R.id.trophy_btn);
		ic_play_btn = (ImageView) myActivity.findViewById(R.id.ic_play_btn);
		ic_continue_btn = (ImageView) myActivity.findViewById(R.id.ic_continue_btn);
		ic_gomenu = (ImageView) myActivity.findViewById(R.id.ic_gomenu);
		ic_navi_left = (ImageView) myActivity.findViewById(R.id.ic_navi_left);
		ic_navi_right = (ImageView) myActivity.findViewById(R.id.ic_navi_right);
		ic_how_btn = (ImageView) myActivity.findViewById(R.id.ic_how_btn);
		ic_howto_continue_btn = (ImageView) myActivity.findViewById(R.id.ic_howto_continue_btn);
		ic_howto_menu_btn = (ImageView) myActivity.findViewById(R.id.ic_howto_menu_btn);
		
		stageLevelShow = (TextView) myActivity.findViewById(R.id.stageLevelShow);
		stageTimeShow = (TextView) myActivity.findViewById(R.id.stageTimeShow);
		ic_trophy_game_scores = (TextView) myActivity.findViewById(R.id.ic_trophy_game_scores);
		ic_mybomb_txt = (TextView) myActivity.findViewById(R.id.ic_mybomb_txt);
	    
		leaderBoardList.setPadding((screenWidth /20), (screenHeight /10), (screenHeight /20), (screenWidth /20));
		
		objectsController.setWindowDetails(screenWidth,screenHeight,smallScreen,sdk);
		
		if(globalVariable.getmyUID() == ""){
			globalVariable.saveYunizUID();
		}
		
	    uiSetup();
	}

	private void uiSetup(){
		stageController(slashScreen);
		objectsController.setStageBackground(slashScreen,"backgrounds/slashScreen.jpg");
		objectsController.setStageBackground_scrollView(leaderBoardList,"backgrounds/leaderBoard_bg.png");
		globalVariable.curentStage = 0;
		
		Typeface tf = Typeface.createFromAsset(myActivity.getAssets(), "fonts/Cookies.ttf");
		Typeface tf2 = Typeface.createFromAsset(myActivity.getAssets(), "fonts/GROBOLD.ttf");
		stageLevelShow.setTextSize(20f);
		stageLevelShow.setTypeface(tf);
		stageLevelShow.setTextColor(Color.parseColor("#ff7979"));
		
		stageTimeShow.setTextSize(20f);
		stageTimeShow.setTypeface(tf);
		stageTimeShow.setTextColor(Color.parseColor("#ff7979"));
		
		ic_trophy_game_scores.setTextSize(24f);
		ic_trophy_game_scores.setTypeface(tf);
		ic_trophy_game_scores.setTextColor(Color.parseColor("#ff3b3b"));
		
		ic_mybomb_txt.setTextSize(24f);
		ic_mybomb_txt.setTypeface(tf);
		ic_mybomb_txt.setTextColor(Color.parseColor("#ff3b3b"));
		
		if(serverComm.isNetworkAvailable()){
			String webViewHTMLs = "<!DOCTYPE html><head><title>Typing Crush - Facebook Like</title><style>img{border:0px;}</style></head><body style=\"margin:0px;padding:2px 5px;background-color:rgba(247,253,255,0.3);\">" +
					"<iframe src=\"http://www.facebook.com/plugins/like.php?href=https%3A%2F%2Fwww.facebook.com%2Ftypingcrush&amp;width&amp;layout=standard&amp;action=like&amp;show_faces=false&amp;share=false&amp;height=80&amp;appId=466235153487139\" scrolling=\"no\" frameborder=\"0\" style=\"border:none; overflow:hidden; height:100%; width:100%;\" allowTransparency=\"true\"></iframe>" +
					"</body></html>";
			WebView contentWebView=new WebView(myActivity);
			//contentWebView.loadUrl("http://www.facebook.com/plugins/like.php?href=https%3A%2F%2Fwww.facebook.com%2Ftypingcrush&width&layout=standard&action=like&show_faces=true&share=true&height=80&appId=466235153487139");
			contentWebView.loadDataWithBaseURL(null,webViewHTMLs,"text/html","UTF-8","about:blank");
			contentWebView.setBackgroundColor(0);
			contentWebView.setHorizontalScrollBarEnabled(false);
			contentWebView.setVerticalScrollBarEnabled(false);
			contentWebView.setFocusableInTouchMode(false);
			contentWebView.setFocusable(false);
			contentWebView.getSettings().setSupportZoom(false);
			contentWebView.getSettings().setJavaScriptEnabled(false);
			contentWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
			          ((int) LayoutParams.MATCH_PARENT, (screenHeight / 100 * 7));
			
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, contentWebView.getId());
			
			contentWebView.setLayoutParams(params);
			
			mainMenu.addView(contentWebView);

			fb_fanpage_btn.setVisibility(View.INVISIBLE);
			
			contentWebView.setOnTouchListener(new View.OnTouchListener() {
		        @Override
		        public boolean onTouch(View arg0, MotionEvent arg1) {
		            switch (arg1.getAction()) {
			            case MotionEvent.ACTION_UP:{
			            	Uri uri = Uri.parse("https://www.facebook.com/typingcrush");
				       		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				       		myActivity.startActivity(intent);
			            	
			                break;
			            }
		            }
		            return true;
		        }
		    });
		}
		
		slashScreen.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	soundsController.playBgMusic("sounds/splashScreen_sound.mp3",false);
	        	slashScreen.postDelayed(new Runnable() {
	    	        @Override
	    	        public void run() {
	    	        	stageController(mainMenu);
	    	        	objectsController.setStageBackground(mainMenu,"backgrounds/main_menu.jpg");
	    	        	globalVariable.curentStage = 1;
	    	        	
	    	        	serverComm.checkInternetConnection();
	    	        	
	    	        	setEffectListeners();
	    	        }
	    	    }, 2000);
	        }
	    }, 500);
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

    private void setEffectListeners(){
	    fb_fanpage_btn.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	fb_fanpage_btn.setAlpha(180);

		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	fb_fanpage_btn.setAlpha(255);
		            	
		            	Uri uri = Uri.parse("https://www.facebook.com/typingcrush");
			       		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			       		myActivity.startActivity(intent);
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    trophy_btn.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	trophy_btn.setAlpha(180);

		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	trophy_btn.setAlpha(255);
		            	
		            	stageController(leaderBoard);
		            	objectsController.setStageBackground(leaderBoard,"backgrounds/sub_menu.jpg");
		            	globalVariable.curentStage = 2;
		            	
		            	//globalVariable.curResultPageNo = 1;
		            	retreiveLeaderBoard(globalVariable.curResultPageNo,1);

		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_play_btn.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_play_btn.setAlpha(180);

		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_play_btn.setAlpha(255);
		            	
		            	stageController(sub_menu);
		            	objectsController.setStageBackground(sub_menu,"backgrounds/sub_menu.jpg");
		            	globalVariable.curentStage = 2;

		            	objectsController.createLevelOptions(globalVariable.getLevel());
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_continue_btn.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_continue_btn.setAlpha(180);
		            	
		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_continue_btn.setAlpha(255);
		            	
		            	if(globalVariable.getLevel() == 0){
			            	stageController(howToPlay);
			            	objectsController.setStageBackground(howToPlay,"backgrounds/how_to_play.jpg");
			            	globalVariable.curentStage = 3;
		            	}else{
			            	gameEngine.startGameEngine((globalVariable.getLevel() + 1),sdk);
			            	
			            	globalVariable.curentStage = 3;
		            	}
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_how_btn.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_how_btn.setAlpha(180);
		            	
		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_how_btn.setAlpha(255);
		            	
		            	stageController(howToPlay);
		            	objectsController.setStageBackground(howToPlay,"backgrounds/how_to_play.jpg");
		            	globalVariable.curentStage = 3;
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_gomenu.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_gomenu.setAlpha(180);
		            	
		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_gomenu.setAlpha(255);
		            	
		            	globalVariable.curentStage = 1;
						stageController(mainMenu);
						objectsController.setStageBackground(mainMenu,"backgrounds/main_menu.jpg");
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_howto_continue_btn.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_howto_continue_btn.setAlpha(180);
		            	
		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_howto_continue_btn.setAlpha(255);
		            	
		            	gameEngine.startGameEngine((globalVariable.getLevel() + 1),sdk);
		            	
		            	globalVariable.curentStage = 3;
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_howto_menu_btn.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_howto_menu_btn.setAlpha(180);
		            	
		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_howto_menu_btn.setAlpha(255);
		            	
		            	stageController(sub_menu);
		            	objectsController.setStageBackground(sub_menu,"backgrounds/sub_menu.jpg");
		            	globalVariable.curentStage = 2;

		            	objectsController.createLevelOptions(globalVariable.getLevel());
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_navi_left.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_navi_left.setAlpha(180);
		            	
		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_navi_left.setAlpha(255);
		            	
		            	globalVariable.curResultPageNo++;
		            	retreiveLeaderBoard(globalVariable.curResultPageNo,1);
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_navi_right.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_navi_right.setAlpha(180);
		            	
		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_navi_right.setAlpha(255);
		            	
		            	globalVariable.curResultPageNo--;
		            	if(globalVariable.curResultPageNo < 1){
		            		globalVariable.curResultPageNo = 1;
		            	}
		            	
		            	retreiveLeaderBoard(globalVariable.curResultPageNo,1);
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	    
	    ic_invitefriends.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_invitefriends.setAlpha(180);

		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_invitefriends.setAlpha(255);
		            	
		            	popupBox.sendFBInviteDialog();
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
    }

    
    
    public void retreiveLeaderBoard(int page,int level){
    	globalVariable.curResultPageNo = page;
		popupBox.showPopBox("\n\nLoading, please hold on...",3);
		
		trophy_btn.postDelayed(new Runnable() {
	        @Override
	        public void run() {
        	
	        	gameScoresAPI(globalVariable.curResultPageNo, globalVariable.curResultLevels);

	        }
	    }, 1000);
		
		
	}

    public void generateScoreBoard(JSONArray jsoncontacts){
    	if(jsoncontacts.length() == 0 && globalVariable.curResultPageNo > 1){
    		globalVariable.curResultPageNo = 1;
			popupBox.showPopBox("This is the last page of Leader Board.",0);
		}else{

			scores_list.removeAllViewsInLayout();
			
			for(int i=0;i < jsoncontacts.length();i++){						
				
	        	try {
					JSONObject e = jsoncontacts.getJSONObject(i);
					
					if(e.getString("no") != "0" && e.getString("no").length() > 0){
						//pushScoreToBoard((i+1), e.getString("no"), e.getString("n"), e.getString("s"));
						
						LinearLayout levelBox = new LinearLayout(myActivity);
						LinearLayout levelDetails = new LinearLayout(myActivity);
						LinearLayout playDetails = new LinearLayout(myActivity);
						TextView noB = new TextView(myActivity);
						TextView levelText = new TextView(myActivity);
						TextView scoresText = new TextView(myActivity);
						ImageView fbPic = new ImageView(myActivity);
					
						levelBox.setGravity(Gravity.LEFT);
						levelDetails.setOrientation(LinearLayout.VERTICAL);
						playDetails.setOrientation(LinearLayout.HORIZONTAL);
						
						playDetails.setGravity(Gravity.CENTER_VERTICAL);
						
						levelBox.setLayoutParams(new
							       LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
						levelDetails.setLayoutParams(new
							       LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
						playDetails.setLayoutParams(new
							       LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
						/*levelText.setLayoutParams(new
							       LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));*/
						scoresText.setLayoutParams(new
							       LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
						
						fbPic.setLayoutParams(new
							       LayoutParams(50,50));
						fbPic.setScaleType(ImageView.ScaleType.FIT_CENTER);
						
						Typeface tf = Typeface.createFromAsset(myActivity.getAssets(), "fonts/Cookies.ttf");
						levelText.setTypeface(tf);
						levelText.setTextSize(22);
						levelText.setPadding(3, 10, 20, 0);
						
						noB.setTypeface(tf);
						noB.setTextSize(22);
						noB.setPadding(20, 10, 3, 0);
						
						noB.setText(e.getString("no") + ")");
						levelText.setText(e.getString("n"));
						
						if(e.getString("fb").equals("")){
							fbPic.setImageResource(R.drawable.ic_playeravatar);
						}else{
							Bitmap bitmap = null;
							
							try {
								bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://graph.facebook.com/" + e.getString("fb") + "/picture").getContent());
							} catch (MalformedURLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							fbPic.setImageBitmap(bitmap);
						}
						
						scoresText.setTypeface(tf);
						scoresText.setTextSize(22);
						scoresText.setPadding(20, 10, 20, 10);
						scoresText.setGravity(Gravity.CENTER);
						
						scoresText.setText(e.getString("s") + " pts @ lvl" + e.getString("l"));
						
						noB.setTextColor(Color.parseColor("#d80606"));
						levelText.setTextColor(Color.parseColor("#d80606"));
						scoresText.setTextColor(Color.parseColor("#212121"));
	
						playDetails.addView(noB);
						playDetails.addView(fbPic);
						playDetails.addView(levelText);
						levelDetails.addView(playDetails);
						levelDetails.addView(scoresText);
						
						levelBox.setId(i);
						levelBox.addView(levelDetails);
						
						scores_list.addView(levelBox);
						
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
			}
			
			leaderBoardList.smoothScrollTo(0, 0);
			
			System.gc();
		}
    }
    
	public void gameScoresAPI(int pages, int levels){
		String url = globalVariable.gameServerAPI_URL + "?mod=2&page=" + pages + "&levels=" + levels;
		//-------load JSON
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        //nameValuePairs.add(new BasicNameValuePair("convo_id", "4546db1fd1"));
        //nameValuePairs.add(new BasicNameValuePair("say", words));
		
		JSONObject json = globalVariable.getJSONfromURL(url, nameValuePairs);
		popupBox.closePopBox();
		try {
			if(json == null){
				popupBox.showPopBox("You need a smooth internet connection to retrieve LeaderBoard.",0);
			}else{
				generateScoreBoard(json.getJSONArray("hallOfFame"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//-------load JSON
	}
    
	public void pauseGame(){
		gameEngine.gameActivity(false);
	}
	
	public void resumeGame(){
		gameEngine.gameActivity(true);
	}
	
	public void handleNativeBackAction(){
		soundsController.shortSoundClip("sounds/buttons_clicked.mp3");

		if(globalVariable.isPopUpOpen == false && globalVariable.isResultOpen == false && globalVariable.isBlockOpen == false && globalVariable.isEnterName == false){

			if(globalVariable.curentStage < 0){
				globalVariable.curentStage = 0;
			}

			switch(globalVariable.curentStage){
				case 2 : {
					globalVariable.curentStage = 1;
					stageController(mainMenu);
					objectsController.setStageBackground(mainMenu,"backgrounds/main_menu.jpg");
					
					break;
				}
				case 3 : {
					globalVariable.curentStage = 2;
					gameEngine.stopGameStage();
					stageController(sub_menu);
					objectsController.setStageBackground(sub_menu,"backgrounds/sub_menu.jpg");
					
					objectsController.createLevelOptions(globalVariable.getLevel());
					
					break;
				}
				case 1 : {
					globalVariable.curentStage = 0;
					//myActivity.finish();
					
					break;
				}
			}
		}else{
			if(globalVariable.isBlockOpen == false){
				
				if(globalVariable.isResultOpen == true){
	
					switch(globalVariable.curentStage){
						case 2 : {
							globalVariable.curentStage = 1;
	
							break;
						}
						case 3 : {
							globalVariable.curentStage = 2;
	
							break;
						}
						case 1 : {
							globalVariable.curentStage = 0;
	
							break;
						}
					}
					
					stageController(sub_menu);
					objectsController.createLevelOptions(globalVariable.getLevel());
					globalVariable.submitClicked = false;
					popupBox.closePopBox();
				}else if(globalVariable.isEnterName == true){
					popupBox.closePopBox();
					
					popupBox.showPopBox("",1);
				}else if(globalVariable.isPopUpOpen == true){
					popupBox.closePopBox();
				}
			}
		}
	}
	
}
