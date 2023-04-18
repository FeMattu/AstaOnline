package Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import gambit.Asta;
import gambit.Resources;

/**
 * <b>ServerTCPThread class</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */
public class ServerTCPThread extends Thread{
	
	private Socket socket;
	private BufferedReader reader;
	private DataOutputStream writer;
	private Resources resources;
	static boolean inviato;
	
	/**
	 * <b>ServerTCPThread constructor</b>
	 * @param socket -> socket used for the connection
	 * @param resources -> resources istance 
	 */
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
    
    /**
     * <b>Run method for ServerTCPThread class</b>
     */
    @Override
    public void run() {
    	// TODO Auto-generated method stub
    	super.run();
    	try {
			writer.writeBytes("Connessione effettuata con successo\n");
			String sceltaMenu;
			do {
				sceltaMenu = reader.readLine();
				switch(sceltaMenu) {
				case "1":
					//Vuole partecipare ad un asta, vengono mostrate quelle disponibili
					List<Asta> aste = resources.getCurrentGambits();
					for(int i=0;i<aste.size();i++) {
						if(Client.ricevuto) {
							writer.writeBytes(aste.get(i).getId_asta()+":"
									+aste.get(i).getProdotto()+":"
									+aste.get(i).getPrezzoCorrente()+"\n");
							inviato = true;
						}
						inviato = false;
					}
					String sceltaAsta = reader.readLine();
					System.out.println(sceltaAsta);
					break;
				case "2":
					//Vuole aggiungere un prodotto da mettere all'asta
					
					//Prodotto p = new Prodotto();
					//resources.addProdotto(p);
					break;
				default:
					System.out.println("Termine programma.");
					reader.close();
					writer.close();
					socket.close();
					break;
				}
			}while(!sceltaMenu.equals("3"));
				
			
			
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