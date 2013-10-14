/*
 * --------------------------------
 * Author: Janindu Arukgoda
 * --------------------------------
 */
package com.janindu.jshark_1_2_activity;

import java.io.File;
import java.util.ArrayList;
import com.janindu.jshark_1_2.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SavedSession extends Activity {
	/*************************************************************/
	private CheckBox tcp;
	private CheckBox udp;
	private CheckBox icmp;
	private CheckBox arp;
	private EditText ip;
	private Button go;
	private ListView fileListView;
	private TextView fileName;
	private String fileNameString;
	/*************************************************************/
	
	/*************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_session);
		
		/*************************************************************/
		tcp = (CheckBox) findViewById(R.id.stcp);
		udp = (CheckBox) findViewById(R.id.sudp);
		icmp = (CheckBox) findViewById(R.id.sicmp);
		arp = (CheckBox) findViewById(R.id.sarp);
		ip = (EditText) findViewById(R.id.sip);
		go = (Button) findViewById(R.id.sgo);
		fileListView = (ListView) findViewById(R.id.sfileListView);
		fileName = (TextView) findViewById(R.id.sfilename);
		
		go.setOnClickListener(savedCapture);
		go.setEnabled(false);	
		//Log.e("Debugging","Reached point1");
		ArrayList<String> myFiles = getFiles();
		//myFiles = null;
		if(myFiles!=null){
			for(int i=0;i<myFiles.size();i++){
				Log.e(Integer.toString(i),myFiles.get(i));
			}
			fileListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, myFiles));
			
			fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
					fileNameString = (String) fileListView.getItemAtPosition(arg2);
					//Log.e("clicked",fileName);
					go.setEnabled(true);
					fileName.setText("Open File "+fileNameString);
					fileName.setVisibility(View.VISIBLE);
				}			
			});
		}
		else{
			Log.e("myfiles","null");
			myFiles = new ArrayList<String>();
			myFiles.add("No Saved Files Found");
			fileListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myFiles));
		}
		//Log.e("debugging","Reached point6");
	}
	/*************************************************************/

	/*************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_view, menu);
		return true;
	}
	/*************************************************************/
	
	/*************************************************************/	
	OnClickListener savedCapture = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i = new Intent(SavedSession.this, SavedView.class);
			i.putExtra("tcp", tcp.isChecked());
			i.putExtra("udp", udp.isChecked());
			i.putExtra("icmp", icmp.isChecked());
			i.putExtra("arp", arp.isChecked());
			i.putExtra("ip", ip.getText().toString());
			i.putExtra("fileName", fileNameString);
			startActivity(i);		
		}
	};
	/*************************************************************/
	
	/*************************************************************/
	private ArrayList<String> getFiles() { 
		ArrayList<String> myFiles = new ArrayList<String>();
		//Log.e("debugging","Reached point2");
		File f = new File(Environment.getExternalStorageDirectory().getPath()+"/jShark/");
		//Log.e("debugging","Reached point3");
        f.mkdirs();
        File[] files = f.listFiles();
        String temp[];
        if (files == null)
            Log.e("File List", "No files detected");
        else {
            for (int i=0; i<files.length; i++){
            	//Log.e(files[i].getName(), "msg");
            	temp = files[i].getName().split("\\.");
            	for(int j=0;j<temp.length;j++){
            		Log.e(temp[j],"-------");
            	}
            	//Log.e(temp.toString(), "msg");
            	if((temp.length==2)&&(temp[1].equals("jsf"))){
            		myFiles.add(files[i].getName());
            	}
            }
        }
        //Log.e("debugging","Reached point4");
		return myFiles;		
	}
	/*************************************************************/

}
