package Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;

import gambit.Asta;
import gambit.GestisciAste;
import gambit.Resources;

public class ServerTCPThread extends Thread{
	
	private Socket socket;
	private BufferedReader reader;
	private DataOutputStream writer;
	private Resources resources;
	
    public ServerTCPThread(Socket socket, Resources resources){
        this.socket=socket;
        this.resources=resources;
        try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void run() {
    	// TODO Auto-generated method stub
    	super.run();
    	try {
			writer.writeBytes("Connessione effettuata con successo\n");
			String scelta;
			do {
				scelta = reader.readLine();
				switch(scelta) {
				case "1":
					//Vuole partecipare ad un asta, vengono mostrate quelle disponibili
					//Stampa delle aste disponibili -> DEVE FARLO LATO CLIENT, NON RIESCO A TROVARE LA MANIERA PER FARLO
					Server.gestioneAste.run();
					break;
				case "2":
					//Vuole aggiungere un prodotto da mettere all'asta
					
					break;
				default:
					System.out.println("Termine programma.");
					break;
				}
			}while(scelta != "3");
				
			
			
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
