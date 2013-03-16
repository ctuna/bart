package edu.berkeley.cs160.clairetuna.prog3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

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

import android.os.AsyncTask;
import android.util.Log;

class TripCostTask extends AsyncTask<String, Void, String> {
	//12TH, 16TH, 19TH, 24TH
	//for testing
	private String fare;
	private String startTime;
	private String arrivalTime;
	private MainActivity master;
	private final String API_KEY = "TJK4-R9EV-6R8E-UW7T";
	private  String url = "http://api.bart.gov/api/sched.aspx?key="+ API_KEY;
	//private final String url = "http://api.bart.gov/api/bsa.aspx?cmd=count&key=" + API_KEY;
	private Document xmlDocument;

    public String doInBackground(String...stations )
    {
    	String stationOrig = stations[0];
    	String stationDest = stations[1];
    	url+="&cmd=depart&orig="+ stationOrig + "&dest=" + stationDest;
    	Log.i("MyApplication", "Going to the url: "+ url);
    		   HttpClient httpclient = new DefaultHttpClient();
    	       HttpResponse response;
    	       String responseString = null;
    	       
    	       
    	       try {
    	            response = httpclient.execute(new HttpGet(url));
    	            StatusLine statusLine = response.getStatusLine();
    	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
    	            	//Log.i("MyApplication", "In do in background: Status code OK");
    	                ByteArrayOutputStream out = new ByteArrayOutputStream();
    	                response.getEntity().writeTo(out);
    	                //Log.i("MyApplication", "In do in background: checkpoint 2");
    	                out.close();
    	                responseString = out.toString();
    	                //Log.i("MyApplication", "In do in background: checkpoint 3");
    	            } else{
    	            	//Log.i("MyApplication", "In do in background: Status code not okay");
    	                //Closes the connection.
    	                response.getEntity().getContent().close();
    	                throw new IOException(statusLine.getReasonPhrase());
    	            }
    	        } catch (ClientProtocolException e) {
    	            //TODO Handle problems..
    	        } catch (IOException e) {
    	            //TODO Handle problems..
    	        }
    	       Log.i("MyApplication", "In do in background: done");
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
	 	        fare = xmlDocument.getElementsByTagName("trip").item(0).getAttributes().getNamedItem("fare").getNodeValue();
	 	        int scheduleNumber = Integer.parseInt(xmlDocument.getElementsByTagName("time").item(0).getNodeValue());
	 	        startTime = xmlDocument.getElementsByTagName("trip").item(0).getAttributes().getNamedItem("origTimeMin").getNodeValue();
	 	        arrivalTime = xmlDocument.getElementsByTagName("trip").item(0).getAttributes().getNamedItem("origTimeMin").getNodeValue();
	 	       Log.i("MyApplication", "SCHEDULE NUMBER IS: " + scheduleNumber);
	 	        Log.i("MyApplication", "in try in onPostExecute");
        	}
        	catch (Exception ex) {
        		Log.i("MyApplication", "Exception in on onPostExecute");
        		//do something
        	}
        }
        else {
        	Log.i("MyApplication", "results to XML query were null");
        }
       
    	Log.i("MyApplication", "Task Done Executing");
    	Log.i("MyApplication", "IN TRIPCOST FARE IS : " + fare);
    	master.updateTripInfo();
    }

	public String getFare(){
		return fare;
	}
	
	public String getStartTime(){
		return startTime;
	}
	
	public String getArrivalTime(){
		return arrivalTime;
	}
	
	public void setMaster(MainActivity master){
		Log.i("MyApplication", "Set master is called");
		this.master=master;
	}

    }
    