package Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

import gambit.Cliente;

/**
 * <b>Client class</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */
public class Client {
	
	private static Socket socket;
	private static BufferedReader reader;
	private static DataOutputStream writer;
	private static Scanner scanner = new Scanner(System.in);
	private static String username;
	private static String password;

	public static void main(String[] args) throws Exception {
		try {
			socket = new Socket("localhost", 5000);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		System.out.println("Connessione al server...");
		//Se non viene effettuato l'accesso al server il programma termina
		if(!accessoServer()){
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
		
		System.out.println("---\n---\n---");
		int scelta = sceltaMenu();
		writer.writeBytes(scelta+"\n");
		
		switch (scelta) {
		case 1:
			//Scegliendo questa, passiamo alla scelta dell'asta alla quale prendere parte
			System.out.println("---\nElenco aste a cui puoi partecipare:");
			String asta = reader.readLine();
			while(!asta.equals("OK")) {
				System.out.println(asta);
				asta = reader.readLine();
			}
			break;
		case 2:
			//Scegliendo questa, inseriamo un prodotto che verrà messo all'asta
			String nome,desc,categoria;
			float prezDiBase;
			System.out.println("---\nInserisci un prodotto:\n---");
			System.out.print("Nome: ");
			scanner.nextLine();
			nome = scanner.nextLine();
			System.out.print("Descrizione: ");
			scanner.nextLine();
			desc = scanner.nextLine();
			System.out.print("Categoria: ");
			categoria = scanner.nextLine();
			System.out.print("Prezzo di Base: ");
			prezDiBase = scanner.nextFloat();
			
			break;
		case 0:
			break;
		default:
			System.out.println("Scelta non valida, reinserire.");
			break;
		}
		
		reader.close();
		writer.close();
		socket.close();
		scanner.close();
	}
	
	/**
	 * permette al client di accedere o registrarsi alla "piattaforma"
	 * @return <strong>true</strong> se l'accesso è stato effettuato, <strong>false</strong> altrimenti
	 */
	private static boolean accessoServer() {
		int scelta = -1;
		while(true) {
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
					writer.writeBytes(username+":"+password+"\n");
					if(reader.readLine().equals("OK")) {
						return true;
					}else {
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
					writer.writeBytes(username+":"+password+":"+nome+":"+cognome+":"+email+":"+residenza+"\n");
					if(reader.readLine().equals("OK")) {
						return true;
					}else {
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
	
	private static void menu(){
		System.out.println("\nMenù:");
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
	

}