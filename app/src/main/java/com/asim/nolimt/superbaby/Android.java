package com.asim.nolimt.superbaby;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

public class Android  extends AbstractAndroid{


	public float accelerameter;
	public float horizonal_speed;
	public float vertical_speed;

	public int current_state;
	public int life_bar;
	public int bullet_times;
	public int life_num;
	public boolean isfalldown;
	public int bitmap_index;
	public int bullet_level;
	SuperBabyMainActivity superbabyActivity;




	private boolean ishurt = false;
	private int  invincible_time = 0;
	
	public Bitmap normal_android;
	
	public Android(SuperBabyMainActivity context, int X, int Y){
		this(context);
		this.LTCoorX = X;
		this.LTCoorY = Y;
		
	}
	
	public Android(SuperBabyMainActivity context){
		this.LTCoorX = INITIAL_COORX;
		this.LTCoorY = INITIAL_COORY;
		this.horizonal_speed = 0;
		this.vertical_speed = INITIAL_VERTICAL_SPEED + 5 * superbabyActivity.height_mul;
		this.current_state = STATE_GO_UP;
	    accelerameter = DEFAULT_VERTICAL_ACCELERATE;
	    this.life_bar = INITIAL_LIFE_BAR;
	    this.life_num = INITIAL_LIFE_NUM;
	    this.bullet_times = INITIAL_LUANCHER_BULLET_TIMES;
	    this.bitmap_index = HANDS_DOWN; 
	    this.isfalldown = false;
	    this.bullet_level = 1;

	    initBitMap(context);
	}
	


	private void initBitMap(SuperBabyMainActivity context){
		//normal_android = BitmapFactory.decodeResource()
		normal_android = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android)).getBitmap();
		//normal_android = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_launch)).getBitmap();
		
		//hurt_android = new Bitmap[2];
	//	hurt_android[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_hurt)).getBitmap();
		//hurt_android[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_hurt_launch)).getBitmap();
		
		//life_littleandroid = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.life_bar_1)).getBitmap();
		
		//bullet = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bullet)).getBitmap();
	}

	@Override
	public void DrawSelf(Canvas canvas) {
		if(!ishurt)                                       //boolean ishurt = false;
			canvas.drawBitmap(normal_android, LTCoorX, LTCoorY, null);
		else{
			if(invincible_time %2 == 0)
				canvas.drawBitmap(normal_android, LTCoorX, LTCoorY, null);
			else
				//canvas.drawBitmap(hurt_android[bitmap_index], LTCoorX, LTCoorY, null);
			invincible_time ++;
			if(invincible_time > 20){
				invincible_time = 0;
				ishurt = false;
			}
		}
		DrawLifeBar(canvas);
		DrawLifeNumandBulletTimes(canvas);
		if(bitmap_index == HANDS_UP)
			bitmap_index = HANDS_DOWN;
	}

	
	

	private void DrawLifeNumandBulletTimes(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(15* superbabyActivity.height_mul);
		canvas.drawText("Life X", superbabyActivity.screen_width/3, 12* superbabyActivity.height_mul, paint);
		canvas.drawText(" X"+bullet_times, superbabyActivity.screen_width/3+140* superbabyActivity.height_mul, 25* superbabyActivity.height_mul, paint);
	}

	private void DrawLifeBar(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(15* superbabyActivity.height_mul);
		canvas.drawText("HP:", superbabyActivity.screen_width/3, 25* superbabyActivity.height_mul, paint);
		if(ishurt)
			paint.setColor(Color.RED);
		else
			paint.setColor(Color.GREEN);
		canvas.drawRect(superbabyActivity.screen_width/3+25* superbabyActivity.height_mul, 15* superbabyActivity.height_mul, superbabyActivity.screen_width/3+25* superbabyActivity.height_mul+life_bar * superbabyActivity.height_mul, 25* superbabyActivity.height_mul, paint);
	}

	public static AbstractAndroid AndroidFactory(SuperBabyMainActivity context){
		return new Android(context);
	}

	@Override
	public void Move() {
		vertical_speed += accelerameter*1;   //this.horizonal_speed = 0;
		                                     //this.vertical_speed = INITIAL_VERTICAL_SPEED + 5
											// 	* DoodleJumpActivity.height_mul;
		LTCoorX += horizonal_speed;
		horizonal_speed = 0;
	}

	@Override
	public void CheckAndroidCoor(OBjectsManager oBjectsManager) {
		if(LTCoorY > superbabyActivity.screen_height){
			this.MinusLifeBar(LogicManager.FALL_DOWN_DAMAGE);
			vertical_speed = Android.INITIAL_VERTICAL_SPEED;
			current_state = Android.STATE_GO_UP;
			oBjectsManager.isrepeated = false;
		}
		if(LTCoorX <= -40 * superbabyActivity.width_mul)
			LTCoorX = superbabyActivity.screen_width - 40 * superbabyActivity.width_mul;
		if(LTCoorX >= superbabyActivity.screen_width)
			LTCoorX = 0;
	}

	@Override
	public void MinusLifeBar(int num) {
		if(!ishurt){
		    //SoundPlayer.playSound(SoundPlayer.SOUND_INJURY);
			ishurt = true;
			this.life_bar -= num;
			if(this.life_bar <= 0){
				this.life_bar = INITIAL_LIFE_BAR;
				this.life_num --;
				this.bullet_level = 1;
			}
		}
	}

	
	@Override
	public void AddLifeBar(int num) {
		this.life_bar += num;
		if(this.life_bar > INITIAL_LIFE_BAR)
			this.life_bar = INITIAL_LIFE_BAR;
	}

}
