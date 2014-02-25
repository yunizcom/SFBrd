package com.mooStan.smashflappybird;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class soundsController {

	private Context myContext;
	private Activity myActivity;
	
	private GlobalVars globalVariable;
	
	soundsController(Context context, Activity myActivityReference) {
		myContext = context;
		myActivity = myActivityReference;
		
		globalVariable = (GlobalVars) myActivity.getApplicationContext();
	}
	
	//-----------app activity life cycle handling---------------
	public void destroyBgMusic(){
		try {
			globalVariable.bgMusic.stop();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		globalVariable.bgMusic.release();
		//globalVariable.bgMusic = null;
		
		try {
			globalVariable.shortMusic.stop();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		globalVariable.shortMusic.release();
		//globalVariable.shortMusic = null;
		
		try {
			globalVariable.objMusic.stop();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		globalVariable.objMusic.release();
		//globalVariable.objMusic = null;
	}
	
	public void pauseBgMusic(){
		try {
			if(globalVariable.bgMusic.isPlaying()){
				try {
					globalVariable.bgMusic.pause();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			globalVariable.objMusic.setVolume(0.0f, 0.0f);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		globalVariable.minimize = 1;
	}
	
	public void resumeBgMusic(){
		try {
			globalVariable.bgMusic.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(!globalVariable.bgMusic.isPlaying()){
				globalVariable.bgMusic.start();
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			globalVariable.objMusic.setVolume(1.0f, 1.0f);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		globalVariable.minimize = 0;
	}
	//-----------app activity life cycle handling---------------
	
	public void playBgMusic(String filename, boolean looping){
		try {
			globalVariable.bgMusic.reset();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		globalVariable.bgMusic.release();
		//globalVariable.bgMusic = null;
    	
		AssetFileDescriptor descriptor = null;
		try {
			descriptor = myActivity.getAssets().openFd(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long start = descriptor.getStartOffset();
		long end = descriptor.getLength();
		
		globalVariable.bgMusic = new MediaPlayer();
		try {
			globalVariable.bgMusic.setDataSource(descriptor.getFileDescriptor(), start, end);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			descriptor.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			globalVariable.bgMusic.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		globalVariable.bgMusic.setVolume(1.0f,1.0f);
		
		globalVariable.bgMusic.setLooping(looping);
		globalVariable.bgMusic.start();
	}
	
	public void shortSoundClip(String filename){
		try {
			globalVariable.shortMusic.reset();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		globalVariable.shortMusic.release();
		//globalVariable.shortMusic = null;
    	
		AssetFileDescriptor descriptor = null;
		try {
			descriptor = myActivity.getAssets().openFd(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long start = descriptor.getStartOffset();
		long end = descriptor.getLength();
		
		globalVariable.shortMusic = new MediaPlayer();
		try {
			globalVariable.shortMusic.setDataSource(descriptor.getFileDescriptor(), start, end);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			descriptor.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			globalVariable.shortMusic.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		globalVariable.shortMusic.start();
	}
	
	public void objSoundClip(String filename){
		try {
			globalVariable.objMusic.reset();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		globalVariable.objMusic.release();
		//globalVariable.objMusic = null;

		AssetFileDescriptor descriptor = null;
		try {
			descriptor = myActivity.getAssets().openFd(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long start = descriptor.getStartOffset();
		long end = descriptor.getLength();
		
		globalVariable.objMusic = new MediaPlayer();
		try {
			globalVariable.objMusic.setDataSource(descriptor.getFileDescriptor(), start, end);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			descriptor.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			globalVariable.objMusic.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(globalVariable.minimize == 1){
			globalVariable.objMusic.setVolume(0.0f, 0.0f);
		}
		
		globalVariable.objMusic.start();
	}
	
}
