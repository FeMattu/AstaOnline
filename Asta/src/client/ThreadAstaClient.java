package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ThreadAstaClient extends Thread{
	
	ThreadAstaClient() {
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
	}
	
	private static void receiveUDPMessage(String AddressIp, int port) throws IOException {
		// TODO Auto-generated method stub
		byte[] buffer = new byte[1024];
		MulticastSocket socket = new MulticastSocket(5550);
		InetAddress group = InetAddress.getByName("224.0.0.5");
		socket.joinGroup(group);
		System.out.println("Aspettando di connettersi all'asta...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		System.out.println("---\n---\n---\n***INIZIO ASTA***\n");
		float priceUp;
		//while (true) {
		//	
		//}
		socket.leaveGroup(group);
		socket.close();
	}
	
}