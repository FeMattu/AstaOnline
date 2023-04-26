package classi;

import java.sql.Date;

/**
 * <b>Classe Cliente</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class Cliente {
	private final String USERNAME;
	private String nome;
	private String cognome;
	private Date data_nascita;
	private String residenza;
	private String password;
	private String email;
	
	/**
	 * Costruttore classe Cliente
	 * )
	 * @param CODICE_FISCALE -> Codice fiscale del cliente
	 * @param nome -> Nome del cliente
	 * @param cognome -> Cognome del cliente
	 * @param data_nascita -> Data di nascita del cliente
	 * @param residenza -> Residenza del cliente
	 * @param email -> E-mail del cliente
	 */
	public Cliente(String USERNAME, String nome, String cognome, Date data_nascita, String residenza,
			String password, String email) {
		super();
		this.USERNAME = USERNAME;
		this.nome = nome;
		this.cognome = cognome;
		this.data_nascita = data_nascita;
		this.residenza = residenza;
		this.password=password;
		this.email = email;
	}
	
	/*GETTERS AND SETTERS*/
		
	public Cliente (String USERNAME) {
		this.USERNAME = USERNAME;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public Date getData_nascita() {
		return data_nascita;
	}

	public String getResidenza() {
		return residenza;
	}
	
	@Override
	public String toString() {
		return "UserName: "+USERNAME+"\tnome: "+nome+"\tcognome: "+cognome+"\tdata di nascita: "+data_nascita.toString();
	}
	

}
