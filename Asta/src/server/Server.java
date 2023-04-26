package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <b>Server class</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class Server {
	public static void main(String[] args) {
		Resources resources = null;
		GestisciAste gestioneAste = null;
		ServerSocket serverSocket = null;
		
		String ip = null;
		try {
			ip = InetAddress.getByName("astadb.ddns.net").getHostAddress();
			System.out.println(ip);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String databaseURL = "jdbc:mysql://" + ip + ":3306";

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		resources = new Resources(databaseURL, "gambit", "test", "trK5iuHPLQNLDZ9J");	// Connessione database
		gestioneAste = new GestisciAste(resources);
		gestioneAste.start();
		
		/*
		 * Prendi un set di oggetti, programmi le aste 
		 */
		
		
		try {
			serverSocket = new ServerSocket(5234);
			while (true) {
				System.out.println("Sono in attesa di accettare un client");

				Socket comunicationSocketFromServer = serverSocket.accept();
				System.out.println("Client accettato: " + comunicationSocketFromServer.getPort());

				new ServerTCPThread(comunicationSocketFromServer, resources).start();
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}