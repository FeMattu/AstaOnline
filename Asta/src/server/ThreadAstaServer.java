package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

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
    }
    
    /**
     * <b>Run method for ThreadAsta class</b>
     */
    public void run(){
        super.run();
        //settaggio data e ora inizio asta       
        //asta.setDataOra_inizio(Timestamp.valueOf(LocalDateTime.now()));
        
        
        
        
        
        
        
        
        
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