package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.Timestamp;
import java.util.Scanner;

import classi.Offerta;

// Per scelta progettuale input e invio dei dati viene fatto tutto assieme per velocizzare. Questo perchè una volta terminato l'input deve essere mandato immediatamente

/**
 * <b>Classe Input</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class Input extends Thread {
	private AstaDataClient datiAsta;
	private MulticastSocket socket;
	private InetAddress inetAddress;
	private int multicastPort;
	
	Scanner t;
	
	/**
	 * Costruttore della classe Input
	 * 
	 * @param datiAsta -> Asta per cui vengono prese le offerte
	 * @param socket -> Socket su cui mandare i dati
	 * @param inetAddress -> Indirizzo su cui mandare i dati
	 */
	public Input(AstaDataClient datiAsta, MulticastSocket socket, InetAddress inetAddress) {
		this.datiAsta = datiAsta;
		this.socket = socket;
		this.inetAddress = inetAddress;
		this.multicastPort = 5550;	// Porta su cui mandare l'offerta
	}
	
	/**
	 * Metodo run classe Input
	 */
	public void run() {
		
		this.t = new Scanner(System.in);
		
		// SMETTO DI CICLARE QUANDO L'ASTA TERMINA
		while(true) {
			String input = this.t.nextLine();
			
			if (!this.datiAsta.isEnded()) {
				int offerta = Integer.parseInt(input);
				
				// Invio il pacchetto
				
				// public Offerta(float offerta, Cliente offerente, Asta asta, Timestamp dataOraOfferta){
				byte[] datiPacchetto = new Offerta(offerta, this.datiAsta.getMeStesso(),new Timestamp(System.currentTimeMillis())).datiDaInviare().getBytes();
					// da vedere il timestamp
				
				DatagramPacket pacchetto = new DatagramPacket(datiPacchetto, datiPacchetto.length, this.inetAddress, multicastPort);
				
				try {
                    socket.send(pacchetto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
			else	// L'asta è terminata, non è necessario continuare il ciclo
				break;
		}
	}
}