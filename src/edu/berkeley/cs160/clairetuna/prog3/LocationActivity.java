package edu.berkeley.cs160.clairetuna.prog3;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity implements LocationListener {

	LocationManager manager;
	MainActivity master;
	Location location;
	
	public LocationActivity (MainActivity master){
		super();
		this.master=master;
	}
	
	
	
	public void checkLocation(LocationManager lm) {

		//initialize location manager
		manager = lm;

		//check if GPS is enabled
		//if not, notify user with a toast
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	    	//Toast.makeText(this, "GPS is disabled.", Toast.LENGTH_SHORT).show();
	    } else {

	    	//get a location provider from location manager
	    	//empty criteria searches through all providers and returns the best one
	    	String providerName = manager.getBestProvider(new Criteria(), true);
	    	location = manager.getLastKnownLocation(providerName);
	    	Log.i("MyApplication", "set location in LocationActivity");
	    	if (location != null) {
	    		
	    	} else {
	    		Log.i("MyApplication", "location was not null");
	    	}
	    	//sign up to be notified of location updates every 15 seconds - for production code this should be at least a minute
	    	manager.requestLocationUpdates(providerName, 60000, 1, this);
	    }
	}

	public Location getLocation(){
		return location;
	}
	@Override
	public void onLocationChanged(Location location) {
    	if (location != null) {
    		
    	} else {
    		
    	}
	}

	@Override
	public void onProviderDisabled(String arg0) {}

	@Override
	public void onProviderEnabled(String arg0) {}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}


}