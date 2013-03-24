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
	private String timeNow;
	private String trainName;
	private MainActivity master;
	private final String API_KEY = "TJK4-R9EV-6R8E-UW7T";
	private  String url = "http://api.bart.gov/api/sched.aspx?key="+ API_KEY;
	//private final String url = "http://api.bart.gov/api/bsa.aspx?cmd=count&key=" + API_KEY;
	private Document xmlDocument;
	
    public String doInBackground(String...stations )
    {
    	String stationOrig = stations[0];
    	String stationDest = stations[1];
    	//url+="&cmd=depart&orig="+ stationOrig + "&dest=" + stationDest;
    	url = "http://api.bart.gov/api/sched.aspx?key=MW9S-E7SL-26DU-VV8V&cmd=depart&orig="+stationOrig+"&dest="+stationDest;
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
    	        	e.printStackTrace();
    	            //TODO Handle problems..
    	        }
    	       Log.i("MyApplication", "In do in background: done, return string is: "+ responseString);
    	       
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
	 	        timeNow = xmlDocument.getElementsByTagName("time").item(0).getTextContent();
	 	        while (!foundTrip){
	 	        fare = xmlDocument.getElementsByTagName("trip").item(i).getAttributes().getNamedItem("fare").getNodeValue();
	 	        startTime = xmlDocument.getElementsByTagName("trip").item(i).getAttributes().getNamedItem("origTimeMin").getNodeValue();
	 	        arrivalTime = xmlDocument.getElementsByTagName("trip").item(i).getAttributes().getNamedItem("destTimeMin").getNodeValue();
	 	        trainName = xmlDocument.getElementsByTagName("leg").item(i).getAttributes().getNamedItem("trainHeadStation").getNodeValue();
	 	        if (TimeHelper.isAfter(timeNow, startTime)){
	 	        	foundTrip=true;
	 	        }
	 	        i++;
	 	        }
	 	       master.updateTripInfo();
        	}
        	catch (Exception ex) {
        		Log.i("MyApplication", "Exception in on onPostExecute");
        		ex.printStackTrace();
        		//do something
        	}
        }
        else {
        	Log.i("MyApplication", "results to XML query were null");
        }
       
    	Log.i("MyApplication", "Task Done Executing");
    	Log.i("MyApplication", "IN TRIPCOST FARE IS : " + fare);
    	Log.i("MyApplication", "IN TRIPCOST TIME NOW IS : " + timeNow);
    	Log.i("MyApplication", "IN TRIPCOST START TIME IS  : " + startTime);
    	Log.i("MyApplication", "IN TRIPCOST ARRIVAL TIME IS  : " + arrivalTime);
    	Log.i("MyApplication", "IN TRIPCOST ARRIVAL TRAIN NAME IS  : " + getTrain());
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
	
	public String getTimeNow(){
		return timeNow;
	}
	
	public String getTrain(){
		if (trainName.equals("RICH")){
			return "Richmond";
		}
		else if (trainName.equals("PITT")){
			return "Pittsburg/Bay Point";
		}
		else if (trainName.equals("FRMT")){
			return"Fremont";
		}
		else if (trainName.equals("MLBR")){
			return "Millbrae";
		}
		else if (trainName.equals("DALY")){
			return"Daly City";
		}
		else{
			return"Dublin/Pleasanton";
		}
		
		
		
		
		
	}
	
	public void setMaster(MainActivity master){
		Log.i("MyApplication", "Set master is called");
		this.master=master;
	}

    }
    