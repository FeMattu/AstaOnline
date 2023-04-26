package client;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import classi.Cliente;

/**
 * <b>Classe Client</b>
 * 
 * @author <i>Federico Mattucci<br>
 *         Tommaso Giannecchini<br>
 *         Federico Massanti<br>
 *         Lorenzo Rapposelli<br>
 *         Giacomo Diridoni</i>
 *
 */
public class Client {

	private static Socket socket;
	private static BufferedReader reader;
	private static DataOutputStream writer;
	private static Scanner scanner = new Scanner(System.in);
	private static String username;
	private static String password; 

	/**
	 * Metodo main della classe Client
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			socket = new Socket("localhost", 5234);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Connessionu al server...");
		// Se non viene effettuato l'accesso al server il programma termina
		if (!accessoServer()) {
			try {
				System.out.println("Connessione al server non effettuata");
				reader.close();
				writer.close();
				socket.close();
				scanner.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		System.out.println("Accesso effettuato");
		int sceltaMenu = -1;
		do {
			System.out.println("---\n---\n---");
			sceltaMenu = sceltaMenu();
			try {
				writer.writeBytes(sceltaMenu + "\n");
				switch (sceltaMenu) {
				case 1:
					// Scegliendo questa, passiamo alla scelta dell'asta alla quale prendere parte
					partecipaAdUnAsta();
					System.out.println("---\n" + reader.readLine());
					break;
				case 2:
					// Scegliendo questa, inseriamo un prodotto che verrà messo all'asta
					aggiuntaProdotto();
					break;
				case 0:
					System.out.println("Termine del programma. Disconnessione in corso...");
					reader.close();
					writer.close();
					socket.close();
					System.out.println("Disconnessione eseguita con successo.");
					break;
				default:
					System.out.println("Scelta non valida, reinserire.");
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (sceltaMenu == 2);

		scanner.close();
	}

	/**
	 * Metodo per validare accesso al server
	 * @return true se esegui accesso, false altrimenti
	 */
	private static boolean accessoServer() {
		int scelta = -1;
		while (true) {
			System.out.println("1 -> Accedi");
			System.out.println("2 -> registrati");
			System.out.println("0 -> exit");
			System.out.print("Scelta: ");
			scelta = scanner.nextInt();
			switch (scelta) {
			case 1: {
				System.out.print("Username: ");
				username = scanner.next();
				System.out.print("Password: ");
				password = scanner.next();

				try {
					writer.writeBytes(username + ":" + password + "\n");
					if (reader.readLine().equals("OK")) {
						return true;
					} else {
						System.out.println("Username o psw non corretti, riprovare");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			case 2: {
				String nome, cognome, residenza, email = null;
				System.out.print("Username: ");
				username = scanner.next();

				System.out.print("Nome: ");
				nome = scanner.next();

				System.out.print("Cognome: ");
				cognome = scanner.next();

				System.out.print("E-mail: ");
				email = scanner.next();
				scanner.nextLine();
				System.out.print("Residenza: ");
				residenza = scanner.nextLine();

				System.out.print("Password (senza spazi): ");
				password = scanner.next();

				try {
					writer.writeBytes(username + ":" + password + ":" + nome + ":" + cognome + ":" + email + ":"
							+ residenza + "\n");
					if (reader.readLine().equals("OK")) {
						return true;
					} else {
						System.out.println("Registrazione fallita, riprovare");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			case 0: {
				return false;
			}
			default:
				System.out.println("Scelta immessa non valida, reinserire");
			}
		}
	}

	/**
	 * Stampa del menu per la scelta di cosa fare
	 */
	private static void menu() {
		System.out.println("Menù:");
		System.out.println("1 -> Partecipa ad un'asta");
		System.out.println("2 -> Aggiungi un prodotto");
		System.out.println("0 -> Esci");
		System.out.print("Scelta: ");
	}

	/**
	 * Metodo per la presa in input della scelta nel menù
	 * @return numero intero rappresentante la scelta fatta
	 */
	private static int sceltaMenu() {
		menu();
		int s = scanner.nextInt();
		return s;
	}
	
	/**
	 * Metodo per partecipare ad un'asta -> valori passati a ServerTCPThread
	 * @throws IOException -> Eccezioni lanciate
	 */
	private static void partecipaAdUnAsta() throws IOException {
		System.out.println("---\nElenco aste a cui puoi partecipare:");
		String asta = reader.readLine();
		
		while (!asta.equals("OK")) {
			System.out.println(asta);
			asta = reader.readLine();
		}
		
		System.out.print("A quale asta vuoi partecipare (ID)?: ");
		int idAstaScelta = scanner.nextInt();
		writer.writeBytes(idAstaScelta + "\n");
		
		
		// Riprendo il pacchetto
		
		String ip = reader.readLine();
		
		String[] dati = ip.split(":");
			// 0 -> ip
			// 1 -> prezzo di base
		
		AstaDataClient data = new AstaDataClient(Float.parseFloat(dati[1]), dati[0]);
        data.setMeStesso(new Cliente(Client.username));

        ConnettoreAsta c = new ConnettoreAsta(data);
		
		c.start();
		
		try {
			c.join();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//receiveUDPMessage("224.0.0.5", 5550);
	}

	/**
	 * Metodo per aggiungere un prodotto al DB -> passaggio dei parametri a ServerTCPThread
	 * @throws IOException -> Eccezioni lanciate
	 */
	private static void aggiuntaProdotto() throws IOException {
		String nome, desc, categoria;
		float prezDiBase;
		System.out.println("---\nInserisci un prodotto:\n---");

		System.out.print("Nome: ");
		scanner.nextLine();
		nome = scanner.nextLine();
		// System.out.println(nome);

		System.out.print("Descrizione: ");
		desc = scanner.nextLine();
		// System.out.println(desc);

		System.out.print("Categoria: ");
		categoria = scanner.next();
		// System.out.println(categoria);

		System.out.print("Prezzo di Base: ");
		prezDiBase = scanner.nextFloat();
		// System.out.println(prezDiBase);

		writer.writeBytes(nome + ":" + desc + ":" + categoria + ":" + prezDiBase + "\n");
		System.out.println(reader.readLine());
	}

}