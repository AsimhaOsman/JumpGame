package com.asim.nolimt.superbaby;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;


public class NormalBar extends AbstractBar {
	
	private Bitmap normal_bar;
	
	public NormalBar(int CoorX, float CoorY, AbstractItem item, SuperBabyMainActivity context){
		this.TLCoorX = CoorX;
		this.TLCoorY = CoorY;
		this.type = TYPE_NORMAL;
		this.item = item;
		this.isitemeaten = false;
		normal_bar = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bar)).getBitmap();
	}

	@Override
	public void drawSelf(Canvas canvas) {
		if(this.item != null && !this.isitemeaten)
			this.item.DrawSelf(canvas, TLCoorX + 15 * SuperBabyMainActivity.height_mul, TLCoorY - 20
					 * SuperBabyMainActivity.height_mul);
		canvas.drawBitmap(normal_bar, TLCoorX, TLCoorY, null);
	}

	@Override
	public boolean IsBeingStep(float CoorX, float CoorY) {
		if(CoorX >= TLCoorX - 35 * SuperBabyMainActivity.width_mul &&
				CoorX <= TLCoorX + 45 * SuperBabyMainActivity.width_mul &&
				CoorY + 45 * SuperBabyMainActivity.height_mul <= TLCoorY
				&& TLCoorY - CoorY <= 45 * SuperBabyMainActivity.height_mul){
			
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		//normal_bar.recycle();
	}

	
}
