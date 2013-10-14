/*
 * --------------------------------
 * Author: Janindu Arukgoda
 * --------------------------------
 */
package com.janindu.jshark_1_2_activity;

import com.janindu.jshark_1_2.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
 * This is the starting point of the app
 * User decides whether to do a live packet capture or to load a saved session
 */
public class MainActivity extends Activity {
	private Button start;
	private Button load;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*************************************************************/
		start = (Button) findViewById(R.id.buttonStartCapture);
		load = (Button) findViewById(R.id.buttonLoadSaved);
		
		start.setOnClickListener(startSniffing);
		load.setOnClickListener(loadFile);
		/*************************************************************/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*-----------------------------------------------------------------*/
	// When Start button is clicked, this onClickListener is invoked
	private OnClickListener startSniffing = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// Go to the CaptureSession activity 
			startActivity(new Intent(MainActivity.this, CaptureSession.class));				
		}
	};
	
	/*-----------------------------------------------------------------*/
	// When Load button is clicked, this onClickListener is invoked
	private OnClickListener loadFile = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// Go to SavedSession activity
			startActivity(new Intent(MainActivity.this, SavedSession.class));
		}
	};
}


