package Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import gambit.GestisciAste;
import gambit.Resources;

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

	static Resources resources = null;
	static GestisciAste gestioneAste = null;
	private static ServerSocket serverSocket = null;

	public static void main(String[] args) {

		String ip = null;
		try {
			ip = InetAddress.getByName("astadb.ddns.net").getHostAddress();
			System.out.println(ip);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String databaseURL = "jdbc:mysql://" + ip + ":3306";

		resources = new Resources(databaseURL, "gambit", "test", "trK5iuHPLQNLDZ9J");
		gestioneAste = new GestisciAste(resources);
		gestioneAste.run();
		
		/*Rimani mi serve per verificare che dopo aver instaurato la connessione TCP Client-Server 
		 * si venga reindirizzati a GestisciAste (almeno finchÃ© non viene fixato l'errore)*/
		try {
			serverSocket = new ServerSocket(5000);
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