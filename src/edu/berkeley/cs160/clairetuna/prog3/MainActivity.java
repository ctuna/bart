package edu.berkeley.cs160.clairetuna.prog3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	ImageMap mImageMap;
	private Document xmlDocument;
	Calendar rightNow = Calendar.getInstance();
	
	public TripCostTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		Log.i("MyApplication", "Starting application");
	    mImageMap = (ImageMap)findViewById(R.id.map);

	    System.out.println("IN ONCREATE");
	    System.out.println("DIFFERENCE IS:"+ TimeHelper.difference("11:32 AM", "11:34 AM"));
	    System.out.println("DIFFERENCE IS:"+ TimeHelper.difference("9:09 AM", "10:08 AM"));
	    System.out.println("DIFFERENCE IS:"+ TimeHelper.difference("11:15 AM", "1:00 PM"));
    	//getTripInfo("16TH", "24TH");
	
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
	
    
	public void getTripInfo(String stationOrig, String stationDest){		
		task = new TripCostTask();
		task.setMaster(this);
		task.execute(stationOrig, stationDest);
		
	}
	
	public void updateTripInfo(){
		Log.i("MyApplication", "getTrip Info after execute");
		rightNow= Calendar.getInstance();
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int minute = rightNow.get(Calendar.MINUTE);
		Log.i("MyApplication", "getTrip Info after get calendar");
		String departureTime = task.getStartTime();
		String fare = task.getFare();
		System.out.println("DEPARTURE TIME IS " + departureTime);
		int departureMinute = Integer.parseInt(departureTime.split(":")[1].substring(0, 2));
		System.out.println("DEPARTURE MINUTE IS " + departureMinute);
		System.out.println("THIS MINUTE IS " + minute);
		Log.i("MyApplication", "after string business");
		int difference = departureMinute-minute;
		System.out.println("LEAVING IN : "+ difference + "MINUTES" );
		System.out.println(rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE));
	}
	
	private class BartCheckTask extends AsyncTask<String, Void, String> {

    	private final String API_KEY = "TJK4-R9EV-6R8E-UW7T";
    	private final String url = "http://api.bart.gov/api/bsa.aspx?cmd=count&key=" + API_KEY;
    	private Document xmlDocument;

        public String doInBackground(String... city)
        {
        	Log.i("MyApplication", "In do in background");
        		   HttpClient httpclient = new DefaultHttpClient();
        	       HttpResponse response;
        	       String responseString = null;
        	       
        	       
        	       try {
        	            response = httpclient.execute(new HttpGet(url));
        	            StatusLine statusLine = response.getStatusLine();
        	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
        	            	Log.i("MyApplication", "In do in background: Status code OK");
        	                ByteArrayOutputStream out = new ByteArrayOutputStream();
        	                response.getEntity().writeTo(out);
        	                Log.i("MyApplication", "In do in background: checkpoint 2");
        	                out.close();
        	                responseString = out.toString();
        	                Log.i("MyApplication", "In do in background: checkpoint 3");
        	            } else{
        	            	Log.i("MyApplication", "In do in background: Status code not okay");
        	                //Closes the connection.
        	                response.getEntity().getContent().close();
        	                throw new IOException(statusLine.getReasonPhrase());
        	            }
        	        } catch (ClientProtocolException e) {
        	            //TODO Handle problems..
        	        } catch (IOException e) {
        	            //TODO Handle problems..
        	        }
        	       Log.i("MyApplication", "In do in background: checkpoint 4");
        	        return responseString;
           }
    	protected void onPostExecute(String results)
        {       
    		Log.i("MyApplication", "In onPostExecute");
            if(results != null)
            {   
            	try {
    	        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	 		   	DocumentBuilder builder = factory.newDocumentBuilder();
    	 		   	InputSource is = new InputSource(new StringReader(results));
    	 	        xmlDocument = builder.parse(is); 
    	 	        
    	 	       int traincount = Integer.parseInt(xmlDocument.getElementsByTagName("traincount").item(0).getTextContent());
    	 	       System.out.println("TRAINCOUNT IS: " + traincount);
            	}
            	catch (Exception ex) {
            		Log.i("MyApplication", "Exception in on onPostExecute");
            		//do something
            	}
            }
        	Log.i("MyApplication", "Task Done Executing");
        }

        }
        
        
	
}

	
