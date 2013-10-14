package com.janindu.jshark_1_2_test;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import android.os.Environment;

import com.janindu.jshark1_2_network.Packet;
import com.janindu.jshark1_2_network.Session;

import junit.framework.TestCase;

public class TestSession extends TestCase {
	String arp = "07:54:28.589279 arp who-has 10.0.3.2 tell 10.0.3.15";
	String udp = "07:54:30.597339 IP (tos 0x0, ttl 9, id 0, offset 0, flags [DF], proto UDP (17),length 38) 10.0.3.15.41783 > 222.165.163.177.33460: UDP, length 10";
	String tcp = "07:54:33.125437 IP (tos 0x0, ttl 64, id 42340, offset 0, flags [DF], proto TCP (6), length 40) 10.0.3.15.50386 > 222.165.163.20.443: F, cksum 0x8ee3 (incorrect(-> 0xc65b), 1:1(0) ack 1 win 15544";
	String icmp = "08:00:51.298565 IP (tos 0x0, ttl 64, id 0, offset 0, flags [DF], proto ICMP (1), length 84) 10.0.3.15 > 222.165.163.166: ICMP echo request, id 35598, seq 4, length 64";		
	
	ArrayList<Packet> sList = new ArrayList<Packet>();
	

	public TestSession(){
		Packet[] p = new Packet[4];
		p[0] = new Packet(arp);
		p[1] = new Packet(udp);
		p[2] = new Packet(tcp);
		p[3] = new Packet(icmp);
		for(int i=0;i<p.length;i++){
			sList.add(p[i]);
		}
	}
	
	@Test
	public void testConstruct1(){
		Session s = new Session(sList);
		assertEquals(sList, s.serializedList);
	}
	
	@Test
	public void testConstruct2(){
		Session s = new Session();
		assertEquals(null, s.serializedList);
	}
	
	@Test
	public void fileWriteTest(){
		Session s = new Session(sList);
		s.writeToFile("test_file");
		ArrayList<String> myFiles = new ArrayList<String>();
		File f = new File(Environment.getExternalStorageDirectory().getPath()+"/jShark/");
		
		f.mkdirs();
        File[] files = f.listFiles();
        String temp[];
        if (files == null)
            assertEquals(true, false);
        else {
            for (int i=0; i<files.length; i++){
            	temp = files[i].getName().split("\\.");
            	if((temp.length==2)&&(temp[1].equals("jsf"))){
            		myFiles.add(files[i].getName());
            	}
            }
        }
        //Log.e("debugging","Reached point4");
		if(myFiles.contains("test_file.jsf"))
			assertEquals(true, true);
		else
			assertEquals(true, false);
	}
	
	@Test
	public void fileReadTest(){
		Session s = new Session(sList);
		s.writeToFile("test_file_2");
		
		ArrayList<Packet> p = s.readFromFile("test_file_2");
		
		assertEquals(sList, p);
	}
		
}


