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
		try {
			receiveUDPMessage("224.0.0.5", 5550);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		super.run();
	}
	
	@SuppressWarnings("deprecation")
	private static void receiveUDPMessage(String AddressIp, int port) throws IOException {
		byte[] buffer = new byte[1024];
		MulticastSocket socket = new MulticastSocket(5550);
		InetAddress group = InetAddress.getByName("224.0.0.5");
		socket.joinGroup(group);
		while (true) {
			System.out.println("Aspettando di ricevere le informazioni...");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
			System.out.println("---\n---\n---\n***ECCO LE INFORMAZIONI:***\n");
			if ("OK".equals(msg)) {
				System.out.println("No more message. Exiting : " + msg);
				break;
			}
		}
		socket.leaveGroup(group);
		socket.close();
	}
	
}