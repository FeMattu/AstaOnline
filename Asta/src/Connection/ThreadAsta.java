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

    //private final int ID_ASTA;
    //private Date DataOra_inizio;
    //private Date DataOra_fine;
    //private String multicastIP;
    //private String porta;
    //private MulticastSocket socket;
    //private Prodotto prodotto;
    private Resources resources;
    private Asta asta;
    
    /**
     * <b>ThreadAsta constructor</b>
     * @param asta -> asta istance
     * @param r -> resources istance
     */
    public ThreadAsta(Asta asta, Resources resources){
        this.asta = asta;
        this.resources = resources;
    }

    /**
     * <b>Run method for ThreadAsta class</b>
     */
    public void run(){
        super.run();
        //settaggio data e ora inizio asta       
        asta.setDataOra_inizio(Timestamp.valueOf(LocalDateTime.now()));
        System.out.println(asta);
        
		try {
			byte[] buf = new byte[256];
			DatagramSocket socket = new DatagramSocket();
			InetAddress address = InetAddress.getByName(asta.getIp());
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5550);
			socket.send(packet);
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
             
        
        //per il test predno un client a caso
        int i = (int) (Math.random() * 100);     
        asta.setVincitore(resources.getClienti().get(i));
        
        
        
        //fine asta
        //aggiunta data e ora finea asta
        asta.setDataOra_fine(Timestamp.valueOf(LocalDateTime.now()));
        resources.getCurrentGambits().remove(asta);
        resources.addAstaIntoDB(asta);
    }
    
}