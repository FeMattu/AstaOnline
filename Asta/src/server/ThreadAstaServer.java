package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import classi.Asta;
import classi.Offerta;

/**
 * <b>Classe ThreadAstaServer</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class ThreadAstaServer extends Thread{

    private Resources resources;
    private Asta asta;
    private AstaDataServer astaData;
    private TimerAsta timer;
    
    /**
     * Costruttore ThreadAstaServer
     * @param asta -> asta della quale viene creato il thread
     * @param resources -> risorse alle quali si connette
     */
    
    // TODO USARE QUESTO COSTRUTTORE QUA INVECE CHE DI QUELLO SOPRA
    public ThreadAstaServer(AstaDataServer astaDataServer, Resources resources){
        this.astaData = astaDataServer;
        this.resources = resources;
        this.timer = new TimerAsta(this.astaData);
    }
    
    /**
     * Metodo run della classe ThreadAstaServer
     */
    public void run(){
        super.run();
        //settaggio data e ora inizio asta       
        //asta.setDataOra_inizio(Timestamp.valueOf(LocalDateTime.now()));
        
		MulticastSocket socket;
		InetAddress group;
		DatagramPacket packet;
		timer.start();
		try {
			socket = new MulticastSocket(5550);
			group = InetAddress.getByName("224.0.0.5");
			socket.joinGroup(group);
			
			System.out.println("Aspettando offerte dai compratori...");
			
			// Alla prima offerta inizia l'asta
			
			// Finchè l'asta non è finita
			while (!this.astaData.isEnded()) {
				// Deve ricevere il pacchetto
				
				byte[] pacchetto = new byte[1024];	// SE STONA GUARDARE QUA
				packet = new DatagramPacket(pacchetto, pacchetto.length);
				
				try {
					socket.receive(packet);
					
					// All'interno viene passato un toString di offerta
					
				}
				catch (IOException e) {
	                e.printStackTrace();
	            }
				
				if (this.astaData.isEnded()) {
					break;
				}
				else {
                    // Non è finita quindi posso analizzare quanto mi è arrivato

					resources.addAstaIntoDB(this.astaData.getAsta());
					System.out.println("Asta terminata e aggiunta nel db.");
					String input = new String(packet.getData()).substring(0, packet.getLength());
                    Offerta o = new Offerta(input);

                    if (o.getOfferta() > this.astaData.getOffertaMaggiore().getOfferta()) {
                        // L'offerta è valida

                        this.astaData.setOffertaMaggiore(o);
                    }

                    // Comincio ad aspettare
                    /*this.astaData.setWaiting(true);
                    System.out.println("Waiting:" +this.astaData.isWaiting());*/
                }
				
			}
			
			// L'asta è finita, dichiaro il vincitore
			
			
			byte[] msg = ("Il vincitore è:" + this.astaData.getCliente().getUSERNAME()).getBytes();
			packet = new DatagramPacket(msg, msg.length, group, 5550);
			socket.send(packet);
		
			// A questo punto posso uscire dal gruppo e chiudere l'asta
			socket.leaveGroup(group);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //fine asta
        //aggiunta data e ora finea asta
        //asta.setDataOra_fine(Timestamp.valueOf(LocalDateTime.now()));
        //resources.getCurrentGambits().remove(asta);
        //resources.addAstaIntoDB(asta);
    }
    
    /*GETTERS AND SETTERS*/
    
    public Asta getAsta() {
		return asta;
	}
    
    public Resources getResources() {
		return resources;
	}
    
    /**
     * Metodo per mandare messaggi UDP
     * @param message -> messaggio da mandare
     * @param ipAddress -> IP al quale mandarlo in Multicast
     * @param port -> Porta sulla quale connettersi
     * @throws IOException -> Eccezioni lanciate
     */
    private static void sendUDPMessage(String message, String ipAddress, int port) throws IOException {
    	DatagramSocket socket = new DatagramSocket();
		InetAddress group = InetAddress.getByName("224.0.0.5");
		byte[] msg = message.getBytes();
		DatagramPacket packet = new DatagramPacket(msg, msg.length, group, 5550);
		socket.send(packet);
		socket.close();
	}
    
}