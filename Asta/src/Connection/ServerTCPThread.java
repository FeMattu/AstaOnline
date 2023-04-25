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
import java.util.List;

import gambit.Asta;
import gambit.Cliente;
import gambit.Prodotto;
import gambit.Resources;

/**
 * <b>ServerTCPThread class</b>
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
	 * <b>ServerTCPThread constructor</b>
	 * 
	 * @param socket    -> socket used for the connection
	 * @param resources -> resources istance
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
	 * <b>Run method for ServerTCPThread class</b>
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
					int astaScelta = Integer.parseInt(reader.readLine());
					if (validazioneAstaScelta(astaScelta, aste)) {
						System.out.println("---\nClient ha accesso all'asta.");
						writer.writeBytes("Hai effettuato l'accesso all'asta\n");
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
	 * invia le informazioni sulle aste attive al client con il seguente formato:
	 * id_asta:nomeProdotto:categoria:prezzoCorrente;
	 * 
	 * @return
	 */
	private boolean sendAste() {
		List<Asta> currentGambits = resources.getCurrentGambits();
		try {
			for (Asta asta : currentGambits) {
				Prodotto p = asta.getProdotto();
				writer.writeBytes(asta.getId_asta() + ":" + p.getNome() + ":" + p.getCategoria() + ":"
						+ asta.getPrezzoCorrente());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private boolean validazioneAstaScelta(int id, List<Asta> aste) {
		for (int i = 0; i < aste.size(); i++) {
			if (aste.get(i).getId_asta() == id) {
				return true;
			}
		}
		return false;
	}

}