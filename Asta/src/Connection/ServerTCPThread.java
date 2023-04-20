package Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import gambit.Asta;
import gambit.Cliente;
import gambit.Prodotto;
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
    	String access;
		try {
			access = reader.readLine();
			if(validazioneAccesso(access)) {
				writer.writeBytes("OK\n");
			}else {
				writer.writeBytes("Nan\n");
				reader.close();
				writer.close();
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    private boolean validazioneAccesso(String access) {
    	String[] parametri = access.split(":");
    	if(parametri.length == 2) {
    		if(resources.getCliente(parametri[0]).getPassword().equals(parametri[1])) {
    			return true;
    		}
    	}else if(parametri.length > 2){
    		return resources.addCliente(new Cliente(
	    				parametri[0], 
	    				parametri[2], 
	    				parametri[3],
	    				Date.valueOf(LocalDate.now()),
	    				parametri[5],
	    				parametri[1], 
	    				parametri[4]));
    	}
    	
    	return false;
    }
    
    /**
     * invia le informazioni sulle aste attive al client con il seguente formato:
     * id_asta:nomeProdotto:categoria:prezzoCorrente;
     * @return
     */
    private boolean sendAste() {
    	List<Asta> currentGambits = resources.getCurrentGambits();
    	
    	try {
    		for (Asta asta : currentGambits) {
    			Prodotto p = asta.getProdotto();
				writer.writeBytes(asta.getId_asta()+":"
						+ p.getNome()+":"
						+ p.getCategoria()+":"
						+ asta.getPrezzoCorrente());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	return false;
    }
    
}