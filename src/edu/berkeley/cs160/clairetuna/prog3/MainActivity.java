package edu.berkeley.cs160.clairetuna.prog3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	ImageMap mImageMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doshit();
		setContentView(R.layout.activity_main);
	    mImageMap = (ImageMap)findViewById(R.id.map);
	    System.out.print("clicked something");
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
		
		

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public void doshit() throws IOException{
		String url = "www.example.com/myfile.xml";
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		
		response = client.execute(new HttpGet(url));
		StatusLine sl= response.getStatusLine();
		if(sl.getStatusCode() == HttpStatus.SC_OK){
		ByteArrayOutputStream out = new 
		ByteArrayOutputStream();
		response.getEntity().
		writeTo(out);
		out.close();
		System.out.println( out.toString());
		}

	
	
	
	
	
	
}
}

	
