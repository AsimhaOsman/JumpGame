package com.asim.nolimt.superbaby;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class OBjectsManager {

	
	public static  long sum;
	public int bar_level = 0;
	public int barApperrate = 35;
	public int monsterApperrate = 80;
	public int itemAppearate = 75;
	private int monstersappear = 0;
	public int itemappear = 0;
	private Map<String, AbstractBar> barMap;
	private long bar_identifier = 0;
	private long monster_identifier = 0;
	private long repeate_long = 100;
	//private String repeate_String; 
    public boolean isrepeated = false;
	public int touch_bar_type = AbstractBar.TYPE_NORMAL;
	public static boolean[] person;
	public static int choose;
	private SuperBabyMainActivity  context;
	SuperBabyMainActivity superbabyActivity;
	private Paint paint;
	SharedPreferences get_Diff_Setting;


	public OBjectsManager(SuperBabyMainActivity context){
		//repeate_String = new String("100");
		this.context = context;
		barMap = new HashMap<String, AbstractBar>();

		initBarMap();
		initPaint();
		person = new boolean[2];
     	person[0] = false;
		person[1] = false;
		sum = 0;
		//SetDiff(context);
	}
	
	
	private void initPaint() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setTextSize(20* superbabyActivity.height_mul);
	}

	public void Clear(){
		barMap.clear();
	}
	

	public void initBarMap(){
		int count = 0;
		int CoorX;
		while(count < SuperBabyMainActivity.screen_height / (20 * SuperBabyMainActivity.height_mul)){
			if(hasBar()){
				int range = (int) (SuperBabyMainActivity.screen_width - 50 * SuperBabyMainActivity.width_mul);
				CoorX = new Random().nextInt(range);
				AbstractBar bar = new NormalBar(CoorX, count * (20 * SuperBabyMainActivity.height_mul), GetRandomItem(),context);
				barMap.put(""+bar_identifier, bar);
				bar_identifier ++;
			}
			count ++;
		}
	}

	private boolean hasBar() {
		int temp = new Random().nextInt(100);
		if(temp > barApperrate)
			return true;
		return false;
	}

	private boolean hasMonster() {
		int temp = new Random().nextInt(100);
		if(temp > monsterApperrate)
			return true;
		return false;
	}

	public void DrawBarsAndMonsters(Canvas canvas){
		person[0] = true;
		choose = 1;
		while(person[1]&&choose==1);

		for(String key : barMap.keySet()){
			barMap.get(key).drawSelf(canvas);
		}

		drawHeight(canvas);
		RemoveOuterBarsAndMonsters();

		person[0] = false;
	}

	private void drawHeight(Canvas canvas) {
		canvas.drawText(""+sum, 5 * SuperBabyMainActivity.width_mul, 20 * SuperBabyMainActivity.height_mul, paint);
	}

	public boolean isTouchBars(float CoorX, float CoorY){
		
		for(String key : barMap.keySet()){
		//	tempX = barMap.get(key).TLCoorX;
		//	tempY = barMap.get(key).TLCoorY;
			if(barMap.get(key).IsBeingStep(CoorX, CoorY)/*CoorX >= tempX - 35 && CoorX <= tempX + 45 && CoorY + 45 <= tempY && tempY - CoorY <= 45*/){
				if(repeate_long == Long.parseLong(key))
					isrepeated = true;
				else{
					isrepeated = false;
					repeate_long = Long.parseLong(key);
					touch_bar_type = barMap.get(key).type;
				}
				return true;
			}
		}
		

		
		return false;
	}

	public void MoveBarsAndMonstersDown(float vertical_speed, float add){
		person[1] = true;
		choose = 0;
		while(person[0]&&choose==0);

		for(String key : barMap.keySet()){
			barMap.get(key).TLCoorY -= vertical_speed;
			barMap.get(key).TLCoorY += add;
		}

		sum += add - vertical_speed;
		monstersappear = itemappear += add - vertical_speed;
		bar_level += add - vertical_speed;
		AddNewBarsAndMonsters();

		person[1] = false;
	}
	
	private void RemoveOuterBarsAndMonsters() {
		List<String> temp = new ArrayList<String>();
		
		for(String key : barMap.keySet()){
			if(barMap.get(key).TLCoorY > SuperBabyMainActivity.screen_height + 20){
				barMap.get(key).clear();
				temp.add(key);
			}
		}
		for(String key : temp){
			barMap.remove(key);
		}
		temp.clear();

	}
	
	public void AddNewBarsAndMonsters(){
		
		CheckLevel();
		
		AbstractBar bar;
		float tempY = 100;
		
		tempY = GetTopCoorY();
		if(tempY > (20 * SuperBabyMainActivity.height_mul)){
			/*if(hasBar()){
				int temp = new Random().nextInt(100);
				int CoorX = new Random().nextInt((int) (SuperBabyMainActivity.screen_width - 50));
				if(temp <= 5){
					bar = new SpringBar(CoorX, (int) (-15 * SuperBabyMainActivity.height_mul), context);
				}
				else if(temp <= 15){
					bar = new ShiftBar(CoorX, 0, GetRandomItem(), context);
				}
				else if(temp <= 25){
					bar = new ThronBar(CoorX, (int) (-10 * SuperBabyMainActivity.height_mul), context);
				}
				else if(temp <= 50){
					bar = new InstantShiftBar(CoorX, (int) (-10 * SuperBabyMainActivity.height_mul), context);
				}
				else{
					bar = new NormalBar(CoorX, 0, GetRandomItem(), context);
				}
				barMap.put(""+bar_identifier, bar);
				bar_identifier ++;
				
			}
			else if(hasMonster() && monstersappear >= 300 * SuperBabyMainActivity.height_mul){
				monstersappear = 0;
				int temp2 = new Random().nextInt(100);
				if(temp2 < 40)
					monsterMap.put(""+monster_identifier, new EatingHead(-10 * SuperBabyMainActivity.height_mul, context));
				else if(temp2 < 80)
					monsterMap.put(""+monster_identifier, new RotateMonster(-10 * SuperBabyMainActivity.height_mul, context));
				else {
					monsterMap.put(""+monster_identifier, new BlackStrone(-10 * SuperBabyMainActivity.height_mul, context));
					
				}
				monster_identifier ++;
			*/
		}
		
	}
	
	private void CheckLevel() {
		if(bar_level >= 2000){
			if(barApperrate < 55)
				barApperrate += 2;
			bar_level = 0;
		}
	}

	private AbstractItem GetRandomItem() {
		if(itemappear >= 400 * SuperBabyMainActivity.height_mul){
			itemappear = 0;
			int temp = new Random(bar_identifier).nextInt(100);
			/*if(temp > monsterApperrate){
				temp = new Random().nextInt(100);
				if(temp < 10)
					return new ItemUpgradeBullet(context);
				else if(temp < 30)
					return new ItemBullet(context);
				else if(temp < 80)
					return new ItemFruit(context);
				else if(temp < 90)
					return new ItemLife(context);
				else
					return new ItemShit(context);*/
			}

		return null;
	}

	public void CheckisTouchItems(AbstractAndroid android){
		float temp_x = android.LTCoorX + 22 * SuperBabyMainActivity.height_mul;
		float temp_y = android.LTCoorY + 22 * SuperBabyMainActivity.height_mul;
		for(String key : barMap.keySet()){
			if(barMap.get(key).isitemeaten == false && barMap.get(key).item != null){
				int X = (int) (barMap.get(key).TLCoorX + 25 * SuperBabyMainActivity.height_mul);
				float Y = barMap.get(key).TLCoorY - 10 * SuperBabyMainActivity.height_mul;
				if(GetLength(temp_x, temp_y, X, Y) < 30 * SuperBabyMainActivity.height_mul){
					barMap.get(key).item.ModifyAndroid(android);
					barMap.get(key).isitemeaten = true;
				}
			}
			
		}
	}

	private int GetLength(float temp_x, float temp_y, int x, float y) {
		double x_length = temp_x - x;
		double y_length = temp_y - y;
		return (int) Math.sqrt(x_length*x_length + y_length*y_length);
	}

	private float GetTopCoorY() {
		float tempY = 100;
		for(String key : barMap.keySet()){
			if(barMap.get(key).item == null){
				if(barMap.get(key).TLCoorY <= tempY)
					tempY = barMap.get(key).TLCoorY;
			}
			else if(barMap.get(key).TLCoorY - 20 * SuperBabyMainActivity.height_mul <= tempY){
				tempY = barMap.get(key).TLCoorY - 20 * SuperBabyMainActivity.height_mul;
			}
		}

		return tempY;
	}
	

}
