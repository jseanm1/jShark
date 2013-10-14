/*
 * --------------------------------
 * Author: Janindu Arukgoda
 * --------------------------------
 */
package com.janindu.jshark_1_2_activity;

import java.util.ArrayList;

import com.janindu.jshark1_2_network.Packet;
import com.janindu.jshark1_2_network.Session;
import com.janindu.jshark_1_2.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class SavedView extends Activity {
	/*************************************************************/
	private TextView packetView;
	private boolean tcp;
	private boolean udp;
	private boolean icmp;
	private boolean arp;
	private String ip;
	private ArrayList<Packet> packetList;
	private String fileName;
	/*************************************************************/

	/*************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_view);
		
		packetView = (TextView) findViewById(R.id.spacketView);
		
		//back.setOnClickListener(goBack); GIVES A NULL POINTER EXCEPTION!!!		
		start();
	}
	/*************************************************************/

	/*************************************************************/
	private void start() {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			tcp = extras.getBoolean("tcp");
			udp = extras.getBoolean("udp");
			icmp = extras.getBoolean("icmp");
			arp = extras.getBoolean("arp");
			ip = extras.getString("ip");
			fileName = extras.getString("fileName"); 
			if(!tcp&&!udp&&!icmp&&!arp){
				tcp = true;
				udp = true;
				icmp = true;
				arp = true;
			}
			
			setView();
			
			Session session = new Session();
			packetList = session.readFromFile(fileName);
			Log.e("packetList size",Integer.toString(packetList.size()));
			packetView.append("\nAll set to read\n");
			for(int i=0;i<packetList.size();i++){
				printPacket(packetList.get(i));
			}
		} 		
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
	private void setView(){
		// Show the filters on packetView 
		
		packetView.append("Protocols: ");
		if(!tcp&&!udp&&!icmp&&!arp){
			packetView.append("No filter");
		}
		else{
			if(tcp){
				packetView.append("TCP ");
			}
			if(udp){
				packetView.append("UDP ");
			}
			if(icmp){
				packetView.append("ICMP ");
			}
			if(arp){
				packetView.append("ARP");
			}
		}
		
		packetView.append("\nIP: ");
		if(ip.equals("")){
			packetView.append("All\n");				
		}
		else{
			packetView.append(ip+"\n");
		}		
	}
	/*************************************************************/
	
	/*************************************************************/
	private void printPacket(Packet p){
		String protocol = p.getProtocol();
		//packetView.append(protocol+"\n");
		if(protocol.equals("TCP")&&tcp)
			packetView.append(p.getTimeStamp()+" "+p.getSrc()+" -> "+p.getDst()+" "+p.getProtocol()+"\n");
		else if(protocol.equals("UDP")&&udp)
			packetView.append(p.getTimeStamp()+" "+p.getSrc()+" -> "+p.getDst()+" "+p.getProtocol()+"\n");
		else if(protocol.equals("ICMP")&&icmp)
			packetView.append(p.getTimeStamp()+" "+p.getSrc()+" -> "+p.getDst()+" "+p.getProtocol()+"\n");
		else if(protocol.equals("ARP")&&arp){
			// ARP packets are displayed a little differently
			packetView.append(p.getString()+"\n");		
		}
	}
	/*************************************************************/
}
