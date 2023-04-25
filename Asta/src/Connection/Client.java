package Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Scanner;

import gambit.Resources;

/**
 * <b>Client class</b>
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
					System.out.println("---\n"+reader.readLine());
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

	private static void receiveUDPMessage(String AddressIp, int port) throws IOException {
		// TODO Auto-generated method stub
		byte[] buffer = new byte[1024];
		MulticastSocket socket = new MulticastSocket(5550);
		InetAddress group = InetAddress.getByName("224.0.0.5");
		socket.joinGroup(group);
		while (true) {
			System.out.println("Waiting for multicast message...");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
			System.out.println("[Multicast UDP message received] >> " + msg);
			if ("OK".equals(msg)) {
				System.out.println("No more message. Exiting : " + msg);
				break;
			}
		}
		socket.leaveGroup(group);
		socket.close();
	}

	/**
	 * permette al client di accedere o registrarsi alla "piattaforma"
	 * 
	 * @return <strong>true</strong> se l'accesso è stato effettuato,
	 *         <strong>false</strong> altrimenti
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

	private static void menu() {
		System.out.println("Menù:");
		System.out.println("1 -> Partecipa ad un'asta");
		System.out.println("2 -> Aggiungi un prodotto");
		System.out.println("0 -> Esci");
		System.out.print("Scelta: ");
	}

	private static int sceltaMenu() {
		menu();
		int s = scanner.nextInt();
		return s;
	}

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
		receiveUDPMessage("224.0.0.5", 5550);
	}

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