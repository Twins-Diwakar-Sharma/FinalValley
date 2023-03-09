package network;

import java.io.IOException;
import java.net.DatagramPacket;


public class Recieve implements Runnable{

	private UDPThing thing;
	private byte[] inData = new byte[1024];
	
	public Recieve(UDPThing thing){
		this.thing = thing;
	}
	
	public void run(){
		while(thing.go()){
			try {
				
		
				DatagramPacket in = new DatagramPacket(inData, inData.length);
				thing.getSocket().receive(in);

				
			    thing.setInData(in);
 
			} catch (IOException e) {

			}
	}
}
	
}
