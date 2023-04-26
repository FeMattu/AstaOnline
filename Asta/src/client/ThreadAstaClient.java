package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import classi.Offerta;

/**
 * <b>Classe ThreadAstaClient</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class ThreadAstaClient extends Thread{

	AstaDataClient astaData;
	
	/**
	 * Costruttore della classe ThreadAstaClient
	 * @param astaData -> istanza di classe AstaDataClient passata come parametro
	 */
    ThreadAstaClient(AstaDataClient astaData) {
        // TODO Auto-generated constructor stub
    	this.astaData = astaData;
    }

    /**
     * Metodo run della classe ThreadAstaClient
     */
	public void run() {
        try {
        	System.out.println("Sono in ThreadAstaClient.");
            
            byte[] buffer = new byte[1024];
            MulticastSocket socket = new MulticastSocket(5550);
            InetAddress group = InetAddress.getByName("224.0.0.5");
            socket.joinGroup(group);
            
            // Itero finchè l'asta non è finita
            while (!this.astaData.isEnded()) {
            	System.out.println("Inserire un'offerta che sia maggiore di: " + this.astaData.getOffertaMaggiore().getOfferta() + " : ");
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                
                // ho ricevuto l'offerta/la comunicazione di chi è il vincitore
                
                if (msg.contains("vincitore")) {
                	// stato mandato il vincitore
                	
                	
                	if (msg.contains(this.astaData.getMeStesso().getUSERNAME())){
                		// HO VINTO IO
                		System.out.println("HAI ACQUISTATO IL PRODOTTO");
                	}
                	else {
                		System.out.println("NON HAI ACQUISTATO IL PRODOTTO");
                	}
                }
                else {
                	// una nuova offerta
                	
                	Offerta o = new Offerta(msg);
                	if (o.getOfferta() > this.astaData.getOffertaMaggiore().getOfferta()) {
                		// L'offerta è valida
                		
                		this.astaData.setOffertaMaggiore(o);
                	}
                }
            }
            
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        super.run();
    }
}