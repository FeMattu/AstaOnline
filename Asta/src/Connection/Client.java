package Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import gambit.Asta;
import gambit.GestisciAste;

public class Client {
	
	private static Socket socket;
	private static BufferedReader reader;
	private static DataOutputStream writer;
	private static Scanner s = new Scanner(System.in);
	static int scelta = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
				scelta = s.nextInt();
				writer.writeBytes(scelta+"\n");
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
