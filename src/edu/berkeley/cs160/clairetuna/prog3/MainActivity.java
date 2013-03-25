package edu.berkeley.cs160.clairetuna.prog3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {
	ImageMap mImageMap;
	LocationManager manager;
	public TripCostTask task;
	public StationsTask stationsTask;
	private HashMap<String, Double[]> stationCoordinates;
	private HashMap<String, String> stationNames;
	
	LocationActivity locationActivity;
	Location userLocation; 
	MainView drawView;
	ImageView pinA;
	ImageView pinB;
	FrameLayout.LayoutParams pinAParams;
	FrameLayout.LayoutParams pinBParams;
	FrameLayout fLayout;
	FrameLayout ticketHolder;
	int width = 300, height =200,  pinAMarginLeft = 10, marginRight =0, pinAMarginTop = 10, marginBottom = 0, pinBMarginLeft=80, pinBMarginTop=80;
	
	@Override
	//Try BitmapFactory.decodeFile() and then setImageBitmap() on the ImageView.
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main); 
		ticketHolder = (FrameLayout) findViewById(R.id.ticketholder);
		
		
		instantiateLayout();
		
		
		
		super.onCreate(savedInstanceState);

	  
        
		Log.i("MyApplication", "Starting application");
//	    
	   // updateLocation();
	   getStationInfo();
	}

	FrameLayout myLayout;
	FrameLayout.LayoutParams params;
	TextView lastTrain1;
	TextView lastTrain2;
