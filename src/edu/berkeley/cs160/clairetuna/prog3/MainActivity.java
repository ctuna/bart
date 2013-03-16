package edu.berkeley.cs160.clairetuna.prog3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import android.view.Menu;

public class MainActivity extends Activity {
	ImageMap mImageMap;
	private Document xmlDocument;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
	
	protected void onPostExecute(String results)
    {       
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
        		//do something
        	}
        }
    }
    

	
	private class BartCheckTask extends AsyncTask<String, Void, String> {

    	private final String API_KEY = "TJK4-R9EV-6R8E-UW7T";
    	private final String url = "http://api.bart.gov/api/bsa.aspx?cmd=count&key=" + API_KEY;
    	private Document xmlDocument;

        public String doInBackground(String... city)
        {
        		   
        		   HttpClient httpclient = new DefaultHttpClient();
        	       HttpResponse response;
        	       String responseString = null;
        	       
        	       
        	       try {
        	            response = httpclient.execute(new HttpGet(url));
        	            StatusLine statusLine = response.getStatusLine();
        	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
        	                ByteArrayOutputStream out = new ByteArrayOutputStream();
        	                response.getEntity().writeTo(out);
        	                out.close();
        	                responseString = out.toString();
        	            } else{
        	                //Closes the connection.
        	                response.getEntity().getContent().close();
        	                throw new IOException(statusLine.getReasonPhrase());
        	            }
        	        } catch (ClientProtocolException e) {
        	            //TODO Handle problems..
        	        } catch (IOException e) {
        	            //TODO Handle problems..
        	        }
        	        return responseString;
           }
        }
        
        
	
}

	
