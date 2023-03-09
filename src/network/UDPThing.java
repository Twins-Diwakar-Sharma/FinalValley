package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public interface UDPThing {
   
	public boolean go();
	
    public byte[] getOutData();
    
    public void setInData(DatagramPacket in);
    
    public InetAddress getIPAddress();
    
    public int getPort();
    
    public DatagramSocket getSocket();

}
