package edu.berkeley.cs160.clairetuna.prog3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

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

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
public class StationsTask extends AsyncTask<String, Void, String> {
	Context locationContext;
	LocationManager manager;
	HashMap<String, Double[]> stationCoordinates= new HashMap<String, Double[]>();
	
		private MainActivity master;
		private final String API_KEY = "TJK4-R9EV-6R8E-UW7T";
		private String stationsUrl = "http://api.bart.gov/api/stn.aspx?cmd=stns&key="+ API_KEY;
		private Document xmlDocument;
		
		public void setMaster(MainActivity master){
			this.master=master;
			Log.i("MyApplication", "IN THE CLASS WTF");
		}
	    public String doInBackground(String...stations )
	    {
			Log.i("MyApplication", "LocationHelper in Do in background: visiting "+ stationsUrl);
	    		   HttpClient httpclient = new DefaultHttpClient();
	    	       HttpResponse response;
	    	       String responseString = null;
	    	       
	    	       
	    	       try {
	    	            response = httpclient.execute(new HttpGet(stationsUrl));
	    	            StatusLine statusLine = response.getStatusLine();
	    	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	    	            	Log.i("MyApplication", "In do in background: Status code OK");
	    	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	                response.getEntity().writeTo(out);
	    	                //Log.i("MyApplication", "In do in background: checkpoint 2");
	    	                out.close();
	    	                responseString = out.toString();
	    	                //Log.i("MyApplication", "In do in background: checkpoint 3");
	    	            } else{
	    	            	Log.i("MyApplication", "In do in background: Status code not okay");
	    	                //Closes the connection.
	    	                response.getEntity().getContent().close();
	    	                throw new IOException(statusLine.getReasonPhrase());
	    	            }
	    	        } catch (ClientProtocolException e) {
	    	        	Log.i("MyApplication", "Client protocol exception");
	    	            //TODO Handle problems..
	    	        } catch (IOException e) {
	    	        	Log.i("MyApplication", "Io");
	    	            //TODO Handle problems..
	    	        }
	    	       
	    	       
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
		 	        boolean foundTrip = false;
		 	        int i = 0;
		 	        String stationAbbrev;
		 	        Double[] stationCoords = new Double[2];
		 	        //there are 44 bart stations
		 	        for (i=0; i<44;i++){
		 	        	stationAbbrev = xmlDocument.getElementsByTagName("abbr").item(i).getTextContent();
		 	        	stationCoords[0]=Double.parseDouble(xmlDocument.getElementsByTagName("gtfs_latitude").item(i).getTextContent());
		 	        	stationCoords[1] = Double.parseDouble(xmlDocument.getElementsByTagName("gtfs_longitude").item(i).getTextContent());
		 	        	stationCoordinates.put(stationAbbrev, stationCoords);  }
		 	        master.setCoordinates(stationCoordinates);
		 	        
	        	}
	        	catch (Exception ex) {
	        		Log.i("MyApplication", "Exception in on onPostExecute");
	        		//do something
	        	}
	        }
	        else {
	        	Log.i("MyApplication", "results to XML query were null");
	        }
	       
	    }

	}
