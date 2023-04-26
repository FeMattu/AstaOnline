package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import client.AstaDataClient;

public class ConnettoreAsta extends Thread{
	
	private InetAddress inetAddress;
    private MulticastSocket multicastSocket;
    private AstaDataClient dati;
	
    // DALL'ID CI ARRIVO ALL'IP
    
	public ConnettoreAsta(AstaDataClient dati) {
		
		// MANCA CONFIGURARE I DATI IN MODO DA COLLEGARSI A QUEST'ASTA BENEDETTA
		
		this.dati = dati;
		
		try {
			this.inetAddress = InetAddress.getByName(dati.getIp());
			this.multicastSocket = new MulticastSocket(5550);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		// data sono i dati dell'asta
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
