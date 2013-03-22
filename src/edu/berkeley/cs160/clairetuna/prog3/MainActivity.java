package edu.berkeley.cs160.clairetuna.prog3;

import java.util.Calendar;
import java.util.HashMap;

import org.w3c.dom.Document;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {
	ImageMap mImageMap;
	LocationManager manager;
	public TripCostTask task;
	public StationsTask stationsTask;
	private HashMap<String, Double[]> stationCoordinates;
	LocationActivity locationActivity;
	Location userLocation; 
	MainView drawView;
	ImageView pinA;
	FrameLayout.LayoutParams pinParams;
	FrameLayout fLayout;

	int width = 200, height =200,  pinAMarginLeft = 200, marginRight =0, pinAMarginTop = 100, marginBottom = 0;
	
	@Override
	//Try BitmapFactory.decodeFile() and then setImageBitmap() on the ImageView.
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main); 
		
		
		instantiateLayout();
		
		
		
		super.onCreate(savedInstanceState);

	  
        
		Log.i("MyApplication", "Starting application");
//	    mImageMap = (ImageMap)findViewById(R.id.map);
	    /*locationActivity = new LocationActivity(this);
	    

	    

		
		

		
	    updateLocation();
	   getStationInfo();*/
	}

	FrameLayout myLayout;
	FrameLayout.LayoutParams params;
	
