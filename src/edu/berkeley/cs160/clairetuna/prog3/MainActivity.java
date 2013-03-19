package edu.berkeley.cs160.clairetuna.prog3;

import java.util.Calendar;
import java.util.HashMap;

import org.w3c.dom.Document;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

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
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		Log.i("MyApplication", "Starting application");
	    mImageMap = (ImageMap)findViewById(R.id.map);
	    locationActivity = new LocationActivity(this);
    	//getTripInfo("24TH", "16TH");


	    
	    
        // add a click handler to react when areas are tapped
        mImageMap.addOnImageMapClickedHandler(new ImageMap.OnImageMapClickedHandler() {
            @Override
            public void onImageMapClicked(int id) {            	
                // when the area is tapped, show the name in a
                // text bubble
                mImageMap.showBubble(id);
            }
 
            @Override
            public void onBubbleClicked(int id) {
                // react to info bubble for area being tapped
            }
            
            
        });
		
		

		
	    updateLocation();
	   getStationInfo();
	}

	public void updateLocation(){
		LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationActivity.checkLocation(manager);
		userLocation = locationActivity.getLocation();
    	//LocationHelper locationHelper = new LocationHelper(this);
	}
	
	public String closestStation(){
		Log.i("MyApplication", "In MainActivity/closestStation");
		float[] results = new float[2];
		Log.i("MyApplication", "In MainActivity/closestStation before getLatitude");
		Double userLatitude = userLocation.getLatitude();
		Double userLongitude = userLocation.getLongitude();
		Log.i("MyApplication", "In MainActivity/closestStation() after getting");
		String closestStation=null;
		float minDistance = Float.POSITIVE_INFINITY;
		for (String key: stationCoordinates.keySet()){
			Location.distanceBetween(userLatitude, userLongitude, stationCoordinates.get(key)[0],stationCoordinates.get(key)[1], results);
			if (results[0]< minDistance){
				minDistance=results[0];
				closestStation = key;
			}
		}
		Log.i("MyApplication", "Closest station is: " + closestStation);
		return closestStation;
	}
	
	public void setCoordinates(HashMap<String, Double[]> coords){
		this.stationCoordinates=coords;
		Log.i("MyApplication", "received coordinates in master");
		closestStation();
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
	
	

}

	


	
