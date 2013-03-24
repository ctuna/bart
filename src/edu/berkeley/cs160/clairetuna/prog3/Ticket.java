package edu.berkeley.cs160.clairetuna.prog3;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class Ticket extends View {
	MainActivity master;
    private static final int BACKGROUND = Color.WHITE;

    private Bitmap  vBitmap;
    private Canvas  vCanvas;
    private Path    vPath;
    private Paint  vPaint;
    
    ImageView pinA;
    ImageView pinB;
    
    
    float oldX;
    float newX;
    float oldY;
    float newY;
    int strokeWidth;
    float startX;
    float startY;
    Context c;
    Bitmap bitmapBartMap;
    Bitmap originalBartMap;
    int width;
    int height;
    
    public void setStrokeWidth(int newWidth){
    	this.strokeWidth=newWidth;
    	vPaint.setStrokeWidth(strokeWidth);
    	
    }
    Paint erasePaint;
    
    public Ticket(Context context, AttributeSet attrs) {
        super(context, attrs);
        
		vPath= new Path();
    }
    
    public Ticket(Context c) {
    	
        super(c);
        this.c=c;
		//vPath= new Path();
    }
    
		
		
	
	

    public void initializePaint(){
    	strokeWidth=30;
        vPaint= new Paint();
		vPaint.setStrokeWidth(strokeWidth);
        vPaint.setColor(Color.BLACK);
		vPaint.setStyle(Paint.Style.STROKE);
		vPaint.setStrokeWidth(strokeWidth);
		vPaint.setStrokeCap(Paint.Cap.ROUND);
		vPaint.setAntiAlias(true);
		erasePaint = new Paint();
		erasePaint.setStrokeWidth(500);
		erasePaint.setColor(Color.WHITE);
    }
    
    public void setColor(int color){
    	vPaint.setColor(color);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
       
      //  (Resources res, int id, BitmapFactory.Options opts) and specify inMutable
        vBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        vCanvas = new Canvas(vBitmap);
        width=w;
        height = h;
        Log.i("MyApplication", "on size changed called");
        initializePaint();
        //23 total


    }

    //

    @Override
    protected void onDraw(Canvas canvas) {
    	 Log.i("MyApplication", "on draw called");
    	canvas.drawColor(getResources().getColor(R.color.StationColor));
        canvas.drawBitmap(vBitmap, 0, 0, null);
        int circle1Start = 60;
        int radius = 20;
        int circle2Start = width - radius - circle1Start;
        int topMargin = height/4;
        Paint p = new Paint();
        p.setStrokeWidth(30);
        p.setColor(getResources().getColor(R.color.LineColor));
        canvas.drawLine(circle1Start+radius/2, topMargin, circle2Start-radius/2, topMargin, p);
        canvas.drawCircle(circle1Start, topMargin, 20, new Paint());
        canvas.drawCircle(circle2Start, topMargin, 20,  new Paint());
    }
    
    public void drawStation(Polygon poly, boolean drawing){
    	int color;
    	if (drawing){
    		color = getResources().getColor(R.color.StationColor);
    	}
    	else {
    		color = Color.BLACK;
    	}
    	float[] xCoords2 = poly.getXCoords();
        float[] yCoords2 = poly.getYCoords();
    	Paint paint = new Paint();
    	paint.setColor(color);
    	paint.setStyle(Paint.Style.FILL);
    	paint.setStrokeWidth(20);
    	vPath= new Path();
    	vPath.moveTo(xCoords2[0], yCoords2[0]);
    	for (int i = 1; i < 4; i++){
    		vPath.lineTo(xCoords2[i], yCoords2[i]);
    	}
    	vCanvas.drawPath(vPath, paint);
        
        
    }
    

   
    


    public void drawRectangle(float startX, float startY, float newX, float newY){
    	
		float dX=newX-startX;
		float dY=newY-startY;
		
		vCanvas.drawRect(startX, startY, startX+dX, startY + dY, vPaint);
	}
    
    
    public void drawCircle(float x, float y, float radius){
    	if (vCanvas==null){
    		Log.i("MyApplication", "vCanvas is nully");
    	}
		
		vCanvas.drawCircle(x, y, radius, new Paint());
		invalidate();
	}
}
