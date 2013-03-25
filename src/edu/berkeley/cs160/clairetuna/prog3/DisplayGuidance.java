package edu.berkeley.cs160.clairetuna.prog3;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayGuidance extends Activity{
	String[] gooodies;
	@Override
	//Try BitmapFactory.decodeFile() and then setImageBitmap() on the ImageView.
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guidance); 
		String [] goodies= getIntent().getStringArrayExtra("GOODIES");
		initiate(goodies);
	}
	
	public void initiate(String[] goodies){
		LinearLayout l = (LinearLayout)findViewById(R.id.layout);
		TextView ticketAdvice = (TextView)findViewById(R.id.ticketAdvice);

		TextView boardTrain1 = (TextView)findViewById(R.id.boardTrainAdvice);
		TextView exitTrain1 = (TextView)findViewById(R.id.exitTrainAdvice);
		TextView boardTrain2Title = (TextView) findViewById(R.id.boardTrain2Title);
		TextView boardTrain2 = (TextView)findViewById(R.id.boardTrain2Advice);
		TextView exitTrain2Title = (TextView) findViewById(R.id.exitTrain2Title);
		TextView exitTrain2 = (TextView)findViewById(R.id.exitTrain2Advice);
		
		String stationOrig = goodies[1];
		String stationDest = goodies[2];
		String stationTransfer = goodies[3];
		boolean hasConnection = goodies[0].equals("true");
		String fare = goodies[4];
		String train1 = goodies[5];
		String train2 = goodies[6];
		String time = goodies[7];
		String roundTripCost = goodies[8];
	
		ticketAdvice.setText("Buy one-way ticket for $" + fare + " or a round-trip ticket for $"+ roundTripCost);
		boardTrain1.setText("Board the " + train1 + " train at " + time);
		if (hasConnection){
		exitTrain1.setText("Exit the train at the " + stationTransfer + " stop.");
		boardTrain2Title.setText("6");
		boardTrain2.setText("Transfer to the " + train2 + " train.");
		
		exitTrain2Title.setText("7");
	    exitTrain2.setText("Exit at the  " + stationDest + " stop.");
		}
		else{
			exitTrain1.setText("Exit the train at the " + stationDest + " stop.");
		}
		//l.addView(child)
	}
}
