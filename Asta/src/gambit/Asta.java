package gambit;

import java.sql.Date;

public class Asta {

	private int id_asta;
	private Date dataOra_inizio;
	private Date dataOra_fine;	
	private String ip;
	private Cliente vincitore;
	private Prodotto prodotto;
	
	private float prezzoCorrente;
	
	public Asta(int id_asta, Prodotto prodotto, String ip) {
		super();
		this.id_asta = id_asta;
		this.prodotto = prodotto;
		this.prezzoCorrente=prodotto.getPrezzoDiBase();
		this.ip = ip;
	}
	
	public Asta(int id_asta, Date dataOra_inizio, Date dataOra_fine, String ip, Cliente vincitore,
			Prodotto prodotto) {
		super();
		this.id_asta = id_asta;
		this.dataOra_inizio = dataOra_inizio;
		this.dataOra_fine = dataOra_fine;
		this.ip = ip;
		this.vincitore = vincitore;
		this.prodotto = prodotto;
		this.prezzoCorrente=prodotto.getPrezzoDiBase();
	}

	public int getId_asta() {
		return id_asta;
	}

	public Date getDataOra_inizio() {
		return dataOra_inizio;
	}

	public Date getDataOra_fine() {
		return dataOra_fine;
	}
	
	public void setDataOra_inizio(Date dataOra_inizio) {
		this.dataOra_inizio = dataOra_inizio;
	}

	public void setDataOra_fine(Date dataOra_fine) {
		this.dataOra_fine = dataOra_fine;
	}

	public Cliente getVincitore() {
		return vincitore;
	}
	
	public void setVincitore(Cliente vincitore) {
		this.vincitore = vincitore;
	}

	public Prodotto getProdotto() {
		return prodotto;
	}

	public String getIp() {
		return ip;
	}
	
	//GESTIONE PREZZO
	public synchronized float getPrezzoCorrente() {
		return prezzoCorrente;
	}
	
	public synchronized void aggiornaPrezzoCorrente(int prezzo) {
		if(prezzo > prezzoCorrente) {
			prezzoCorrente = prezzo;
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Id_asta: "+id_asta+"\tIp: "+ip;
	}

}
