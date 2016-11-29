package com.asim.nolimt.superbaby;

import android.graphics.Canvas;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;


public class LogicManager {
	
	public  static final int FALL_DOWN_DAMAGE = 40;  
	private static final int SLEEP_TIME       = 40;
	
	private OBjectsManager objectsManager;
	private AbstractAndroid android;
	//	private Android android;
	
	private boolean game_started = false;
	public static boolean isrunning;


	public static boolean[] person;
	public static int choose;
	SuperBabyMainActivity context;
	SuperBabyMainActivity superbabyActivity;
	
	public LogicManager(SuperBabyMainActivity context){
		this.context = context;
	    objectsManager = new OBjectsManager(context);
		android = Android.AndroidFactory(context);
		person = new boolean[2];
     	person[0] = false;
		person[1] = false;
	//	soundPlayer = new SoundPlayer(context);
		isrunning = true;
		new Thread(new LogicThread()).start();
	}
	
	public void Clear(){
		isrunning = false;
		android = null;
		objectsManager.Clear();
	}
	
	public void DrawAndroidAndBars(Canvas canvas){
		person[0] = true;
		choose = 1;
		while(person[1]&&choose==1);
		
		objectsManager.DrawBarsAndMonsters(canvas);
		android.DrawSelf(canvas);


		person[0] = false;
	}

	public void SetAndroidHands(){
		android.bitmap_index = Android.HANDS_UP;
	}

	public void SetAndroid_HSpeed(float horizonal_speed){
		android.horizonal_speed = - horizonal_speed;
	}
	
	

	
	private class LogicThread implements Runnable{

		float add;
		@Override
		public void run() {
			while(isrunning){
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (Exception e) {
					
				}
				finally{
					if(!GameView.ispause){
						android.Move();
						android.CheckAndroidCoor(objectsManager);
						CheckVertivalSpeed();

						objectsManager.CheckisTouchItems(android);

						if(android.life_num < 0){
							isrunning = false;
							GameView.isGameOver = true;
						}
					}
				}
			}
		}
		
		





		private void CheckVertivalSpeed() {
			if(android.current_state == Android.STATE_GO_UP){
				android.LTCoorY += android.vertical_speed;
				if(game_started/* && !objectsManager.isrepeated*/){
					objectsManager.MoveBarsAndMonstersDown(android.vertical_speed, add);
					/*for(int i=0; i<explodes.size(); i++){
						 if(explodes.get(i) != null){
							explodes.get(i).CoorY -= android.vertical_speed;
							explodes.get(i).CoorY += add;
						 }
					 }*/
					
				}
				if(android.vertical_speed >= 0){
					android.vertical_speed = 0;
					android.current_state = Android.STATE_GO_DOWN;
					game_started = true;
				}
			
			}
		    if(android.current_state == Android.STATE_GO_DOWN) {
		    	if(android.vertical_speed >= Android.MAX_VERTICAL_SPEED)
			    		android.vertical_speed = Android.MAX_VERTICAL_SPEED;
		    	float temp =  android.vertical_speed;
		    	for(float i=0; i<=temp; i += 0.5){
		    		android.LTCoorY += 0.5;
			    	if(objectsManager.isTouchBars(android.LTCoorX, android.LTCoorY)){
			    		if(objectsManager.isrepeated){
			    			//SoundPlayer.playSound(SoundPlayer.SOUND_NORMAL_BAR);
			    			android.vertical_speed = Android.INITIAL_VERTICAL_SPEED;
			    		}
			    		else{ 
			    			if(objectsManager.touch_bar_type == AbstractBar.TYPE_NORMAL 
			    			   || objectsManager.touch_bar_type == AbstractBar.TYPE_SHIFT){
			    				Log.e("eror", "error");
			    				//SoundPlayer.playSound(SoundPlayer.SOUND_NORMAL_BAR);
			    				add = getAdd(android.LTCoorY);
			    				android.vertical_speed = Android.INITIAL_VERTICAL_SPEED + add;
			    			}
			    			else{
			    				add = getAdd(android.LTCoorY);
			    				android.vertical_speed = Android.INITIAL_VERTICAL_SPEED + add;
			    				add = 30 * SuperBabyMainActivity.height_mul;
			    			}

			    		}
			    		android.current_state = Android.STATE_GO_UP;
			    		break;
			    	}
		    	}
		      
			}
		}

		private int getAdd(float lTCoorY) {
			if(lTCoorY >= 350 * superbabyActivity.height_mul)
				return (int) (0 * superbabyActivity.height_mul);
			else if(lTCoorY >= 300 * superbabyActivity.height_mul)
				return (int) (5 * superbabyActivity.height_mul);
			else 
				return (int) (10 * superbabyActivity.height_mul);
		}
		
	}

}
