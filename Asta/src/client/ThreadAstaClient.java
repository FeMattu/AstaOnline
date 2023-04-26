package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import classi.Offerta;
import classi.Cliente;

public class ThreadAstaClient extends Thread{

	AstaDataClient astaDataClient;
	Cliente cliente;
	
    ThreadAstaClient(AstaDataClient astaDataClient, Cliente cliente) {
        // TODO Auto-generated constructor stub
    	this.astaDataClient = astaDataClient;
    	this.cliente = cliente;
    }

    public void run() {
        try {
            String AddressIp = "224.0.0.5";
            int port = 5550;
            
            byte[] buffer = new byte[1024];
            MulticastSocket socket = new MulticastSocket(5550);
            InetAddress group = InetAddress.getByName("224.0.0.5");
            socket.joinGroup(group);
            
            // Itero finchè l'asta non è finita
            while (!this.astaDataClient.isEnded()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                
                // ho ricevuto l'offerta/la comunicazione di chi è il vincitore
                
                if (msg.contains("vincitore")) {
                	// stato mandato il vincitore
                	
                	String[] vincitore = msg.split(":");
                		// Ad indice 1 ho il vincitore
                	
                	if (vincitore[1].equals(this.cliente.getUSERNAME())) {
                		// HO VINTO IO
                		System.out.println("HAI ACQUISTATO IL PRODOTTO");
                	}
                	else {
                		System.out.println("NON HAI ACQUISTATO IL PRODOTTO");
                	}
                }
                else {
                	// una nuova offerta
                	
                	Offerta o = new Offerta(msg);
                	if (o.getOfferta() > this.astaDataClient.getOffertaMaggiore().getOfferta()) {
                		// L'offerta è valida
                		
                		this.astaDataClient.setOffertaMaggiore(o);
                	}
                }
            }
            
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        super.run();
    }
}