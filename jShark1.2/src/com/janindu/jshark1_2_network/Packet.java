/*
 * --------------------------------
 * Author: Janindu Arukgoda
 * --------------------------------
 */
package com.janindu.jshark1_2_network;

import java.io.Serializable;

/*
 * This class is the container for a single packet
 */
@SuppressWarnings("serial")
public class Packet implements Serializable {
	private String s;
	private String protocol;
	private String src;
	private String dst;
	private String timeStamp;
	
	public Packet(String str){
		s = str;
	}
	
	public String getString(){
		return s;
	}
	
	public void process(){
		//Set protocol, source address and destination address
		this.src = "unspecified";
		this.dst = "unspesified";
		if(s.contains("TCP")){
			protocol = "TCP";
		}
		else if(s.contains("ICMP")){
			protocol = "ICMP";
		}
		else if(s.contains("UDP")){
			protocol = "UDP";
		}
		else if(s.contains("arp")){
			protocol = "ARP";
		}
		else if(s.contains("proto")){
			String[] temp = s.split(" ");
			for(int i=0;i<temp.length;i++){
				if(temp[i].equals("proto")){
					try{
						protocol = temp[i+i];
						break;
					} catch(ArrayIndexOutOfBoundsException e){
						
					}
				}
			}
		}
		else{
			protocol = "Unknown";
		}
		String[] temp;
		int index = -1;
		temp = s.split(" ");
		timeStamp = temp[0].substring(0, temp[0].length()-7);
		for(int i=0;i<temp.length;i++){
			if(temp[i].equals(">")){
				index = i;
				break;
			}
		}
		if(index!=-1){
			try{
				src = temp[index-1];
				dst = temp[index+1].substring(0, temp[index+1].length()-1);
			}catch(ArrayIndexOutOfBoundsException e){
				
			}
		}
		else{
			for(int i=0;i<temp.length;i++){
				if(temp[i].equals("tell")||temp[i].equals("is-at")){
					index = i;
					break;
				}
			}
			if(index!=-1){
				try{
					src = temp[index+1];
					dst = temp[index-1];
				}catch(ArrayIndexOutOfBoundsException e){
					
				}
			}
		}
		
		
	}
	
	public String getProtocol(){
		return protocol;
	}
	
	public String getSrc(){
		return src;
	}
	
	public String getDst(){
		return dst;
	}
	
	public String getTimeStamp(){
		return timeStamp;
	}
}
