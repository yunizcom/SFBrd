package com.mooStan.smashflappybird;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mooStan.smashflappybird.R;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.mooStan.smashflappybird.soundsController;

public class popupBox extends Activity {

	private Context myContext;
	private Activity myActivity;
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
	private RelativeLayout popbox, slashScreen, mainMenu, sub_menu, gameStage, leaderBoard;
	private LinearLayout popboxCenter,popboxResult;
	private ImageView popboxOK_btn,popbox_trophy,ic_popbox_close,ic_fb_share,ic_submit_button;
	private TextView popboxMSG,popboxResultPts,popboxResultLvl2,popboxResultLvl,popboxResultPts2,shareTips;
	private LinearLayout level_list;
	private EditText mynickName;
	
	public Bitmap fbImg;
	public String fbMsg;
	
	public soundsController soundsController;
	private GlobalVars globalVariable;
	
	private int boxType = 0;
	
	popupBox(Context context, Activity myActivityReference) {
		myContext = context;
		myActivity = myActivityReference;
		
		soundsController = new soundsController(myContext,myActivity);
		globalVariable = (GlobalVars) myActivity.getApplicationContext();

		popbox = (RelativeLayout) myActivity.findViewById(R.id.popbox);
		popboxCenter = (LinearLayout) myActivity.findViewById(R.id.popboxCenter);
		popboxResult = (LinearLayout) myActivity.findViewById(R.id.popboxResult);
		popboxOK_btn = (ImageView) myActivity.findViewById(R.id.popboxOK_btn);
		popboxMSG = (TextView) myActivity.findViewById(R.id.popboxMSG);
		popboxResultPts = (TextView) myActivity.findViewById(R.id.popboxResultPts);
		popboxResultPts2 = (TextView) myActivity.findViewById(R.id.popboxResultPts2);
		popboxResultLvl2 = (TextView) myActivity.findViewById(R.id.popboxResultLvl2);
		popboxResultLvl = (TextView) myActivity.findViewById(R.id.popboxResultLvl);
		shareTips = (TextView) myActivity.findViewById(R.id.shareTips);
		popbox_trophy = (ImageView) myActivity.findViewById(R.id.popbox_trophy);
		ic_popbox_close = (ImageView) myActivity.findViewById(R.id.ic_popbox_close);
		ic_fb_share = (ImageView) myActivity.findViewById(R.id.ic_fb_share);
		ic_submit_button = (ImageView) myActivity.findViewById(R.id.ic_submit_button);
		
		level_list = (LinearLayout) myActivity.findViewById(R.id.level_list);
		
		mynickName = (EditText) myActivity.findViewById(R.id.mynickName);
		
		slashScreen = (RelativeLayout) myActivity.findViewById(R.id.slashScreen);
		mainMenu = (RelativeLayout) myActivity.findViewById(R.id.mainMenu);
		sub_menu = (RelativeLayout) myActivity.findViewById(R.id.sub_menu);
		gameStage = (RelativeLayout) myActivity.findViewById(R.id.gameStage);
		leaderBoard = (RelativeLayout) myActivity.findViewById(R.id.leaderBoard);
		
		setStageBackground_relative(popbox,"backgrounds/opacity_50_bg.png");
		setStageBackground_linear(popboxCenter,"backgrounds/popUp_bg.png");
		setStageBackground_linear(popboxResult,"backgrounds/popUp_bg2.png");
		
		Typeface tf = Typeface.createFromAsset(myActivity.getAssets(), "fonts/Cookies.ttf");
		popboxMSG.setTypeface(tf);
		popboxMSG.setTextSize(20);
		popboxMSG.setPadding(10, 0, 10, 20);
		popboxMSG.setTextColor(Color.parseColor("#7a7a7a"));
		
		tf = Typeface.createFromAsset(myActivity.getAssets(), "fonts/GROBOLD.ttf");
		popboxResultLvl.setTypeface(tf);
		popboxResultLvl.setTextSize(30);
		popboxResultLvl.setTextColor(Color.parseColor("#fc0000"));
		popboxResultLvl2.setTypeface(tf);
		popboxResultLvl2.setTextSize(30);
		popboxResultLvl2.setTextColor(Color.parseColor("#373737"));
		
		popboxResultPts.setTypeface(tf);
		popboxResultPts.setTextSize(50);
		popboxResultPts.setTextColor(Color.parseColor("#fd3636"));
		popboxResultPts2.setTypeface(tf);
		popboxResultPts2.setTextSize(50);
		popboxResultPts2.setTextColor(Color.parseColor("#373737"));
		
		mynickName.setTypeface(tf);
		mynickName.setTextSize(35);
		
		popboxOK_btn.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	popboxOK_btn.setAlpha(180);

		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	popboxOK_btn.setAlpha(255);
		            	
		            	closePopBox();
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
		
		ic_popbox_close.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_popbox_close.setAlpha(180);

		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_popbox_close.setAlpha(255);

		            	globalVariable.curentStage = 2;
		            	
		            	closePopBox();
		            	globalVariable.submitClicked = false;
		            	//updateResultBoard(globalVariable.currentLevels,200);
		            	
		            	stageController(mainMenu);

		                break;
		            }
	            }
	            return true;
	        }
	    });
		
		ic_fb_share.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_fb_share.setAlpha(180);

		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_fb_share.setAlpha(255);
		            	
		            	RelativeLayout myview = (RelativeLayout) myActivity.findViewById(R.id.myview);
		            	if(globalVariable.submitClicked == false){
		            		publishPhoto(loadBitmapFromView_BITMAP(myview),"I just break a new record with " + globalVariable.scores + " points at level " + globalVariable.currentLevels + " in Smash Flappy Bird! How about you? ");
		            	}else{
		            		publishPhoto(loadBitmapFromView_BITMAP(myview),"I just break a new record with " + globalVariable.scores + " points at level " + globalVariable.currentLevels + " and #" + globalVariable.curSubmittedRank + " at the World Ranking in Smash Flappy Bird! How about you? ");
		            	}
		        		
		            	
		            	//publishStory();
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
		
		ic_submit_button.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		            	soundsController.shortSoundClip("sounds/buttons_clicked.mp3");
		            	ic_submit_button.setAlpha(180);

		                break;
		            }
		            case MotionEvent.ACTION_UP:{
		            	ic_submit_button.setAlpha(255);
		            	
		            	if(globalVariable.isEnterName == true){
		            		if(mynickName.getText().toString().length() == 0){
		            			mynickName.setText("New Player");
		            		}
		            		globalVariable.saveYunizSaved(globalVariable.getLevel(), globalVariable.getBomb(), mynickName.getText().toString(), globalVariable.getShare());
		            		
		            		globalVariable.submitClicked = true;
		            		
		            		submitScore();
		            	}else{
		            		showPopBox("",4);
		            	}
		            	
		                break;
		            }
	            }
	            return true;
	        }
	    });
	}
	
	/* for FB SDK */
	  public static Bitmap loadBitmapFromView_BITMAP(View v) {
		    Bitmap bitmap;
			View v1 = v.getRootView();
			v1.setDrawingCacheEnabled(true);
			bitmap = Bitmap.createBitmap(v1.getDrawingCache());

			bitmap=Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * 1), (int)(bitmap.getHeight() * 1), true);
			v1.setDrawingCacheEnabled(false);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
			
			 return bitmap;
		}
	  
	    public void publishPhoto(Bitmap imgData, String message) {
	    	fbImg = imgData;
	    	fbMsg = message;
			Session session = Session.getActiveSession();
				
		    if (session != null){//Log.v("DEBUG","FB session 2 : " + session + " | " + session.isOpened());

		        // Check for publish permissions    
		        List<String> permissions = session.getPermissions();
		        if (!isSubsetOf(PERMISSIONS, permissions)) {
		            pendingPublishReauthorization = true;
		            Session.NewPermissionsRequest newPermissionsRequest = new Session
		                    .NewPermissionsRequest(myActivity, PERMISSIONS);
		        session.requestNewPublishPermissions(newPermissionsRequest);
		            return;
		        }else{
		        	closePopBox();
		    		showPopBox("\n\nUploading, please hold \non...",3);
		        }

		        Request request = Request.newUploadPhotoRequest(session, imgData, new Request.Callback()
		        {
		            @Override
		            public void onCompleted(Response response)
		            {
		            	//Log.v("DEBUG","DONE UPLOAD : " + response);
		            	closePopBox();
						showPopBox("\n\nScore has been successfully shared on your Facebook wall.",3);
						
						popboxOK_btn.postDelayed(new Runnable() {
					        @Override
					        public void run() {
					        	
					        	if(globalVariable.getShare() == 0){
					        		globalVariable.saveYunizSaved(globalVariable.getLevel(), (globalVariable.getBomb() + 1), globalVariable.getPlayerName(), 1);
					        	}
					        	
					        	closePopBox();
					        	showPopBox("",1);

					        }
					    }, 2000);
		            }
		        });
		        Bundle postParams = request.getParameters(); // <-- THIS IS IMPORTANT
		        postParams.putString("name", message + " Install and join me at http://goo.gl/xWNDmf #smashflappybird #smashflappybirdlevel" + globalVariable.currentLevels);
		        postParams.putString("link","http://goo.gl/xWNDmf");
		        request.setParameters(postParams);
		        request.executeAsync();
		        
		    }else{
		    	Session.openActiveSession(myActivity, true, new Session.StatusCallback() {
		    		
				    // callback when session changes state
				    @SuppressWarnings("deprecation")
					@Override
				    public void call(Session session, SessionState state, Exception exception) {
						//Log.v("DEBUG","FB session : " + session + " | " + session.isOpened());	
				    	if (session.isOpened()) {

				    		// make request to the /me API
				    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

				    		  // callback after Graph API response with user object
				    		  @Override
				    		  public void onCompleted(GraphUser user, Response response) {
				    			  
				    			  
				    			  if (user != null) {
				    				  //Log.v("DEBUG","LOGIN FB : " + user.getId() + " | " + user.getName());
				    				  globalVariable.saveYunizFBID(user.getId());
				    				  mynickName.setText(user.getName());
				    				  globalVariable.saveYunizSaved(globalVariable.getLevel(), globalVariable.getBomb(), user.getName(), globalVariable.getShare());
				    			  }
				    			  if(globalVariable.curentStage == 1){
				    				  sendFBInviteDialog();
				    			  }else{
				    				  publishPhoto(fbImg,fbMsg);
				    			  }
				    			  
				    		  }
				    		});
				    		
				    	}
				    	
				    }
				  });
		    }

		}

	    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		    for (String string : subset) {
		        if (!superset.contains(string)) {
		            return false;
		        }
		    }
		    return true;
		}
	    
	    public void sendFBInviteDialog() {
	    	Session session = Session.getActiveSession();
	    	
	    	if (session != null){
	    		Bundle params = new Bundle();
	    	    params.putString("message", "Come! Play Smash Flappy Bird with me.");

	    	    WebDialog requestsDialog = (
	    	        new WebDialog.RequestsDialogBuilder(myActivity,
	    	            Session.getActiveSession(),
	    	            params))
	    	            .setOnCompleteListener(new OnCompleteListener() {

	    	                @Override
	    	                public void onComplete(Bundle values,
	    	                    FacebookException error) {
	    	                    if (error != null) {
	    	                        if (error instanceof FacebookOperationCanceledException) {
	    	                            //Log.v("debug","cancel");
	    	                        } else {
	    	                        	//Log.v("debug","network error");
	    	                        }
	    	                    } else {
	    	                        final String requestId = values.getString("request");
	    	                        if (requestId != null) {
	    	                        	//Log.v("debug","sent");
	    	                        	globalVariable.saveYunizSaved(globalVariable.getLevel(), (globalVariable.getBomb() + 1), globalVariable.getPlayerName(), globalVariable.getShare());
	    	                        } else {
	    	                        	//Log.v("debug","cancel");
	    	                        }
	    	                    }   
	    	                }

	    	            })
	    	            .build();
	    	    requestsDialog.show();
	    	}else{
	    		Session.openActiveSession(myActivity, true, new Session.StatusCallback() {
		    		
				    // callback when session changes state
				    @SuppressWarnings("deprecation")
					@Override
				    public void call(Session session, SessionState state, Exception exception) {
						//Log.v("DEBUG","FB session : " + session + " | " + session.isOpened());	
				    	if (session.isOpened()) {

				    		// make request to the /me API
				    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

				    		  // callback after Graph API response with user object
				    		  @Override
				    		  public void onCompleted(GraphUser user, Response response) {
				    			  
				    			  if (user != null) {
				    				  //Log.v("DEBUG","LOGIN FB : " + user.getId() + " | " + user.getName());
				    				  globalVariable.saveYunizFBID(user.getId());
				    				  mynickName.setText(user.getName());
				    				  globalVariable.saveYunizSaved(globalVariable.getLevel(), globalVariable.getBomb(), user.getName(), globalVariable.getShare());
				    			  }
				    			  if(globalVariable.curentStage == 1){
				    				  sendFBInviteDialog();
				    			  }else{
				    				  publishPhoto(fbImg,fbMsg);
				    			  }
				    			  
				    		  }
				    		});
				    		
				    	}
				    	
				    }
				  });
	    	}
		}
	/* for FB SDK */  
	
	public void closePopBox(){
		shareTips.setVisibility(View.INVISIBLE);
		popbox.setVisibility(View.INVISIBLE);
    	popbox.setClickable(false);
    	globalVariable.isPopUpOpen = false;
    	globalVariable.isResultOpen = false;
    	globalVariable.isBlockOpen = false;
    	globalVariable.isEnterName = false;
	}
	
	public void showPopBox(String msg, int cboxType){
		popbox.setVisibility(View.VISIBLE);
		popboxMSG.setText(msg);
		popbox.setClickable(true);
		
		boxType = cboxType;
		
		popboxCenter.setVisibility(View.INVISIBLE);
		popboxResult.setVisibility(View.INVISIBLE);
		popbox_trophy.setVisibility(View.INVISIBLE);
		ic_popbox_close.setVisibility(View.INVISIBLE);
		
		mynickName.setVisibility(View.INVISIBLE);
		
		globalVariable.isPopUpOpen = false;
    	globalVariable.isResultOpen = false;
    	globalVariable.isBlockOpen = false;
    	globalVariable.isEnterName = false;
		
		switch(boxType){
			case 0:{ // normal message prompt
				popboxCenter.setVisibility(View.VISIBLE);
				
				popboxOK_btn.setVisibility(View.VISIBLE);
				
				globalVariable.isPopUpOpen = true;
				break;
			}
			case 1:{ // result shown
				shareTips.setVisibility(View.VISIBLE);
				soundsController.shortSoundClip("sounds/splashScreen_sound.mp3");
				
				popboxResultLvl.setText("Level " + globalVariable.currentLevels);
				popboxResultLvl2.setText("Level " + globalVariable.currentLevels);
				
				popboxResultPts.setVisibility(View.VISIBLE);
				popboxResultPts2.setVisibility(View.VISIBLE);

				if(globalVariable.submitClicked == false){
					ic_submit_button.setVisibility(View.VISIBLE);
					
					popboxResultPts.setTextSize(50);
					popboxResultPts2.setTextSize(50);
					
					popboxResultPts.setText(globalVariable.scores + "\npoints");
					popboxResultPts2.setText(globalVariable.scores + "\npoints");
				}else{
					ic_submit_button.setVisibility(View.INVISIBLE);
					
					popboxResultPts.setTextSize(25);
					popboxResultPts2.setTextSize(25);
					
					popboxResultPts.setText(globalVariable.scores + "\npoints\n\nWorld Rank\n#" + globalVariable.curSubmittedRank);
					popboxResultPts2.setText(globalVariable.scores + "\npoints\n\nWorld Rank\n#" + globalVariable.curSubmittedRank);
				}
				ic_fb_share.setVisibility(View.VISIBLE);
				
				popboxResult.setVisibility(View.VISIBLE);
				popbox_trophy.setVisibility(View.VISIBLE);
				ic_popbox_close.setVisibility(View.VISIBLE);
				
				globalVariable.isResultOpen = true;
				break;
			}
			case 2:{ // score failed
				soundsController.shortSoundClip("sounds/levelFailed.mp3");
				
				popboxResultLvl.setText("Level " + globalVariable.currentLevels + " FAILED");
				popboxResultLvl2.setText("Level " + globalVariable.currentLevels + " FAILED");
				popboxResultPts.setVisibility(View.VISIBLE);
				popboxResultPts2.setVisibility(View.VISIBLE);
				
				int topScore = Integer.valueOf(globalVariable.getSelectedYunizScores(globalVariable.currentLevels));
				
				if(topScore >= globalVariable.scores){
					popboxResultPts.setText("You need " + (topScore + 1) + " points");
					popboxResultPts2.setText("You need " + (topScore + 1) + " points");
				}else{
					popboxResultPts.setText("You need " + (((globalVariable.currentLevels * 10) + globalVariable.currentLevels + 1) * 2) + " points");
					popboxResultPts2.setText("You need " + (((globalVariable.currentLevels * 10) + globalVariable.currentLevels + 1) * 2) + " points");
				}
				
				ic_submit_button.setVisibility(View.INVISIBLE);
				ic_fb_share.setVisibility(View.INVISIBLE);
				
				popboxResult.setVisibility(View.VISIBLE);
				ic_popbox_close.setVisibility(View.VISIBLE);
				
				globalVariable.isResultOpen = true;
				break;
			}
			case 3:{ // progress dialog
				popboxCenter.setVisibility(View.VISIBLE);
				
				popboxOK_btn.setVisibility(View.INVISIBLE);
				
				globalVariable.isBlockOpen = true;
				break;
			}
			case 4:{ // enter your name
				popboxResultLvl.setText("Your Name ?");
				popboxResultLvl2.setText("Your Name ?");
				
				popboxResultPts.setVisibility(View.INVISIBLE);
				popboxResultPts2.setVisibility(View.INVISIBLE);
				
				ic_submit_button.setVisibility(View.VISIBLE);
				ic_fb_share.setVisibility(View.INVISIBLE);
				
				mynickName.setVisibility(View.VISIBLE);
				mynickName.setText(globalVariable.getPlayerName());
				
				popbox_trophy.setVisibility(View.VISIBLE);
				
				popboxResult.setVisibility(View.VISIBLE);
				ic_popbox_close.setVisibility(View.VISIBLE);
				
				globalVariable.isEnterName = true;
				break;
			}
		}

	}
	
	@SuppressLint("NewApi")
	private void setStageBackground_linear(LinearLayout thisStage, String fileName){

		try 
		{
			InputStream ims = myActivity.getAssets().open(fileName);
		    Drawable d = Drawable.createFromStream(ims, null);
		    
		    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
	private void setStageBackground_relative(RelativeLayout thisStage, String fileName){

		try 
		{
			InputStream ims = myActivity.getAssets().open(fileName);
		    Drawable d = Drawable.createFromStream(ims, null);
		    
		    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
	
	private void updateResultBoard(int level, int scores){
		level_list.removeViewAt((level - 1));

	}
	
	private void submitScore(){
		closePopBox();
		showPopBox("\n\nProcessing, please hold on...",3);
		
		popboxOK_btn.postDelayed(new Runnable() {
	        @Override
	        public void run() {
        	
	        	gameScoreSubmitAPI(globalVariable.getPlayerName(),globalVariable.scores);

	        }
	    }, 1000);
		
	}
	
	private void gameScoreSubmitAPI(String nickname, int scores){
		try {
			nickname = URLEncoder.encode(nickname, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = globalVariable.gameServerAPI_URL + "?mod=1&nickname=" + nickname + "&score=" + scores + "&uid=" + globalVariable.getmyUID() + "&fbid=" + globalVariable.getmyFBID() + "&levels=" + globalVariable.currentLevels;
		//-------load JSON
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        //nameValuePairs.add(new BasicNameValuePair("convo_id", "4546db1fd1"));
        //nameValuePairs.add(new BasicNameValuePair("say", words));

		JSONObject json = globalVariable.getJSONfromURL(url, nameValuePairs);

		try {
			if(json == null){
				closePopBox();
				showPopBox("\n\nScore failed to submit, please try again.",3);
				globalVariable.submitClicked = false;
				
				popboxOK_btn.postDelayed(new Runnable() {
			        @Override
			        public void run() {
		        	
			        	closePopBox();
			        	showPopBox("",1);

			        }
			    }, 2000);
			}else{
				//curPosistion = json.getString("curPosistion");
				//curPage = json.getString("curPage");
				//breakRecords = json.getString("breakRecords");
				globalVariable.curSubmittedRank = json.getString("curPosistion");
				//loadBoard(json.getJSONArray("hallOfFame"));
				
				closePopBox();
				showPopBox("\n\nScore successfully submitted.",3);
				
				popboxOK_btn.postDelayed(new Runnable() {
			        @Override
			        public void run() {
		        	
			        	closePopBox();
			        	showPopBox("",1);

			        }
			    }, 1000);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//-------load JSON
	}
	
}
