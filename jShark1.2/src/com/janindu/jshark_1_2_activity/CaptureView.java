/*
 * --------------------------------
 * Author: Janindu Arukgoda
 * --------------------------------
 */
package com.janindu.jshark_1_2_activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import com.janindu.jshark1_2_network.Packet;
import com.janindu.jshark1_2_network.Session;
import com.janindu.jshark_1_2.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
 * This class capture the packets and display them
 * Has an inner class PCapture
 */
@SuppressLint("HandlerLeak")
public class CaptureView extends Activity {
	/********************************************************/
	private Button stop;
	private Button save;
	private Button start;
	private TextView packetView;
	private EditText fileName;
	private boolean tcp = false;
	private boolean udp = false;
	private boolean icmp = false;
	private boolean arp = false;
	private String ip = "0.0.0.0";
	private PCapturer pcapture;
	private ArrayList<Packet> pList;
	/********************************************************/
	
	/********************************************************/
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			updatePacketView(msg.obj);
			//packetView.append((String) msg.obj+"\n");
		}
	};
	/********************************************************/

	/********************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_view);
		
		/********************************************************/
		stop = (Button) findViewById(R.id.cstop);
		save = (Button) findViewById(R.id.csave);
		start = (Button) findViewById(R.id.cstart);
		packetView = (TextView) findViewById(R.id.cpacketView);
		fileName = (EditText) findViewById(R.id.cfilename);
		
		stop.setOnClickListener(stopCapture);
		stop.setEnabled(false);
		save.setOnClickListener(saveCapture);
		save.setEnabled(false);
		start.setOnClickListener(startCapture);
		
		fileName.setEnabled(false);
		
		new ArrayList<String>();
		pList = new ArrayList<Packet>();
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){ 
			tcp = extras.getBoolean("tcp");
			udp = extras.getBoolean("udp");
			icmp = extras.getBoolean("icmp");
			arp = extras.getBoolean("arp");
			ip = extras.getString("ip");
			if(!tcp&&!udp&&!icmp&&!arp){
				tcp = true;
				udp = true;
				icmp = true;
				arp = true;
			}
		}
		
		setView();
		/********************************************************/
	}
	/********************************************************/

	/********************************************************/
	public void start() {
		// Starts the capture session		
		pcapture = new PCapturer(tcp, udp, icmp, ip);
		pcapture.start();
		
		
	}
	/********************************************************/
	
	/********************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.capture_view, menu);
		return true;
	}
	/********************************************************/
	
	/********************************************************/
	OnClickListener stopCapture = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			try{
				Process p = pcapture.getProcess();
				pcapture.interrupt();
				p.destroy();
			}catch(Exception e){
				packetView.append("ERROR CLOSING tcpdump!!!");
			}
			save.setEnabled(true);
			start.setEnabled(true);
			stop.setEnabled(false);
			fileName.setEnabled(true);
		}
	};
	/********************************************************/
	
	/********************************************************/	
	OnClickListener saveCapture = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// Stop the session and save it to a file
			stop.setEnabled(false);
			start.setEnabled(false);
			save.setEnabled(false);
			Session session = new Session(pList);
			session.writeToFile(fileName.getText().toString());
		}
	};
	/********************************************************/
	
	/********************************************************/	
	OnClickListener startCapture = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// Call for start() method to start capture.
			//packetView.append("Start Button Pressed\n");
			stop.setEnabled(true);
			start.setEnabled(false);
			save.setEnabled(false);
			fileName.setEnabled(false);
			start();			
		}
	};
	/********************************************************/
	
	/********************************************************/
	public void setView(){
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
	/********************************************************/	
	
	/********************************************************/	
	public void updatePacketView(Object obj){
		Packet p = (Packet) obj;
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
	/********************************************************/	
	
	public class PCapturer extends Thread {
		/*************************************************************************************/
		private Process p;
		private String command;
		private InputStream is;
		private BufferedReader reader;
		private String received;		
		/*************************************************************************************/
		
		/*************************************************************************************/
		public PCapturer(boolean tcp,boolean udp,boolean icmp,String ip){
			setCommand(tcp, udp, icmp, arp, ip);			
		}
		/*************************************************************************************/
		
		/*************************************************************************************/
		public void run(){
			startCapture();
		}
		/*************************************************************************************/
		
		/*************************************************************************************/
		public void startCapture(){ 
			try {//"tcpdump","-l","-n","-v","-i","eth1"
				p = new ProcessBuilder().command("su","-l").redirectErrorStream(true).start();
				OutputStream os = p.getOutputStream(); 
				DataOutputStream dos = new DataOutputStream(os);
				is = p.getInputStream();
				p.getOutputStream();
				String cmd = "tcpdump"+" -l"+" -n"+" -v"+" -i"+" eth0"+" \n";
				Log.e("CaptureView.265",cmd);
				dos.writeBytes(cmd);
				dos.flush();
				dos.writeBytes("exit\n");
				os.flush();
				os.close(); 
				
			} catch (IOException e) { 
				Log.e("IOException","captureView Line 262");
				e.printStackTrace(); 
			}	
			
			reader = new BufferedReader(new InputStreamReader(is));
			Packet temp;
			Log.e("CaptureView.285","here");
			try {
				int i=0; 
				while(true){
					
					if(reader.ready()){
						received = reader.readLine(); 
						//Ignore first two lines of tcpdump output
						if(received.contains("tcpdump")||i<2){
							Log.e("here", received);
							i++;
							continue;
						}
						if(received.contains("listening")){
							Log.e("here2", received);
							continue;
						}
						Log.e("received", received);
						//--------------------------
						//packetList.add(received);
						//--------------------------
						temp = new Packet(received);
						temp.process();
						pList.add(temp);
						
						Message msg = Message.obtain(handler);
						msg.obj = temp;
						//msg.obj = received;
						handler.sendMessage(msg);
					}					
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("CaptureView.316","here");
				e.printStackTrace();
			} 	
		}
		/*************************************************************************************/ 
		
		/***********************************************************************************/
		public void setCommand(boolean tcp,boolean udp,boolean icmp,boolean arp, String ip){
			//NOT CHANGED AFTER ARP IS ADDED BECAUSE NOT PLANNING TO USE THIS METHOD
			String[] cmd = {"tcpdump"," tcp"," or"," udp"," or"," icmp"," and"," host ", ip};
			boolean tcpswitch = true;
			boolean udpswitch = true;
			boolean icmpswitch = true;
			
			if(!tcp){
				cmd[1] = "";
				cmd[2] = "";
				tcpswitch = false;
			}
			if(!udp){
				cmd[3] = "";
				cmd[4] = "";
				udpswitch = false;
			}
			if(!icmp){
				cmd[5] = "";
	                        cmd[4] = "";
	                        icmpswitch = false;
				if(!udpswitch){
					cmd[2] = "";
					if(!tcpswitch){
						cmd[2] = ""; 
	                    if(!icmpswitch){
	                    	cmd[6] = "";
	                    }
					}
				}
			}		
				
			if(ip.equals("0.0.0.0")){
				cmd[6] = "";
				cmd[7] = "";
				cmd[8] = "";
			}		
			
			command = "";
			for(int i=0;i<cmd.length;i++){
				command.concat(cmd[i]);
			}
		}
		/*************************************************************************************/
		
		/*************************************************************************************/
		public Process getProcess(){
			return p;
		}
		/*************************************************************************************/	
		
	} 

			
}


