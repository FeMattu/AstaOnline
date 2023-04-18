package gambit;

import java.sql.Date;
import java.time.LocalDate;

public class Cliente {
	private final String USERNAME;
	private String nome;
	private String cognome;
	private Date data_nascita;
	private String residenza;
	private String password;
	private String email;
	
	/**
	 * fa nascere una persona (quello che direbbe il loco AH AH AH, per il gianne
	 * )
	 * @param CODICE_FISCALE Codice fiscale del cliente
	 * @param nome nome del cliente
	 * @param cognome cognome del cliente
	 * @param data_nascita data di nascita del cliente
	 * @param residenza residenza del cliente
	 * @param email E-mail del cliente
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
