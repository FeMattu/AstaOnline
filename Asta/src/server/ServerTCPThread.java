package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.List;

import classi.Asta;
import classi.Cliente;
import classi.Prodotto;

/**
 * <b>Classe ServerTCPThread</b>
 * 
 * @author <i>Federico Mattucci<br>
 *         Tommaso Giannecchini<br>
 *         Federico Massanti<br>
 *         Lorenzo Rapposelli<br>
 *         Giacomo Diridoni</i>
 *
 */
public class ServerTCPThread extends Thread {

	private Socket socket;
	private BufferedReader reader;
	private DataOutputStream writer;
	private Resources resources;
	static boolean inviato;
	private Cliente utente;

	/**
	 * Costruttore classe ServerTCPThread
	 * 
	 * @param socket -> socket passata per la connessione
	 * @param resources -> risorse passate come parametro
	 */
	public ServerTCPThread(Socket socket, Resources resources) {
		this.socket = socket;
		this.resources = resources;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Metodo run della classe ServerTCPThread
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String access;
		try {
			access = reader.readLine();
			if (validazioneAccesso(access)) {
				writer.writeBytes("OK\n");
			} else {
				writer.writeBytes("Nan\n");
				reader.close();
				writer.close();
				socket.close();
			}
			String choice = "";

			do {
				choice = reader.readLine();
				switch (choice) {
				case "1":
					List<Asta> aste = resources.getCurrentGambits();
					for (int i = 0; i < aste.size(); i++) {
						writer.writeBytes(aste.get(i).toString() + "\n");
					}
					writer.writeBytes("OK\n");
					// Da qui, gestione della scelta dell'asta alla quale si vuole partecipare
					int idAstaScelta = Integer.parseInt(reader.readLine());
					if (validazioneAstaScelta(idAstaScelta, aste)) {
						System.out.println("---\nClient ha accesso all'asta.");
						Asta a = getAstaScelta(idAstaScelta, aste);
						//ThreadAstaServer t = resources.getThreadByAsta(a);
						//t.start();
                        writer.writeBytes(a.getIp()+":"+a.getProdotto().getPrezzoDiBase()+"\n");
						
						//MANDARE LE INFO A ThreadAstaClient.java per la connessione con socket multicast
						
						
					} else {
						System.out.println("---\nClient non ha effettuato accesso all'asta.");
						writer.writeBytes("Non hai effettuato l'accesso all'asta\n");
					}
					break;
				case "2":
					String[] propProdotto = reader.readLine().split(":");
					Prodotto p = new Prodotto(resources.getIdUltimoProdotto() + 1, propProdotto[0], propProdotto[1],
							Float.parseFloat(propProdotto[3]), false, utente, Timestamp.valueOf(LocalDateTime.now()),
							propProdotto[2]);
					if (resources.addProdotto(p)) {
						writer.writeBytes("Prodotto inserito\n");
					} else {
						writer.writeBytes("Prodotto non inserito\n");
					}
					break;
				case "0":
					System.out.println("Client non ha richiesto nulla.");
					break;
				default:
					System.out.println("Scelta invalida, reinserisci.");
					break;
				}
			} while (choice.equals("2"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Metodo per la validazione dell'accesso
	 * @param access -> Stringa identificativa di colui che vuole accedere
	 * @return -> true se loggato, false altrimenti
	 */
	private boolean validazioneAccesso(String access) {
		String[] parametri = access.split(":");
		if (parametri.length == 2) {
			utente = resources.getCliente(parametri[0]);
			if (utente == null)
				return false;
			if (utente.getPassword().equals(parametri[1])) {
				return true;
			}
		} else if (parametri.length > 2) {
			utente = new Cliente(parametri[0], parametri[2], parametri[3], Date.valueOf(LocalDate.now()), parametri[5],
					parametri[1], parametri[4]);
			return resources.addCliente(utente);
		}

		return false;
	}

	/**
	 * Metodo per validazione dell'indice dell'asta scelta
	 * @param id -> ID scelto in input
	 * @param aste -> Lista di aste correnti
	 * @return -> true se esiste una corrispondenza, false altrimenti
	 */
	private boolean validazioneAstaScelta(int id, List<Asta> aste) {
		for (int i = 0; i < aste.size(); i++) {
			if (aste.get(i).getId_asta() == id) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metodo che ritorna l'asta con ID quello preso in input
	 * @param id -> ID preso in input
	 * @param aste -> Lista di aste correnti dalla quale prelevare l'asta da ritornare
	 * @return -> istanza di classe Asta alla quale vogliamo collegarci
	 */
	private Asta getAstaScelta(int id, List<Asta> aste) {
		for (int i = 0; i < aste.size(); i++) {
			if (aste.get(i).getId_asta() == id) {
				return aste.get(i);
			}
		}
		return null;
	}
	
}