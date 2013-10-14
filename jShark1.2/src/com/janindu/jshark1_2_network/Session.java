/*
 * --------------------------------
 * Author: Janindu Arukgoda
 * --------------------------------
 */
package com.janindu.jshark1_2_network;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

/*
 * Handles File I/O for a capture session as well as serializing and desirializing the packets
 */
public class Session {
	/********************************************************/
	public ArrayList<Packet> serializedList;
	/********************************************************/
	
	/********************************************************/
	public Session(ArrayList<Packet> pList) {
		serializedList = pList;
	}
	/********************************************************/
	
	/********************************************************/
	public Session(){
		
	}	
	/********************************************************/

	/********************************************************/
	public void writeToFile(String name) {
		String fileName = name;
		if(fileName.equals("")){
			fileName = "jSharkSession";
		}
		
		try {
			FileOutputStream fout = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/jShark/"+fileName+".jsf");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			
			for(int i=0;i<serializedList.size();i++){
				oos.writeObject(serializedList.get(i));
			}
			oos.close();
			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			
		}
	}	
	/********************************************************/
	
	/********************************************************/
	public ArrayList<Packet> readFromFile(String name){
		String fileName = name;
		/*if(fileName.equals("")){
			fileName = "jSharkSession";
		}*/
		serializedList = new ArrayList<Packet>();
		try {
			FileInputStream fin = new FileInputStream(Environment.getExternalStorageDirectory().getPath()+"/jShark/"+fileName);
			ObjectInputStream ois = new ObjectInputStream(fin);
			//Log.e("Reached point", "1");
			Packet temp = (Packet) ois.readObject();
			//Log.e("Reached point", "2");
			while(temp != null){  
				serializedList.add(temp);
				temp = (Packet) ois.readObject();
			}
			//Log.e("Reached point", "3");
			ois.close();
			fin.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e("File Handling", "ERROR!!!");
			Log.e("File Name Not Found", fileName);
			e.printStackTrace();
		} catch(EOFException e){
			Log.e("EOFException", "msg");
		}catch (IOException ek){
			Log.e("IOException", "msg");
		} catch (ClassNotFoundException e){
			Log.e("ClassNotFoundException", "msg");
		}
		
		//reConvert();
		return serializedList;
	}
	/********************************************************/
	
}
