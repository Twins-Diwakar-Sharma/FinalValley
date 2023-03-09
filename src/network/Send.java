package network;

import java.io.IOException;
import java.net.DatagramPacket;

public class Send implements Runnable {
	
	private UDPThing thing;
	byte[] outData = new byte[1024];
	
	public Send(UDPThing thing){
			this.thing = thing;
	}

		public void run() {
			
			while(thing.go()){
				try{              
				outData = thing.getOutData();	
				DatagramPacket out = new DatagramPacket(outData,outData.length, thing.getIPAddress(), thing.getPort());
				thing.getSocket().send(out);
				}catch(Exception e){
					e.printStackTrace();
				}

			}
		}
}
