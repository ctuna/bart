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
	private Document xmlDocument;
	Calendar rightNow = Calendar.getInstance();
	LocationManager manager;
	public TripCostTask task;
	public StationsTask stationsTask;
	private HashMap<String, Double[]> stationCoordinates;
	LocationActivity locationActivity;
	Location userLocation; 
	MainView drawView;

	
	
	@Override
	//Try BitmapFactory.decodeFile() and then setImageBitmap() on the ImageView.
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main); 
		drawView = new MainView(this);
		FrameLayout fLayout = (FrameLayout)findViewById(R.id.mapholder);
		fLayout.addView(drawView);
		
	

		
		
		super.onCreate(savedInstanceState);

	  
        
		Log.i("MyApplication", "Starting application");
//	    mImageMap = (ImageMap)findViewById(R.id.map);
	    /*locationActivity = new LocationActivity(this);
	    

	    

		
		

		
	    updateLocation();
	   getStationInfo();*/
	}

	FrameLayout myLayout;
	FrameLayout.LayoutParams params;
	int width = 200, height =200, x = 10, y = 20, marginLeft = 200, marginRight =0, marginTop = 100, marginBottom = 0;
	

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
	
	
	
	public class MainView extends FrameLayout {

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
        
    	public void instantiateLayout(){
    		FrameLayout.LayoutParams pinParams =  new FrameLayout.LayoutParams(width, height);
    		/**
    		pinA = new ImageView(c);
    		Bitmap bitmapPinA = BitmapFactory.decodeResource(getResources(), R.drawable.pina);
    		pinA.setImageBitmap(bitmapPinA);
    		
    		//params.setMargins(left, top, right, bottom)
    		params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
    		addView(pinA, pinParams);*/
    		

    		ImageView testImage = new ImageView(c);
            BitmapFactory.Options opts = new BitmapFactory.Options();
    		int mapId = getResources().getIdentifier("bartthick", "drawable", "edu.berkeley.cs160.clairetuna.prog3");
    		Bitmap bitmapBartMap2  = BitmapFactory.decodeResource(getResources(), mapId, opts);

    		testImage.setImageBitmap(bitmapBartMap2);   		
    		addView(testImage, params);
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
            int mapId= getResources().getIdentifier("bartthick", "drawable", "edu.berkeley.cs160.clairetuna.prog3");
            Log.i("MyApplication", "MAP ID IS: " + mapId);
          //  (Resources res, int id, BitmapFactory.Options opts) and specify inMutable
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inMutable=true;
            bitmapBartMap  = BitmapFactory.decodeResource(getResources(), mapId, opts);
            //originalBartMap = BitmapFactory.decodeResource(getResources(), R.drawable.bartthick);
            //bitmapBartMap  = BitmapFactory.decodeResource(getResources(), R.drawable.bartthick);
            vCanvas = new Canvas(bitmapBartMap);
            initializePaint();
            mode="scribble";
            startX=0;
            startY=0;
            instantiateLayout();
        }

        
        public void setMode(String newMode){
        	mode=newMode;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(bitmapBartMap, 0, 0, null);
            
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
        	/**if (mode.equals("scribble")){
        		vPaint.setStyle(Paint.Style.STROKE);
        	}
            newX = event.getX();
            newY = event.getY();
            float dX;
            float dY;
            dX=newX-startX;
    		dY=newY-startY;
            if (mode.equals("circleStroke") || mode.equals("rectangleStroke")){
            	vPaint.setStyle(Paint.Style.STROKE);
            }

            if (event.getAction()== MotionEvent.ACTION_DOWN){
            	newShape = true;
            	startX = newX;
        		startY=newY;
        		vPaint.setStrokeCap(Paint.Cap.ROUND);
        		vPath= new Path();
                vPath.moveTo(newX, newY);
                
            	if (mode.equals("scribble")){
                vPaint.setStyle(Paint.Style.STROKE);
                vCanvas.drawPath(vPath, vPaint);
            	}
                
                }
           
            else if (event.getAction()== MotionEvent.ACTION_UP){
            	
            	if (mode.equals("rectangleFill")){
            		vPaint.setStyle(Paint.Style.FILL);}
            	if (mode.equals("rectangleFill")|| mode.equals("rectangleStroke")){
            		undo();
            		drawRectangle(startX, startY, newX, newY);
            		
            		vPaint.setStrokeCap(Paint.Cap.SQUARE);
            		
            		vPath.reset();
                    vPath.moveTo(startX, startY);
            		//--->
            		vPath.lineTo(startX+dX, startY);
            		//down
            		vPath.lineTo(startX+dX, startY+dY);
            		//<------
            		vPath.lineTo(startX, startY+dY);
            		//^
            		vPath.lineTo(startX, startY);
            	}    
            	

            	if (mode.equals("circleFill")){
            		vPaint.setStyle(Paint.Style.FILL);}
            	
            	if (mode.equals("circleFill")|| mode.equals("circleStroke")){
            		undo();     			   
            		float centerX = (startX+newX)/2;
            		float centerY = (startY+newY)/2;
            		vPaint.setStrokeCap(Paint.Cap.SQUARE);
            		
            		double diameter = Math.sqrt((Math.pow(dX, 2) + Math.pow(dY, 2)));
            		float radius = (float) diameter/2;
            		drawCircle(centerX, centerY, radius);
            		vPath.reset();
            		vPath.addCircle(centerX, centerY, radius, Path.Direction.CCW);
            		
            	}  
            	else if (mode.equals("line")){
            		           		
      				undo();
            		vPath= new Path();
            		
            		vPath.moveTo(startX, startY);
            		vPath.lineTo(newX, newY);
            		
      		   		vCanvas.drawLine(startX, startY, newX, newY, vPaint);
        	}
            	else if (mode.equals("scribble")){
            	
            	
            	}
            	paths.add(vPath);
            	paints.add(new Paint(vPaint));
            	vPath = new Path();
            	
            }
            else {
         	   //DRAGGING
         	   
         	   if (newX!=startX && newY!=startY){
         		   if (mode.equals("rectangleFill")|| mode.equals("rectangleStroke")){
         			   //undo() versions of rectangle;
         			   if (!newShape){
         				   undo();
         			   }
         			   newShape=false;
         		   		
         		   
         			   drawRectangle(startX, startY, newX, newY);

         			   dX=newX-startX;
         			   dY=newY-startY;
            		
         			   vPath= new Path();
            		//make a path of current rectangle
          		   		vPath.moveTo(startX, startY);
            		//--->
          		   		vPath.lineTo(startX+dX, startY);
            		//down
          		   		vPath.lineTo(startX+dX, startY+dY);
            		//<------
          		   		vPath.lineTo(startX, startY+dY);
            		//^
          		   		vPath.lineTo(startX, startY);
            		//add to paths
          		   		paths.add(vPath);
          		   		paints.add(new Paint(vPaint));
            		
            	
            		}
         		   else if (mode.equals("circleFill")){
                		vPaint.setStyle(Paint.Style.FILL);}
                	if (mode.equals("circleFill")|| mode.equals("circleStroke")){
                		if (!newShape){
          				   undo();
          			   }
          			    newShape=false;
                		float centerX = (startX+newX)/2;
                		float centerY = (startY+newY)/2;
                		vPaint.setStrokeCap(Paint.Cap.SQUARE);
                		vPath= new Path();
                		double diameter = Math.sqrt((Math.pow(dX, 2) + Math.pow(dY, 2)));
                		float radius = (float) diameter/2;
                		vPath.addCircle(centerX, centerY, radius, Path.Direction.CCW);
                		drawCircle(centerX, centerY, radius);
                		paths.add(vPath);
          		   		paints.add(new Paint(vPaint));
                		
                	} 
                	else if (mode.equals("line")){
                		
                    		if (!newShape){
              				   undo();
              			   }
              			    newShape=false;
                    		vPath= new Path();
                    		vPath.moveTo(startX, startY);
                    		vPath.lineTo(newX, newY);
                    		paths.add(vPath);
              		   		paints.add(new Paint(vPaint));
              		   		vCanvas.drawLine(startX, startY, newX, newY, vPaint);
                	}
                	
                else if (mode.equals("scribble")){
             	vPath.quadTo(oldX, oldY, (oldX+newX)/2, (oldY+newY)/2);
             	vCanvas.drawPath(vPath, vPaint);
             	
             	}
             	
         	   }
             	invalidate();
             	
             }
         
            
            
            oldX=newX;
            oldY=newY;
            */
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

	


	
