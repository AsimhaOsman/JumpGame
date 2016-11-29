package com.asim.nolimt.superbaby;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

public class SuperBabyMainActivity extends AppCompatActivity {

    public static final int GAME_OVER  = 0;
    public static final int GAME_START = 1;
    public static final int EXIT       = 4;
    public static final int WELCOME    = 5;
    private GameView gameView;
    private ExitView exitView;
    private FailView failView;
    public static float screen_width;
    public static float screen_height;
    public static float width_mul;
    public static float height_mul;

    private SensorManager sensorManager;
    private MySensorEventListener sensorEventListener;
    int pre_speed = 0;


    public static boolean isGame_Running = false;
    View current_view;
    WelcomeActivity Welcome;

    public Handler handler = new Handler(){
        public void handleMessage(Message msg) {

            if(msg.what == GAME_OVER){
                current_view = null;
                initFailView();
            }
            if(msg.what == GAME_START){
                isGame_Running = true;
                Welcome = null;
                initGameView();
            }

            if(msg.what == EXIT){
                current_view = null;
                initExitView();
            }
            if(msg.what == WELCOME){
                isGame_Running = false;
                current_view = null;
                initWelcomeView();
            }


        }



    };


    private void initFailView() {
        failView = new FailView(this);
        current_view = failView;
        setContentView(failView);
        gameView = null;
    }



    private void initGameView() {
        gameView = new GameView(this);
        current_view = gameView;
        setContentView(gameView);
    }

    private void initExitView() {
        exitView = new ExitView(this);
        current_view = exitView;
        setContentView(exitView);
        Welcome = null;
    }


    private void initWelcomeView(){
        Welcome = new WelcomeActivity(this);
        current_view = Welcome;
        setContentView(Welcome);
        failView = null;
        exitView = null;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        DisplayMetrics dm;
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Welcome= new WelcomeActivity(this);

        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
        width_mul = screen_width/320;
        height_mul = screen_height/480;
        if(screen_height >= 800)
            height_mul = (float) 1.5;
        setContentView(Welcome);
        current_view = Welcome;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(current_view == gameView){
            if( (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) || (keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)){
                GameView.ispause = true;
                return true;
            }
        }
        else if(current_view == Welcome){
            if( (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) || (keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)){
                handler.sendEmptyMessage(EXIT);
                return true;
            }
        }
        else{
            if( (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) || (keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorEventListener = new MySensorEventListener();
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }


    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        //	Debug.stopMethodTracing();
        super.onPause();
    }


    private final class MySensorEventListener implements SensorEventListener {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(current_view == gameView){
                float X = event.values[SensorManager.DATA_X];
                pre_speed += X;
                if(X > 0.45 || X < -0.45){
                    int temp = X > 0 ? 4 : -4;
                    if(pre_speed > 7 || pre_speed < -7)
                        pre_speed = pre_speed > 0 ? 7 : -7;
                    gameView.logicManager.SetAndroid_HSpeed(pre_speed + temp);
                }
            }
        }

    }


}
