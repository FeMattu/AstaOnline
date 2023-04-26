package client;

import java.net.InetAddress;
import java.net.MulticastSocket;

public class ConnettoreAsta extends Thread{
	
	private InetAddress inetAddress;
    private MulticastSocket multicastSocket;
	private AstaDataClient astaDataClient;
	
    // DALL'ID CI ARRIVO ALL'IP
    
	public ConnettoreAsta() {
		
		// MANCA CONFIGURARE I DATI IN MODO DA COLLEGARSI A QUEST'ASTA BENEDETTA
		
	}
	
	public void run() {
		// data sono i dati dell'asta
		Input input = new Input(astaDataClient, multicastSocket, inetAddress);
		ThreadAstaClient client = new ThreadAstaClient(astaDataClient, multicastSocket);
		
		input.start();
		client.start();
		
		// FinchÃ¨ non terminano non torno al main
		
		try {
			client.join();
            input.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
	}
}