package com.janindu.jshark_1_2_test;

import junit.framework.TestCase;

import org.junit.Test;

import com.janindu.jshark1_2_network.Packet;

public class TestPacket extends TestCase {
	String arp = "07:54:28.589279 arp who-has 10.0.3.2 tell 10.0.3.15";
	String udp = "07:54:30.597339 IP (tos 0x0, ttl 9, id 0, offset 0, flags [DF], proto UDP (17),length 38) 10.0.3.15.41783 > 222.165.163.177.33460: UDP, length 10";
	String tcp = "07:54:33.125437 IP (tos 0x0, ttl 64, id 42340, offset 0, flags [DF], proto TCP (6), length 40) 10.0.3.15.50386 > 222.165.163.20.443: F, cksum 0x8ee3 (incorrect(-> 0xc65b), 1:1(0) ack 1 win 15544";
	String icmp = "08:00:51.298565 IP (tos 0x0, ttl 64, id 0, offset 0, flags [DF], proto ICMP (1), length 84) 10.0.3.15 > 222.165.163.166: ICMP echo request, id 35598, seq 4, length 64";		
	
	Packet[] p = new Packet[4];

	@Test
	public void testConstruct() {
		p[0] = new Packet(arp);
		p[1] = new Packet(udp);
		p[2] = new Packet(tcp);
		p[3] = new Packet(icmp);
		assertEquals(arp, p[0].getString());
		assertEquals(udp, p[1].getString());
		assertEquals(tcp, p[2].getString());
		assertEquals(icmp, p[3].getString());
	}
	
	@Test
	public void testProcess(){
		p[0] = new Packet(arp);
		p[1] = new Packet(udp);
		p[2] = new Packet(tcp);
		p[3] = new Packet(icmp);
		
		for(int i=0;i<4;i++){
			p[i].process();
		}
		
		assertEquals("ARP", p[0].getProtocol());
		assertEquals("UDP", p[1].getProtocol());
		assertEquals("TCP", p[2].getProtocol());
		assertEquals("ICMP", p[3].getProtocol());
		
		assertEquals("10.0.3.15.41783", p[1].getSrc());
		assertEquals("10.0.3.15.50386", p[2].getSrc());
		assertEquals("10.0.3.15", p[3].getSrc());
		
		assertEquals("222.165.163.177.33460", p[1].getDst());
		assertEquals("222.165.163.20.443", p[2].getDst());
		assertEquals("222.165.163.166", p[3].getDst());
	}	

}
