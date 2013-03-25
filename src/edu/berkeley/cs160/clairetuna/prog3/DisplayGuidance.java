package edu.berkeley.cs160.clairetuna.prog3;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class DisplayGuidance extends Activity{
	
	@Override
	//Try BitmapFactory.decodeFile() and then setImageBitmap() on the ImageView.
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.guidance); 
		ticketHolder = (FrameLayout) findViewById(R.id.ticketholder);
		
		
		instantiateLayout();
		
		
		
		super.onCreate(savedInstanceState);

	  
        
		Log.i("MyApplication", "Starting application");
//	    
	   // updateLocation();
	   getStationInfo();
	}
}
