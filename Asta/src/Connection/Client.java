package Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import gambit.Cliente;


public class Client {
	
	private static Socket socket;
	private static BufferedReader reader;
	private static DataOutputStream writer;
	private static Scanner scanner = new Scanner(System.in);
	static int scelta = 0;
	static boolean ricevuto,verificato=false;
	static String username;
	static String password;

	public static void main(String[] args) {
		
		LinkedList<Cliente> clienti = Server.resources.getClienti();
		for(int i=0;i<clienti.size();i++) {
			System.out.println(clienti.get(i).toString());
		}
		
		while(!verificato) {
			System.out.println("---\nLOGIN FORM:");
			System.out.print("Username: ");
			username = scanner.next();
			System.out.print("Password: ");
			password = scanner.next();
			
			for(int i=0;i<clienti.size();i++) {
				if(clienti.get(i).getUSERNAME().equals(username) && clienti.get(i).getPassword().equals(password)) {
					verificato = true;
					break;
				}
			}
		}
		
		serverConnection();

	}
	
	private static void serverConnection() {
		try {
			socket = new Socket("localhost", 5000);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new DataOutputStream(socket.getOutputStream());
			
			
			System.out.println(reader.readLine()+"\n---");
			
			//Menu per scelta su cosa fare
			while(scelta != 3) {
				System.out.println("\nCosa vuoi fare?\n");
				System.out.println("1. Partecipare ad un'asta");
				System.out.println("2. Aggiungere un prodotto");
				System.out.println("3. Niente\n");
				System.out.print("Scelta: ");
				scelta = scanner.nextInt();
				writer.writeBytes(scelta+"\n");
				
				switch(scelta) {
				case 1:
					String infoAsta="";
					//while(!infoAsta.equals(null)) {
					//	if(ServerTCPThread.inviato) {
					//		infoAsta = reader.readLine();
					//		ricevuto = true;
					//	}
					//	ricevuto = false;
					//}
					System.out.println("\nQuale asta scegli? [ID]: ");
					int sceltaAsta = scanner.nextInt();
					writer.writeBytes(sceltaAsta+"\n");
					break;
				case 2:
					System.out.println();
					break;
				default:
					System.out.println("Termine programma.");
					reader.close();
					writer.close();
					socket.close();
					break;
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(socket != null) {
    		try {
    			socket.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	} 	
	}

}