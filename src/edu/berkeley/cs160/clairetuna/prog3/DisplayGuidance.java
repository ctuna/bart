package edu.berkeley.cs160.clairetuna.prog3;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayGuidance extends Activity{
	
	@Override
	//Try BitmapFactory.decodeFile() and then setImageBitmap() on the ImageView.
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guidance); 
		
	}
	
	public void initiate(){
		LinearLayout l = (LinearLayout)findViewById(R.id.layout);
		TextView ticketAdvice = (TextView)findViewById(R.id.ticketAdvice);

		TextView boardTrain1 = (TextView)findViewById(R.id.boardTrainAdvice);
		TextView exitTrain1 = (TextView)findViewById(R.id.exitTrainAdvice);
		TextView boardTrain2 = (TextView)findViewById(R.id.boardTrain2Advice);
		TextView exitTrain2 = (TextView)findViewById(R.id.exitTrain2Advice);
		//l.addView(child)
	}
}
