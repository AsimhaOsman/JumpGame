package com.asim.nolimt.superbaby;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import java.util.Calendar;

public class GameView extends View /* SurfaceView implements SurfaceHolder.Callback*/ {

private static final int SLEEP_TIME = 50;
//public static BitmapManager bitmapManager;
public LogicManager logicManager;
private Paint paint;
public  static boolean isGameOver;

    SuperBabyMainActivity superbabyActivity;
    private DataBaseOperation dataBaseOperation;
    public static boolean ispause = false;
       int save_time = 0;
    private boolean isgamerunning = true;

    private Animation alphAnimation;

    public void startEntryAnim(){
       alphAnimation = new AlphaAnimation(0.0f, 1.0f);
       alphAnimation.setDuration(Constants.ANIMATION_TIME);
       startAnimation(alphAnimation);
       }

    public void startExitAnim(){
       alphAnimation = new AlphaAnimation(1.0f, 0.0f);
       alphAnimation.setDuration(Constants.ANIMATION_TIME);
       startAnimation(alphAnimation);
       }

    public GameView(SuperBabyMainActivity context) {
       super(context);
       //bitmapManager = BitmapManager.getBitmapManager(context);
       logicManager = new LogicManager(context);
       paint = new Paint();
       paint.setAntiAlias(true);
       paint.setColor(Color.parseColor("#c5c5c5"));
       isGameOver = false;
       this.superbabyActivity = context;
       dataBaseOperation = new DataBaseOperation(context);
       ispause = false;
       new Thread(new GameThread()).start();
       startEntryAnim();
       }

     public void ReFresh(){

       }

      @Override
        protected void onDraw(Canvas canvas) {
       canvas.drawColor(Color.parseColor("#faf0cc"));
       DrawBackground(canvas);
       if(!ispause){                       //boolean ispause = false;
       logicManager.DrawAndroidAndBars(canvas);
       DrawTitle(canvas);
       }
       else{
       DrawPause(canvas);
       }
       super.onDraw(canvas);
       }

        private void DrawPause(Canvas canvas) {
       Paint paint2 = new Paint();
       paint2.setAntiAlias(true);
       paint2.setTextSize(30 * superbabyActivity.height_mul);
       paint2.setColor(Color.parseColor("#173403"));
       canvas.drawText("resume", superbabyActivity.screen_width/2-40* superbabyActivity.height_mul, superbabyActivity.screen_height/10+50* superbabyActivity.height_mul, paint2);
       canvas.drawText("exit", superbabyActivity.screen_width/2-20* superbabyActivity.height_mul, superbabyActivity.screen_height/10+150* superbabyActivity.height_mul, paint2);
       }


            private void DrawTitle(Canvas canvas) {
       Paint paint = new Paint();
       paint.setAntiAlias(true);
       paint.setColor(Color.parseColor("#45faab"));
       paint.setAlpha(60);
       canvas.drawRect(0, 0, superbabyActivity.screen_width, superbabyActivity.screen_height/16, paint);
       }


            private void DrawBackground(Canvas canvas) {

       for(int i=0; i < superbabyActivity.screen_height; i = (int) (i + 10 * superbabyActivity.height_mul))
       {

           canvas.drawLine(0, i, superbabyActivity.screen_width, i, paint);
       }

       for(int i=0; i < superbabyActivity.screen_width; i = (int) (i + 10 * superbabyActivity.height_mul))
       {
           canvas.drawLine(i, 0, i, superbabyActivity.screen_height, paint);
       }


       }

        @Override
            public boolean onTouchEvent(MotionEvent event) {
       if(event.getAction() == MotionEvent.ACTION_DOWN){
       if(!ispause){
       logicManager.SetAndroidHands();

       }
       else{
       float x = event.getX();
       float y = event.getY();
       if( x > superbabyActivity.screen_width/2-60* superbabyActivity.height_mul &&
               x < superbabyActivity.screen_width/2+80* superbabyActivity.height_mul

       && y > superbabyActivity.screen_height/10+30* superbabyActivity.height_mul &&
               y<superbabyActivity.screen_height/10+90* superbabyActivity.height_mul)
       ispause = false;
       if(x > superbabyActivity.screen_width/2-40* superbabyActivity.height_mul && x < superbabyActivity.screen_width/2+100* superbabyActivity.height_mul
       && y > superbabyActivity.screen_height/10+130* superbabyActivity.height_mul && y<superbabyActivity.screen_height/10+190* superbabyActivity.height_mul){
       LogicManager.isrunning = false;
       isgamerunning = false;
           superbabyActivity.handler.sendEmptyMessage(superbabyActivity.WELCOME);
       GameView.ispause = false;
       }
       }
       }

       return super.onTouchEvent(event);
       }

            private String GetDate() {
       Calendar c = Calendar.getInstance();
       int mYear = c.get(Calendar.YEAR);
       int mMonth = c.get(Calendar.MONTH);
       int mDay = c.get(Calendar.DAY_OF_MONTH);
       int mHour = c.get(Calendar.HOUR_OF_DAY);
       int mMinute = c.get(Calendar.MINUTE);

       return ""+mYear+"-"+mMonth+"-"+mDay+" "+mHour+":"+mMinute;
       }


        private class GameThread implements Runnable{

            @Override
            public void run() {

       while(isgamerunning){
           try {
               //repaint();
               Thread.sleep(GameView.SLEEP_TIME);
           } catch (Exception e) {
               // TODO: handle exception
           }
           postInvalidate();
           //	repaint();
           if(isGameOver == true){
               if(save_time == 0)
                   dataBaseOperation.SaveHeight(OBjectsManager.sum, GetDate());
               save_time ++;
               isgamerunning = false;
               logicManager.Clear();
               superbabyActivity.handler.sendEmptyMessage(superbabyActivity.GAME_OVER);
           }
       }

   }



}
/*
   public void repaint(){
       SurfaceHolder surfaceHolder = this.getHolder();
       Canvas canvas = surfaceHolder.lockCanvas();
       try{
           synchronized (surfaceHolder) {
               onDraw(canvas);
           }
       }catch (Exception e) {
           e.printStackTrace();
       }finally{
           if(canvas != null)
               surfaceHolder.unlockCanvasAndPost(canvas);
       }
   }


   @Override
   public void surfaceChanged(SurfaceHolder holder, int format, int width,
           int height) {
       // TODO Auto-generated method stub

   }

   @Override
   public void surfaceCreated(SurfaceHolder holder) {
       repaint();
   }

   @Override
   public void surfaceDestroyed(SurfaceHolder holder) {
       // TODO Auto-generated method stub

   }
   */
}
