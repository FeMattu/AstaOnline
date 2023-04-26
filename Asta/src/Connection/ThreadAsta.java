package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

import gambit.Asta;
import gambit.GestisciAste;
import gambit.Resources;

/**
 * <b>ThreadAsta class</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */
public class ThreadAsta extends Thread{

    private Resources resources;
    private Asta asta;
    private float nuovaOfferta, precOfferta;
    private static boolean finito = false;
    
    /**
     * <b>ThreadAsta constructor</b>
     * @param asta -> asta istance
     * @param r -> resources istance
     */
    public ThreadAsta(Asta asta, Resources resources){
        this.asta = asta;
        this.resources = resources;
        this.nuovaOfferta = 0;
        this.precOfferta = 0;
    }

    public Asta getAsta() {
		return asta;
	}
    
    public Resources getResources() {
		return resources;
	}
    
    /**
     * <b>Run method for ThreadAsta class</b>
     */
    public void run(){
        super.run();
        //settaggio data e ora inizio asta       
        asta.setDataOra_inizio(Timestamp.valueOf(LocalDateTime.now()));
        System.out.println(asta);
        
       
        
        
        
        
        
        
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
    
    private float getPrecOfferta() {
		return precOfferta;
	}
    
    private void setPrecOfferta(float precOfferta) {
		this.precOfferta = precOfferta;
	}
    
    private float getNuovaOfferta() {
		return nuovaOfferta;
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
    
    private static void countdownUDP() {
		for(int i=30;i>0;i--) {
			try {
				System.out.println(i+" secondi rimasti.");
				if(i==0) {
					finito = true;
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
}