public void instantiateLayout(){
	drawView = new MainView(this);
	fLayout = (FrameLayout)findViewById(R.id.mapholder);
	fLayout.addView(drawView);
	pinAParams = new FrameLayout.LayoutParams(width, height);
	pinBParams = new FrameLayout.LayoutParams(width, height);
	
	pinA = new ImageView(this);
	Bitmap bitmapPinA = BitmapFactory.decodeResource(getResources(), R.drawable.pina);
	pinA.setImageBitmap(bitmapPinA);
	pinAParams.setMargins(pinAMarginLeft, pinAMarginTop, marginRight, marginBottom);
	fLayout.addView(pinA, pinAParams);
	
	
	pinB = new ImageView(this);
	Bitmap bitmapPinB = BitmapFactory.decodeResource(getResources(), R.drawable.pinb);
	pinB.setImageBitmap(bitmapPinB);
	pinBParams.setMargins(pinBMarginLeft, pinBMarginTop, marginRight, marginBottom);
	fLayout.addView(pinB, pinBParams);


	ticket = new Ticket(this);
	ticketHolder.addView(ticket);

	helpButton = new Button(this);
	helpButton.setText("step by step");
	helpButton.setTextSize(15);
	helpButton.setTextColor(Color.WHITE);
	
	FrameLayout.LayoutParams buttonParams =  new FrameLayout.LayoutParams(150, 50);
	buttonParams.setMargins(525, 150, 0, 0);
	helpButton.setOnClickListener(helpButtonListener);
	ticketHolder.addView(helpButton, buttonParams);
	
}
Ticket ticket;
View.OnClickListener helpButtonListener = new View.OnClickListener(){
	public void onClick(View v){
		Intent intent = new Intent(MainActivity.this, DisplayGuidance.class);
		String[] goodies = new String[8];
		goodies[0] = String.valueOf(task.hasConnection());
		goodies[1] = stationNames.get(task.getStationOrig());
		goodies[2] = stationNames.get(task.getStationDest());
		goodies[3] = stationNames.get(task.getTransferStation());
		goodies[4] = task.getFare();
		goodies[5] = task.getTrain1();
		goodies[6] = task.getTrain2();
		goodies[7] = difference;
		intent.putExtra("GOODIES", goodies);
	}
};

	public 
	void updateTicket(){
		ticket.drawCircle(10,10, 10);
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
			//Log.i("MyApplication", "Distance to " + key.toUpperCase() + " is: "+ currentDistance);
			if (currentDistance< minDistance){
				minDistance=currentDistance;
				closestStation = key;
			}
		}
		Log.i("MyApplication", "Closest station is: " + closestStation);
		return closestStation;
	}
	
	
	
	public boolean isOnPinA(float x, float y){
		boolean xProper = (x>=pinAMarginLeft+(width/4) && x<=pinAMarginLeft + 3*width/4);
		boolean yProper = (y>=pinAMarginTop && y<= pinAMarginTop + height*.75);
		return xProper && yProper;
	}
	
	public boolean isOnPinB(float x, float y){
		boolean xProper = (x>=pinBMarginLeft+(width/4) && x<=pinBMarginLeft + 3*width/4);
		boolean yProper = (y>=pinBMarginTop && y<= pinBMarginTop +  height*.75);
		return xProper && yProper;
	}
	
	public void setCoordinates(HashMap<String, Double[]> coords){
		this.stationCoordinates=coords;
	}
	
	public void setStationNames(HashMap<String, String> names){
		this.stationNames=names;
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
		stationsTask = new StationsTask();
		stationsTask.setMaster(this);
		stationsTask.execute();
	}
	
	TextView lastStart;
	TextView lastTransfer;
	TextView lastEnd;
	TextView lastStartTime;
	TextView lastEndTime;
	TextView oneWayTitle;
	TextView oneWayValue;
	TextView roundTripTitle;
	TextView roundTripValue;
	TextView lastOneWay;
	TextView lastRoundTrip;
	TextView lastStartTimeDifference;
	TextView lastEndTimeDifference;
	Button helpButton;
	String difference;
	public void updateTripInfo(){
		String timeNow = task.getTimeNow();
		String departureTime = task.getStartTime();
		String arrivalTime = task.getArrivalTime();
		String fare = task.getFare();
		difference = TimeHelper.difference(timeNow, departureTime);
		String train1 = task.getTrain1();
		
		boolean hasConnection = task.hasConnection();
		ticket.hasTransfer=hasConnection;
		Log.i("MyApplication", "in the task, connection is: " + hasConnection);
		int grey = getResources().getColor(R.color.TitleText);
		int white = getResources().getColor(R.color.ValueText);
		
		ticket.invalidate();

		int stationTextSize= 15;
		int timeTextSize = 15;
		int fareTextSize=20;
		int fontSize = stationTextSize;
		int topMargin = 80;
		
		if (lastStart !=null){
			Log.i("plazadrama", "it was not null");
			ticketHolder.removeView(lastStart);
		}
		if (lastTrain1 != null){
			ticketHolder.removeView(lastTrain1);
		}
		if (lastStartTime !=null){
			ticketHolder.removeView(lastStartTime);
		}
		if (lastEndTime !=null){
			ticketHolder.removeView(lastEndTime);
		}
		if (lastOneWay!=null){
			ticketHolder.removeView(lastOneWay);
		}
	
		if (lastRoundTrip!=null){
			ticketHolder.removeView(lastRoundTrip);
		}
		if (lastStartTimeDifference!=null){
			ticketHolder.removeView(lastStartTimeDifference);
		}
		
		if (lastEndTimeDifference!=null){
			ticketHolder.removeView(lastEndTimeDifference);
		}

		
		TextView startStation = new TextView(this);
		ViewGroup.MarginLayoutParams source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		source.setMargins(40, topMargin, 0, 0);
		String startStationString= stationNames.get(task.getStationOrig());
		startStation.setText(startStationString.toUpperCase());
		startStation.setTextColor(white);
		startStation.setTextSize(stationTextSize);
		LayoutParams p = new LayoutParams(source);
		startStation.setLayoutParams(p);
		ticketHolder.addView(startStation, p);
		lastStart=startStation;
		
		TextView startTime = new TextView(this);
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		source.setMargins(40, topMargin+30, 0, 0);
		startTime.setText(departureTime);
		startTime.setTextColor(white);
		startTime.setTextSize(timeTextSize);
		p = new LayoutParams(source);
		startTime.setLayoutParams(p);
		ticketHolder.addView(startTime, p);
		lastStartTime=startTime;
		

		TextView startTimeDifference = new TextView(this);
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		source.setMargins(40+85, topMargin+30, 0, 0);
		startTimeDifference.setText("(" + difference+ ")");
		startTimeDifference.setTextColor(grey);
		startTimeDifference.setTextSize(timeTextSize);
		p = new LayoutParams(source);
		startTimeDifference.setLayoutParams(p);
		ticketHolder.addView(startTimeDifference, p);
		lastStartTimeDifference=startTimeDifference;
		
		
		TextView oneWayTitle = new TextView(this);
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		source.setMargins(40, topMargin+80, 0, 0);
		oneWayTitle.setText("one-way:");
		oneWayTitle.setTextColor(grey);
		oneWayTitle.setTextSize(fareTextSize);
		p = new LayoutParams(source);
		oneWayTitle.setLayoutParams(p);
		ticketHolder.addView(oneWayTitle, p);
		
		
		TextView oneWayValue = new TextView(this);
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		source.setMargins(40 + 110, topMargin+80, 0, 0);
		oneWayValue.setText("$" +fare);
		oneWayValue.setTextColor(white);
		oneWayValue.setTextSize(fareTextSize);
		p = new LayoutParams(source);
		oneWayValue.setLayoutParams(p);
		ticketHolder.addView(oneWayValue, p);
		lastOneWay=oneWayValue;
		
		
		TextView roundTripTitle = new TextView(this);
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		source.setMargins(270, topMargin+80, 0, 0);
		roundTripTitle.setText("round-trip:");
		roundTripTitle.setTextColor(grey);
		roundTripTitle.setTextSize(fareTextSize);
		p = new LayoutParams(source);
		roundTripTitle.setLayoutParams(p);
		ticketHolder.addView(roundTripTitle, p);
		
		
		TextView roundTripValue = new TextView(this);
		String roundTripCostString;
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		source.setMargins(270 + 130, topMargin+80, 0, 0);
		float roundTripCost = Float.parseFloat(fare)*2;
		if (Float.toString(roundTripCost).split("\\.")[1].length()==1){
			roundTripCostString= Float.toString(roundTripCost) + "0";
		}
		else if (Float.toString(roundTripCost).split("\\.")[1] == null || Float.toString(roundTripCost).split("\\.")[1].length()==0){
			roundTripCostString= Float.toString(roundTripCost) + "00";
		}
		else{
			roundTripCostString= Float.toString(roundTripCost);
		}
		
		roundTripValue.setText("$" +roundTripCostString);
		roundTripValue.setTextColor(white);
		roundTripValue.setTextSize(fareTextSize);
		p = new LayoutParams(source);
		roundTripValue.setLayoutParams(p);
		ticketHolder.addView(roundTripValue, p);
		lastRoundTrip=roundTripValue;
		if (lastTransfer !=null){
			ticketHolder.removeView(lastTransfer);
		}
		
		train1 = stationNames.get(task.getTrain1());
		TextView train1View = new TextView(this);
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		train1View.setText(train1.toUpperCase());
		
		if (lastTrain2 != null){
			ticketHolder.removeView(lastTrain2);
		}
		if (task.hasConnection()){
			String train2 = task.getTrain2();
			
			
			String middleText = stationNames.get(task.getTransferStation());
	
			
			TextView transferStation = new TextView(this);
			source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
			transferStation.setText(middleText.toUpperCase());
			source.setMargins(390- (int)(middleText.length()*(fontSize*.7)), topMargin, 0, 0);
			transferStation.setTextColor(white);
			transferStation.setTextSize(stationTextSize);
			p = new LayoutParams(source);
			transferStation.setLayoutParams(p);
			ticketHolder.addView(transferStation, p);
			lastTransfer=transferStation;
			ticket.drawTransfer();
			
			
			
			source.setMargins(240- (int)(train1.length()*(fontSize*.7)), 20, 0, 0);
			train1View.setTextColor(grey);
			train1View.setTextSize(stationTextSize);
			p = new LayoutParams(source);
			train1View.setLayoutParams(p);
			ticketHolder.addView(train1View, p);
			lastTrain1=train1View;
			
			train2 = stationNames.get(task.getTrain2());
			TextView train2View = new TextView(this);
			source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
			train2View.setText(train2.toUpperCase());
			source.setMargins(560- (int)(train2.length()*(fontSize*.7)), 20, 0, 0);
			train2View.setTextColor(grey);

			train2View.setTextSize(stationTextSize);
			p = new LayoutParams(source);
			train2View.setLayoutParams(p);
			ticketHolder.addView(train2View, p);
			lastTrain2=train2View;
			//HAS CONNECTION: PUT TRAINS ON BOTH SIDES 
		}
		else {
			//DOES NOT HAVE CONNECTION: PUT TRAIN IN MIDDLE
			source.setMargins(400- (int)(train1.length()*(fontSize*.7)), 20, 0, 0);
			train1View.setTextColor(grey);
			train1View.setTextSize(stationTextSize);
			p = new LayoutParams(source);
			train1View.setLayoutParams(p);
			ticketHolder.addView(train1View, p);
			lastTrain1=train1View;
			
		}
		
		if (lastEnd !=null){
			ticketHolder.removeView(lastEnd);
		}
		TextView endStation = new TextView(this);
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		Log.i("MyApplication", "abbrev in update is: " + task.getStationDest());
		
		String endText = stationNames.get(task.getStationDest());
		endStation.setText(endText.toUpperCase());
		source.setMargins( 650- endText.length()*(int)(fontSize*.7), topMargin, 0, 0);
		endStation.setTextColor(white);
		endStation.setTextSize(stationTextSize);
		p = new LayoutParams(source);
		endStation.setLayoutParams(p);
		ticketHolder.addView(endStation, p);
		lastEnd=endStation;
		
		
		TextView endTime = new TextView(this);
		source= new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
		source.setMargins(650- 65, topMargin+30, 0, 0);
		endTime.setText(arrivalTime);
		endTime.setTextColor(white);
		endTime.setTextSize(timeTextSize);
		p = new LayoutParams(source);
		endTime.setLayoutParams(p);
		ticketHolder.addView(endTime, p);
		lastEndTime=endTime;
	}
	
	
	public void movePinA(float dX, float dY){
		pinAMarginLeft +=dX;
		pinAMarginTop +=dY;
		fLayout.removeView(pinA);
		pinAParams.setMargins(pinAMarginLeft, pinAMarginTop, marginRight, marginBottom);
		fLayout.addView(pinA, pinAParams);

	}
	
	
	public void movePinB(float dX, float dY){
		pinBMarginLeft +=dX;
		pinBMarginTop +=dY;
		fLayout.removeView(pinB);
		pinBParams.setMargins(pinBMarginLeft, pinBMarginTop, marginRight, marginBottom);
		fLayout.addView(pinB, pinBParams);

	}
	
	public void movePinATo(float dX, float dY){
		
		Log.i("MyApplication", "called move pin A ");
		pinAMarginLeft = (int) dX;
		pinAMarginTop =(int)dY;
		fLayout.removeView(pinA);
		pinAParams.setMargins(pinAMarginLeft, pinAMarginTop, marginRight, marginBottom);
		fLayout.addView(pinA, pinAParams);
		
		
	}
	
	public void movePinBTo(float dX, float dY){
		pinBMarginLeft = (int) dX;
		pinBMarginTop =(int)dY;
		fLayout.removeView(pinB);
		pinBParams.setMargins(pinBMarginLeft, pinBMarginTop, marginRight, marginBottom);
		fLayout.addView(pinB, pinBParams);
		
		
	}
	
	public int getPinAMarginLeft(){
		return pinAMarginLeft;
	}
	
	public int getPinAMarginTop(){
		return pinAMarginTop;
	}
	
	public int getPinBMarginLeft(){
		return pinBMarginLeft;
	}
	
	public int getPinBMarginTop(){
		return pinBMarginTop;
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
			
        }
        

        Polygon mapPath;
        boolean aSelected;
        boolean bSelected;
        Polygon p1;
        Polygon p2;
        Polygon nullPolygon;
        Bitmap bgr;       
        String destinationFullName = "Castro Valley";
        String Xprogram = "int[] xlala = {";
        String Yprogram = "int[] ylala = {";
       int coordCount = 0;
        float pinAdXGrab;
        float pinAdYGrab;
        float pinBdXGrab;
        float pinBdYGrab;
        boolean newShape=false;
        ArrayList<Polygon> pinADamaged = new ArrayList<Polygon>();
        ArrayList<Polygon> pinBDamaged = new ArrayList<Polygon>();
        ArrayList<Polygon> stations = new ArrayList<Polygon>();
        String mode; 
        Polygon lastPinALocation;
        Polygon lastPinBLocation;
        float[] lastPinACoords = {0, 0};
        float[] lastPinBCoords = {0, 0};
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            Log.i("MyApplication", "ONSIZECHANGED");
            int mapId= getResources().getIdentifier("bartthick", "drawable", "edu.berkeley.cs160.clairetuna.prog3");
           
          //  (Resources res, int id, BitmapFactory.Options opts) and specify inMutable
            BitmapFactory.Options opts = new BitmapFactory.Options();
            //opts.inMutable=true;
            //EMULATOR
            bitmapBartMap  = BitmapFactory.decodeResource(getResources(), mapId).copy(Bitmap.Config.ARGB_8888, true);
            vCanvas = new Canvas(bitmapBartMap);
            instantiatePolygons();
            //23 total

    
        }

        //

        public void instantiatePolygons(){
            float[] xCoords = {262,355,435,494,612,684,551,442,417,475,715,719,568,632,554,322,150,226,145,13,249,309,340, 262};
            float[] yCoords = {81,29,211,197,83,154,288,309,423,501,495,601,611,698,764,493,690,777,841,690,410,400,257, 81};
            mapPath = new Polygon(xCoords, yCoords, 23);
            float[] xCoords2 = {276,369,378,284};
            float[] yCoords2 = {108,62,83,129};
            Polygon delNorte = new Polygon(xCoords2, yCoords2, 4, "DELN", "EL CERRITO DEL NORTE");
            stations.add(delNorte);

            
           
            float[] xCoords3 = {290,384,392,299};
            float[] yCoords3 = {141,96,114,160};
            Polygon plaza = new Polygon(xCoords3, yCoords3, 4, "PLZA", "EL CERRITO PLAZA");
            stations.add(plaza);
            
            nullPolygon = new Polygon(xCoords2, yCoords2, 4, "NULL", "NULL");
            
            float[] xCoords4 = {232,367,380,245};
            float[] yCoords4 = {81,9,37,110};
            Polygon richmondPoly = new Polygon (xCoords4, yCoords4, 4, "RICH", "RICHMOND");
            stations.add(richmondPoly);
           //INSTANTIATE LAST PIN A LOCATION
            
            float[] xCoords5 = {305,400,406,313};
            float[] yCoords5 = {177,129,148,194};
            Polygon nbrkPoly = new Polygon (xCoords5, yCoords5, 4, "NBRK");
            stations.add(nbrkPoly);
            
            float[] xCoords6 = {318,413,419,326};
            float[] yCoords6 = {206,159,175,225};
            Polygon dbrkPoly = new Polygon (xCoords6, yCoords6, 4, "DBRK");
            stations.add(dbrkPoly);

            
            float[] xCoords7 = {332,426,435,338};
            float[] yCoords7 = {238,191,207,257};
            Polygon ashPoly = new Polygon (xCoords7, yCoords7, 4, "ASHB");
            stations.add(ashPoly);
            


            float[] xCoords8 = {435,453,466,445};
            float[] yCoords8 = {211,210,305,309};
            Polygon rockPoly = new Polygon (xCoords8, yCoords8, 4, "ROCK");
            stations.add(rockPoly);
            
            float[] xCoords9 = {464,485,498,478};
            float[] yCoords9 = {205,201,300,304};
            Polygon orindPoly = new Polygon (xCoords9, yCoords9, 4, "ORIN");
            stations.add(orindPoly);
            
            float[] xCoords10 = {491,507,541,521};
            float[] yCoords10 = {201,188,291,293};
            Polygon lafPoly = new Polygon (xCoords10, yCoords10, 4, "LAFY");
            stations.add(lafPoly);
            
            float[] xCoords11 = {508,523,586,572};
            float[] yCoords11 = {186,173,257,270};
            Polygon walnPoly = new Polygon (xCoords11, yCoords11, 4, "WCRK");
            stations.add(walnPoly);
            
            float[] xCoords12 = {530,545,608,594};
            float[] yCoords12 = {168,153,234,250};
            Polygon phillPoly = new Polygon (xCoords12, yCoords12, 4, "PHIL");
            stations.add(phillPoly);

            
//CONCORD
            float[] xCoords13 = {554,570,635,620};
            float[] yCoords13 = {146,132,212,225};
            Polygon conPoly = new Polygon (xCoords13, yCoords13, 4, "CONC");
            stations.add(conPoly);
//NCONCONRD            
            float[] xCoords14 = {580,594,658,644};
            float[] yCoords14 = {124,111,188,202};
            Polygon nconPoly = new Polygon (xCoords14, yCoords14, 4, "NCON");
            stations.add(nconPoly);
  //PITTS          
            float[] xCoords15 = {586,605,711,689};
            float[] yCoords15 = {85,62,174,192,194};
            Polygon pitPoly = new Polygon (xCoords15, yCoords15, 4, "PITT");
            stations.add(pitPoly);
            
            
            //*
           

            //macarthur+
            float[] xCoords16 = {333,440,436,331};
            float[] yCoords16 = {286,311,331,309};
            Polygon poly16 = new Polygon (xCoords16, yCoords16, 4, "MCAR");
            stations.add(poly16);
            
            //19th st oakland
            float[] xCoords17 = {326,431,428,322};
            float[] yCoords17 = {332,352,374,351};
            Polygon poly17 = new Polygon (xCoords17, yCoords17, 4, "19TH");
            stations.add(poly17);
            //12 st oakland
            float[] xCoords18 = {319,424,422,315};
            float[] yCoords18 = {364,387,408,383};
            Polygon poly18 = new Polygon (xCoords18, yCoords18, 4, "12TH");
            stations.add(poly18);
            //lake merritt
            float[] xCoords19 = {337,421,428,350};
            float[] yCoords19 = {499,419,440,513};
            Polygon poly19 = new Polygon (xCoords19, yCoords19, 4, "LAKE");
            stations.add(poly19);
            //fruitvale
            float[] xCoords20 = {363,442,454,376};
            float[] yCoords20 = {530,458,474,548};
            Polygon poly20 = new Polygon (xCoords20, yCoords20, 4, "FTVL");
            stations.add(poly20);
            //colliseum
            float[] xCoords21 = {396,464,480,409};
            float[] yCoords21 = {569,488,500,586};
            Polygon poly21 = new Polygon (xCoords21, yCoords21, 4, "COLS");
            stations.add(poly21);
            //san leandro
            float[] xCoords22 = {430,496,522,444};
            float[] yCoords22 = {614,500,499,632};
            Polygon poly22 = new Polygon (xCoords22, yCoords22, 4, "SANL");
            stations.add(poly22);
            //bay fair
            float[] xCoords23 = {458,539,561,472};
            float[] yCoords23 = {645,497,501,667};
            Polygon poly23 = new Polygon (xCoords23, yCoords23, 4, "BAYF");
            stations.add(poly23);
            //castro valley
            float[] xCoords24 = {590,589,611,613};
            float[] yCoords24 = {607,499,499,605};
            Polygon poly24 = new Polygon (xCoords24, yCoords24, 4, "CAST");
            stations.add(poly24);
            //w dublin pleasanton
            
            float[] xCoords25 = {647,648,670,668};
            float[] yCoords25 = {604,499,497,605};
            Polygon poly25 = new Polygon (xCoords25, yCoords25, 4, "WDUB");
            stations.add(poly25);
            

            //dublin pleasanton
            float[] xCoords26 = {698,730,732,698};
            float[] yCoords26 = {478,480,621,622};
            Polygon poly26 = new Polygon (xCoords26, yCoords26, 4, "DUBL");
            stations.add(poly26);
            
            //HAYWAYRD HAYW HAS 6B 
            float[] xCoords27 = {482,561,570,496};
            float[] yCoords27 = {680,609,622,696};
            Polygon poly27 = new Polygon (xCoords27, yCoords27, 4, "HAYW");
            stations.add(poly27);
            
            //SOUTH HAYWARD SHAY
            float[] xCoords28 = {501,577,590,516};
            float[] yCoords28 = {704,630,645,720};
            Polygon poly28 = new Polygon (xCoords28, yCoords28, 4, "SHAY");
            stations.add(poly28);
            
            //UNION CITY UCTY
            float[] xCoords29 = {519,595,607,532};
            float[] yCoords29 = {723,652,668,739};
            Polygon poly29 = new Polygon (xCoords29, yCoords29, 4, "UCTY");
            stations.add(poly29);
            //FREMONT FRMT
            
            float[] xCoords30 = {518,632,653,537};
            float[] yCoords30 = {765,661,684,788};
            Polygon poly30 = new Polygon (xCoords30, yCoords30, 4, "FRMT");
            stations.add(poly30);
            //WEST OAKLAND HAS 6
            
            float[] xCoords31 = {276,296,338,320,318,276};
            float[] yCoords31 = {411,406, 496,495,497,411};
            Polygon poly31 = new Polygon (xCoords31, yCoords31, 6, "WOAK");
            stations.add(poly31);
            
            //EMBARCADERO
            
            float[] xCoords32 = {227,241,314,299};
            float[] yCoords32 = {442,427,503,521};
            Polygon poly32 = new Polygon (xCoords32, yCoords32, 4, "EMBR");
            stations.add(poly32);
            
            //MONTGOMERY
            
            float[] xCoords33 = {205,218,292,278};
            float[] yCoords33 = {471,455,526,542};
            Polygon poly33 = new Polygon (xCoords33, yCoords33, 4, "MONT");
            stations.add(poly33);
            
            //POWELL
            
            float[] xCoords34 = {183,196,272,259};
            float[] yCoords34 = {497,481,549,567};
            Polygon poly34 = new Polygon (xCoords34, yCoords34, 4, "POWL");
            stations.add(poly34);
            
            //CIVIC CENTER
            
            float[] xCoords35 = {161,175,250,237};
            float[] yCoords35 = {522,506,575,590};
            Polygon poly35 = new Polygon (xCoords35, yCoords35, 4, "CIVC");
            stations.add(poly35);
            
            //16H ST MS
            
            float[] xCoords36 = {142,155,231,216};
            float[] yCoords36 = {545,527,598,614};
            Polygon poly36 = new Polygon (xCoords36, yCoords36, 4, "16TH");
            stations.add(poly36);
            
            //24H ST MS
            
            float[] xCoords37 = {124,137,211,197};
            float[] yCoords37 = {568,553,619,636};
            Polygon poly37 = new Polygon (xCoords37, yCoords37, 4, "24TH");
            stations.add(poly37);
            //GLEN PARK
            
            float[] xCoords38 = {105,118,193,181};
            float[] yCoords38 = {587,572,641,657};
            Polygon poly38 = new Polygon (xCoords38, yCoords38, 4, "GLEN");
            stations.add(poly38);
            
            //balboa PARK
            
            float[] xCoords39 = {84,97,174,161};
            float[] yCoords39 = {614,599,661,675};
            Polygon poly39 = new Polygon (xCoords39, yCoords39, 4, "BALB");
            stations.add(poly39);
            
            //DALY CITY
            
            
            float[] xCoords40 = {62,75,159,150};
            float[] yCoords40 = {637,623,680,689};
            Polygon poly40 = new Polygon (xCoords40, yCoords40, 4, "DALY");
            stations.add(poly40);
            //COLMA
            
            float[] xCoords41 = {19,34, 125,147};
            float[] yCoords41 = {693,672,682,700};
            Polygon poly41 = new Polygon (xCoords41, yCoords41, 4, "COLM");
            stations.add(poly41);
            //SOUTH SF
            float[] xCoords42 = {56,150,163,69};
            float[] yCoords42 = {741,702,714,756};
            Polygon poly42 = new Polygon (xCoords42, yCoords42, 4, "SSAN");
            stations.add(poly42);
            //SAN BRUNO
            float[] xCoords43 = {82,165,179,93};
            float[] yCoords43 = {768,716,731,786};
            Polygon poly43 = new Polygon (xCoords43, yCoords43, 4, "SBRN");
            stations.add(poly43);
            //SFO 
            float[] xCoords44 = {103,186,200,115};
            float[] yCoords44 = {794,736,752,811};
            Polygon poly44 = new Polygon (xCoords44, yCoords44, 4, "SFIA");
            stations.add(poly44);
            //MILLBRAE
            float[] xCoords45 = {79,246,265,125};
            float[] yCoords45 = {863,729,752,866};
            Polygon poly45 = new Polygon (xCoords45, yCoords45, 4, "MLBR");
            stations.add(poly45);
            
     
        
        }
        
        

        
        public Polygon stationForCoord(float x, float y){
        	for (Polygon station : stations){
        		if (station.contains(x, y)){
        			return station;
        		}
        	}
        	return nullPolygon;
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
        	
            canvas.drawBitmap(bitmapBartMap, 0, 0, null);

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
        
        public void setMode(String newMode){
        	mode=newMode;
        }       
        
       
        
        public boolean onTouchEvent(MotionEvent event) {
        	/*if (mode.equals("scribble")){
        		vPaint.setStyle(Paint.Style.STROKE);
        	}*/
            newX = event.getX();
            newY = event.getY();
            
           
            float pinAdX;
            float pinAdY;
            float pinBdX;
            float pinBdY;
            int oldPinAMarginLeft = getPinAMarginLeft();
            int oldPinAMarginTop = getPinAMarginTop();
            int oldPinBMarginLeft = getPinBMarginLeft();
            int oldPinBMarginTop = getPinBMarginTop();
            
            int oldPinX= oldPinAMarginLeft + (width/2);
            int oldPinY= oldPinAMarginTop + height;

            pinAdX=newX-oldPinAMarginLeft;
    		pinAdY=newY-oldPinAMarginTop;
    		
            pinBdX=newX-oldPinBMarginLeft;
    		pinBdY=newY-oldPinBMarginTop;
            /*if (mode.equals("circleStroke") || mode.equals("rectangleStroke")){
            	vPaint.setStyle(Paint.Style.STROKE);
            }*/

            
            
    		if (aSelected && pinADamaged.size()>0){
    			//Log.i("MyApplication", "damaged size is:" + damaged.size() );
    			lastPinALocation = pinADamaged.remove(0);
    			//Log.i("MyApplication", "removing from damaged last pin:" + lastPinALocation );
    			//Log.i("MyApplication", "damaged size is:" + damaged.size() );
    			drawStation(lastPinALocation, false);
    			if (lastPinBLocation !=null){
    			drawStation(lastPinBLocation, true);}
    		}
    		
    		if (bSelected && pinBDamaged.size()>0){
    			//Log.i("MyApplication", "damaged size is:" + damaged.size() );
    			lastPinBLocation = pinBDamaged.remove(0);
    			//Log.i("MyApplication", "removing from damaged last pin:" + lastPinALocation );
    			//Log.i("MyApplication", "damaged size is:" + damaged.size() );
    			drawStation(lastPinBLocation, false);
    			if (lastPinALocation!=null){
    			drawStation(lastPinALocation, true);}
    		}
    		
            if (event.getAction()== MotionEvent.ACTION_DOWN){
            	if (isOnPinA(newX, newY) && isOnPinB(newX, newY)){
            		//if overLapping, pick frontmost in z order 
            		if (fLayout.indexOfChild(pinA)>fLayout.indexOfChild(pinB)){
                    	aSelected=true;
                		pinAdXGrab=newX-oldPinAMarginLeft;
                		pinAdYGrab=newY-oldPinAMarginTop;
            		}
            		else {
                    	bSelected=true;
                		pinBdXGrab=newX-oldPinBMarginLeft;
                		pinBdYGrab=newY-oldPinBMarginTop;
            		}
            	}
            	else if (isOnPinA(newX, newY) && !bSelected){
            		//how far into the pin
            	aSelected=true;
        		pinAdXGrab=newX-oldPinAMarginLeft;
        		pinAdYGrab=newY-oldPinAMarginTop;
            	}
            	else if (isOnPinB(newX, newY) && !aSelected){
            		//how far into the pin
            	bSelected=true;
        		pinBdXGrab=newX-oldPinBMarginLeft;
        		pinBdYGrab=newY-oldPinBMarginTop;
            	}
            	Xprogram+= String.valueOf(newX).split("\\.")[0] + ",";
            	Yprogram+= String.valueOf(newY).split("\\.")[0] + ",";
                 //coordCount++;
                }
           
            else if (event.getAction()== MotionEvent.ACTION_UP){
            	//Log.i("MyApplication", "Releasing with a selected: " + aSelected + "B selected: " + bSelected);
        			
            		if (lastPinALocation!=null && aSelected){
            		
            		movePinATo(lastPinACoords[0], lastPinACoords[1]);
            		drawStation(lastPinALocation, true);
        			pinADamaged.add(lastPinALocation);
        			
            		}
            		
            		if (lastPinBLocation!=null&& bSelected){
                		
                		movePinBTo(lastPinBCoords[0], lastPinBCoords[1]);
                		drawStation(lastPinBLocation, true);
            			pinBDamaged.add(lastPinBLocation);
            			
                		}

            		aSelected=false;
            		bSelected=false;
            	Log.i("MyApplication", "Coords with count "+ coordCount + Xprogram + " " + Yprogram);
            }
            else {
            	float pinAXCoord;
        		float pinAYCoord;
        		float pinBXCoord;
        		float pinBYCoord;
            	if (mapPath.contains((int)newX, (int)newY)){  
            	//if (mapPath.contains((int)newX-dXGrab + (width/2), (int)newY-dYGrab + height)){ 
            		
            		if (aSelected){
            			pinAXCoord=newX-pinAdXGrab+(width/2);
            			pinAYCoord=newY-pinAdYGrab+height- 50;
            			
            			movePinA(pinAdX-pinAdXGrab, pinAdY-pinAdYGrab);
                		if (!stationForCoord(pinAXCoord, pinAYCoord).getName().equals("NULL")){

                			lastPinALocation = stationForCoord(pinAXCoord, pinAYCoord);
                    	//if (!stationForCoord(newX, newY).getName().equals("NULL")){
                		
                			//lastPinALocation = stationForCoord(newX, newY);
                			
                			if (lastPinBLocation!=null){
                			getTripInfo(lastPinALocation.getName(),lastPinBLocation.getName());
                			}
                			lastPinACoords[0]=oldPinAMarginLeft + pinAdX-pinAdXGrab;
                			lastPinACoords[1]=oldPinAMarginTop +  pinAdY- pinAdYGrab;
                			//lastPinALocation = p1;
                			drawStation(lastPinALocation, true);
                			pinADamaged.add(lastPinALocation);
                			
                		}
            		}
            		else if (bSelected){
            			pinBXCoord=newX-pinBdXGrab+(width/2);
            			pinBYCoord=newY-pinBdYGrab+height- 50;
            			movePinB(pinBdX-pinBdXGrab, pinBdY-pinBdYGrab);
                		if (!stationForCoord(pinBXCoord, pinBYCoord).getName().equals("NULL")){
                		
                			lastPinBLocation = stationForCoord(pinBXCoord, pinBYCoord);
                			
                			if (lastPinALocation !=null){
                			getTripInfo(lastPinALocation.getName(),lastPinBLocation.getName());
                			}
                			lastPinBCoords[0]=oldPinBMarginLeft + pinBdX-pinBdXGrab;
                			lastPinBCoords[1]=oldPinBMarginTop +  pinBdY- pinBdYGrab;
                			//lastPinALocation = p1;
                			drawStation(lastPinBLocation, true);
                			pinBDamaged.add(lastPinBLocation);
                			
                		}
            		}
            		
            	}
            	}
            invalidate();
         return true;   
        }
        
        public Polygon getLastPinA(){
        	return lastPinALocation;
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

	


	
