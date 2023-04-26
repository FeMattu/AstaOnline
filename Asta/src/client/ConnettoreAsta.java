package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * <b>Classe ConnettoreAsta</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class ConnettoreAsta extends Thread{
	
	private InetAddress inetAddress;
    private MulticastSocket multicastSocket;
    private AstaDataClient dati;
	
    // DALL'ID CI ARRIVO ALL'IP
    
    /**
     * Costruttore classe ConnettoreAsta
     * @param dati -> dati passati come parametro
     */
	public ConnettoreAsta(AstaDataClient dati) {
		this.dati = dati;
		
		try {
			this.inetAddress = InetAddress.getByName(dati.getIp());
			this.multicastSocket = new MulticastSocket(5550);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo run della classe ConnettoreAsta
	 */
	public void run() {
		// data sono i dati dell'asta
		System.out.println("Sono nel connettore.");
		Input input = new Input(this.dati, multicastSocket, inetAddress);
		ThreadAstaClient client = new ThreadAstaClient(this.dati);
		
		input.start();
		client.start();
		
		// Finchè non terminano non torno al main
		
		try {
			client.join();
            input.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
	}
}
