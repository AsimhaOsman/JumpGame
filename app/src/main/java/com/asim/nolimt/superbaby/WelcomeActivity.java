package com.asim.nolimt.superbaby;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by NO Limt on 07.11.2016.
 */
public class WelcomeActivity extends View{
    SuperBabyMainActivity superbabyActivity;
    private boolean[] isclick;
    private boolean isviewrunning = true;
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


    public WelcomeActivity(SuperBabyMainActivity context)
    {
        super(context);
        this.superbabyActivity=context;
        isclick = new boolean[2];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawButton(canvas);
    }

    private void DrawButton(Canvas canvas) {
      /*  Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#a0f60b"));
        paint.setAlpha(60);
        if(isclick[0]) {
            canvas.drawRect(X - 60 * superbabyActivity.height_mul, Y - 40 * superbabyActivity.height_mul,
                    X + 40 * superbabyActivity.height_mul, Y + 20 * superbabyActivity.height_mul, paint);

        }
        if(isclick[1]){
            canvas.drawRect(X - 60 * superbabyActivity.height_mul, Y + 240 * superbabyActivity.height_mul,
                    X + 30 * superbabyActivity.height_mul, Y + 300 * superbabyActivity.height_mul, paint);

        }*/

        int X = (int) (superbabyActivity.screen_width/2);
        int Y = (int) (superbabyActivity.screen_height/3);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setTextSize(50* superbabyActivity.height_mul);
        paint2.setColor(Color.parseColor("#173403"));
        canvas.drawText("Start", X - 40* superbabyActivity.height_mul, Y, paint2);
        canvas.drawText("Exit", X - 40* superbabyActivity.height_mul, Y+120* superbabyActivity.height_mul, paint2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_DOWN)
        {
            int X = (int) (superbabyActivity.screen_width/2);
            int Y = (int) (superbabyActivity.screen_height/3);
            int touch_x = (int) event.getX();
            int touch_Y= (int) event.getY();


            if(touch_x > X - 60* superbabyActivity.height_mul && touch_x < X +
                    60 * superbabyActivity.height_mul&& touch_Y > Y -
                    40 * superbabyActivity.height_mul&& touch_Y < Y +
                    20* superbabyActivity.height_mul)
                isclick[0] = true;
            else if(touch_x > X - 60 * superbabyActivity.height_mul&& touch_x < X +
                    60* superbabyActivity.height_mul && touch_Y > Y +
                    240* superbabyActivity.height_mul && touch_Y < Y +
                    300* superbabyActivity.height_mul)
                isclick[1] = true;




        }

        if (event.getAction() ==MotionEvent.ACTION_UP)
        {
            if(isclick[0]){
                startExitAnim();
                isviewrunning = false;
                superbabyActivity.handler.sendEmptyMessage(superbabyActivity.GAME_START);
            }
            if(isclick[1]){
                startExitAnim();
                isviewrunning = false;
                superbabyActivity.handler.sendEmptyMessage(superbabyActivity.EXIT);
            }
        }
        return true;





    }
}