public void instantiateLayout(){
	drawView = new MainView(this);
	fLayout = (FrameLayout)findViewById(R.id.mapholder);
	fLayout.addView(drawView);
	pinParams = new FrameLayout.LayoutParams(width, height);
	
	pinA = new ImageView(this);
	Bitmap bitmapPinA = BitmapFactory.decodeResource(getResources(), R.drawable.pina);
	pinA.setImageBitmap(bitmapPinA);
	pinParams.setMargins(pinAMarginLeft, pinAMarginTop, marginRight, marginBottom);
	fLayout.addView(pinA, pinParams);
	int pinAMarginTop = getRelativeTop(pinA);
	int pinAMarginLeft = getRelativeLeft(pinA);
	Log.i("MyApplication", "pin A margin top is: " + pinAMarginTop);
	Log.i("MyApplication", "pin A margin left is: " + pinAMarginLeft);
}
	private int getRelativeLeft(View myView) {
	        return myView.getLeft();
	}

	private int getRelativeTop(View myView) {
	        return myView.getTop();
	}
	
	
	public void drawSomething(){
		
	}
	public void updateLocation(){
		Log.i("MyApplication", "Updated Location");
		LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Log.i("MyApplication", "GOT A LOCATION SERVICE");
		userLocation = locationActivity.checkLocation(manager);
		
    	//LocationHelper locationHelper = new LocationHelper(this);
	}
	
	public String closestStation(){
		Log.i("MyApplication", "In MainActivity/closestStation");
		
		String closestStation=null;
		updateLocation();
		//Log.i("MyApplication", "User coords: " + userLocation.getLatitude()+ "," + userLocation.getLongitude());
		Location locationB = new Location("point B");
		float minDistance = Float.POSITIVE_INFINITY;
		float currentDistance;
		Log.i("MyApplication", "positive infinity: " + minDistance);

		Log.i("MyApplication", "latitude of user is: "+ userLocation.getLatitude());
		Log.i("MyApplication", "longitude of user is: "+ userLocation.getLongitude());
		for (String key: stationCoordinates.keySet()){
			Log.i("MyApplication", key + ": latitude in stationcoords is: " + stationCoordinates.get(key)[0]);
			Log.i("MyApplication", key + ": longitude in stationcoords is: " + stationCoordinates.get(key)[1]);
			
			
			locationB.setLatitude(stationCoordinates.get(key)[0]);
			locationB.setLongitude(stationCoordinates.get(key)[1]);
			
			currentDistance = userLocation.distanceTo(locationB);
			Log.i("MyApplication", "Distance to " + key.toUpperCase() + " is: "+ currentDistance);
			if (currentDistance< minDistance){
				minDistance=currentDistance;
				closestStation = key;
			}
		}
		Log.i("MyApplication", "Closest station is: " + closestStation);
		return closestStation;
	}
	
	
	
	public boolean isOnPinA(float x, float y){
		boolean xProper = (x>=pinAMarginLeft && x<=pinAMarginLeft + width);
		boolean yProper = (y>=pinAMarginTop && y<= pinAMarginTop + height);
		return xProper && yProper;
	}
	
	public void setCoordinates(HashMap<String, Double[]> coords){
		this.stationCoordinates=coords;
		String s = closestStation();
		Log.i("MyApplication", "Hello Hello please don't crash");
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    
	public void getTripInfo(String stationOrig, String stationDest){		
		task = new TripCostTask();
		task.setMaster(this);
		task.execute(stationOrig, stationDest);
	}
	
	public void getStationInfo(){
		Log.i("MyApplication", "starting Get Station info");
		stationsTask = new StationsTask();
		stationsTask.setMaster(this);
		stationsTask.execute();
	}
	
	
	public void updateTripInfo(){
		
		String timeNow = task.getTimeNow();
		String departureTime = task.getStartTime();
		String fare = task.getFare();
		
		String difference = TimeHelper.difference(timeNow, departureTime);
		boolean isAfter = TimeHelper.isAfter(timeNow, departureTime);
		Log.i("MyApplication", "DIFFERENCE: " + difference);
		Log.i("MyApplication", "Is leave in the future?: " + isAfter);
	}
	
	
	public void movePinA(float dX, float dY){
		pinAMarginLeft +=dX;
		pinAMarginTop +=dY;
		fLayout.removeView(pinA);
		pinParams.setMargins(pinAMarginLeft, pinAMarginTop, marginRight, marginBottom);
		fLayout.addView(pinA, pinParams);
		
		
	}
	
	
	public int getPinAMarginLeft(){
		return pinAMarginLeft;
	}
	
	public int getPinAMarginTop(){
		return pinAMarginTop;
	}
	
	public class MainView extends View {

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
        int mapWidth;
        int mapHeight;
        
        
        public void setStrokeWidth(int newWidth){
        	this.strokeWidth=newWidth;
        	vPaint.setStrokeWidth(strokeWidth);
        	
        }
        Paint erasePaint;
        public MainView(Context context, AttributeSet attrs) {
            super(context, attrs);
            
			vPath= new Path();
        }
        
        public MainView(Context c) {
        	
            super(c);
            this.c=c;
			//vPath= new Path();
        }
        
    		/**
    		
    	
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
        }*/
      
        
        Bitmap bgr;       
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            Log.i("MyApplication", "ONSIZECHANGED");
            int mapId= getResources().getIdentifier("bartthick", "drawable", "edu.berkeley.cs160.clairetuna.prog3");
           
          //  (Resources res, int id, BitmapFactory.Options opts) and specify inMutable
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inMutable=true;
            bitmapBartMap  = BitmapFactory.decodeResource(getResources(), mapId, opts);
            vCanvas = new Canvas(bitmapBartMap);
            
            
            
            
            //originalBartMap = BitmapFactory.decodeResource(getResources(), R.drawable.bartthick);
            //bitmapBartMap  = BitmapFactory.decodeResource(getResources(), R.drawable.bartthick);
           // vCanvas = new Canvas(bitmapBartMap);
           /* initializePaint();
            mode="scribble";
            startX=0;
            startY=0;
            //instantiateLayout();
            Log.i("MyApplication", "end of onsizechanged");
            invalidate();*/
        }

        

        @Override
        protected void onDraw(Canvas canvas) {
        	Log.i("MyApplication", "ONDRAW CALLED");
        	Log.i("MyApplication", "BITMAP IS: " + bitmapBartMap);
            canvas.drawBitmap(bitmapBartMap, 0, 0, null);
            
        }
        
        
        
        public void setMode(String newMode){
        	mode=newMode;
        }       
        public void clearCanvas(){
        	vPath.reset();
        	
            vPath.addCircle(407, 419, getWidth(), Path.Direction.CCW);
            //paths.add(vPath);
        	//paints.add(new Paint(erasePaint));
        	vCanvas.drawPath(vPath, erasePaint);
        	//vCanvas.drawColor(BACKGROUND);
        	invalidate();
        }
       
        
       
        
        boolean newShape=false;
        
        String mode; 
        public boolean onTouchEvent(MotionEvent event) {
        	/*if (mode.equals("scribble")){
        		vPaint.setStyle(Paint.Style.STROKE);
        	}*/
            newX = event.getX();
            newY = event.getY();
            float dX;
            float dY;
            int oldMarginLeft = getPinAMarginLeft();
            int oldMarginTop = getPinAMarginTop();
            dX=newX-oldMarginLeft;
    		dY=newY-oldMarginTop;
            /*if (mode.equals("circleStroke") || mode.equals("rectangleStroke")){
            	vPaint.setStyle(Paint.Style.STROKE);
            }*/

            if (event.getAction()== MotionEvent.ACTION_DOWN){
            	if (isOnPinA(newX, newY)){
            		Log.i("MyApplication", "clicked on pin!");
            		
            	}
            	
                
                }
           
            else if (event.getAction()== MotionEvent.ACTION_UP){
            	
            }
            else {
            	movePinA(dX, dY);
         	   }
             	invalidate();
         return true;   
        }

        public void drawRectangle(float startX, float startY, float newX, float newY){
        	
    		float dX=newX-startX;
    		float dY=newY-startY;
    		
    		vCanvas.drawRect(startX, startY, startX+dX, startY + dY, vPaint);
    	}
        
        
        public void drawCircle(float x, float y, float radius){
        	
    		
    		vCanvas.drawCircle(x, y, radius, vPaint);
    	}
    }


}

	


	
