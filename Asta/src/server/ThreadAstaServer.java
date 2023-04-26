package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import classi.Asta;

/**
 * <b>ThreadAsta class</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class ThreadAstaServer extends Thread{

    private Resources resources;
    private Asta asta;
    private float nuovaOfferta, precOfferta;
    private TimerAsta timer;
    
    /**
     * <b>ThreadAsta constructor</b>
     * @param asta -> asta istance
     * @param r -> resources istance
     */
    public ThreadAstaServer(Asta asta, Resources resources){
        this.asta = asta;
        this.resources = resources;
        this.nuovaOfferta = 0;
        this.precOfferta = 0;
        this.timer = new TimerAsta();
    }
    
    /**
     * <b>Run method for ThreadAsta class</b>
     */
    public void run(){
        super.run();
        //settaggio data e ora inizio asta       
        //asta.setDataOra_inizio(Timestamp.valueOf(LocalDateTime.now()));
        
        byte[] buffer = new byte[1024];
		MulticastSocket socket;
		InetAddress group;
		DatagramPacket packet;
		timer.start();
		try {
			socket = new MulticastSocket(5550);
			group = InetAddress.getByName("224.0.0.5");
			socket.joinGroup(group);
			System.out.println("Aspettando offerte dai compratori...");
			packet = new DatagramPacket(buffer, buffer.length);
			System.out.println("---\n---\n---\n***INIZIO ASTA***\n");
			while(timer.isFinito()) {
				socket.receive(packet);
				//System.out.println("Nuova offerta: "+);
			}
			socket.leaveGroup(group);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        
        
        
        
        //fine asta
        //aggiunta data e ora finea asta
        //asta.setDataOra_fine(Timestamp.valueOf(LocalDateTime.now()));
        //resources.getCurrentGambits().remove(asta);
        //resources.addAstaIntoDB(asta);
    }
    
    private boolean isOffertaChanged() {
    	if(getPrecOfferta()!=getNuovaOfferta()) {
    		return true;
    	}
		return false;
	}
    
    public Asta getAsta() {
		return asta;
	}
    
    public Resources getResources() {
		return resources;
	}
    
    private float getPrecOfferta() {
		return precOfferta;
	}
    
    private float getNuovaOfferta() {
		return nuovaOfferta;
	}
    
    private void setPrecOfferta(float precOfferta) {
		this.precOfferta = precOfferta;
	}
    
    private void setNuovaOfferta(float nuovaOfferta) {
		this.nuovaOfferta = nuovaOfferta;
	}
    
    private static void sendUDPMessage(String message, String ipAddress, int port) throws IOException {
    	DatagramSocket socket = new DatagramSocket();
		InetAddress group = InetAddress.getByName("224.0.0.5");
		byte[] msg = message.getBytes();
		DatagramPacket packet = new DatagramPacket(msg, msg.length, group, 5550);
		socket.send(packet);
		socket.close();
	}
    
}