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
import android.widget.CheckBox;
import android.widget.EditText;

/*
 * This class allows the user to specify live packet capture parameters
 */
public class CaptureSession extends Activity {
	/*************************************************************/
	private CheckBox tcp;
	private CheckBox udp;
	private CheckBox icmp;
	private CheckBox arp;
	private EditText ip;
	private Button go;
	/*************************************************************/

	/*************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_session);
		
		/*************************************************************/
		tcp = (CheckBox) findViewById(R.id.ctcp);
		udp = (CheckBox) findViewById(R.id.cudp);
		icmp = (CheckBox) findViewById(R.id.cicmp);
		arp = (CheckBox) findViewById(R.id.carp);
		ip = (EditText) findViewById(R.id.cip);
		go = (Button) findViewById(R.id.cgo);
		
		go.setOnClickListener(liveCapture);
		/*************************************************************/
	}
	/*************************************************************/

	/*************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.capture_session, menu);
		return true;
	}
	/*************************************************************/
	
	/*************************************************************/
	/*-----------------------------------------------------------------*/
	// When Start button is clicked, this onClickListener is invoked
	OnClickListener liveCapture = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// The parameters of the session is passed to CaptureView activity
			Intent i = new Intent(CaptureSession.this, CaptureView.class);
			i.putExtra("tcp", tcp.isChecked());
			i.putExtra("udp", udp.isChecked());
			i.putExtra("icmp", icmp.isChecked());
			i.putExtra("arp", arp.isChecked());
			i.putExtra("ip", ip.getText().toString());
			startActivity(i);
		}
	};
	/*************************************************************/
}
