package classi;

import java.sql.Date;
import java.sql.Timestamp;

public class Offerta {
	private int id_offerta;
	private Timestamp DataOraOfferta;
	private float offerta;
	private Cliente offerente;
	private Asta asta;
		
	public Offerta(int id_offerta, Timestamp dataOraOfferta, float offerta, Cliente offerente, Asta asta) {
		super();
		this.id_offerta = id_offerta;
		DataOraOfferta = dataOraOfferta;
		this.offerta = offerta;
		this.offerente = offerente;
		this.asta = asta;
	}
	
	/**
	 * Costruttore di offerta per inviare i dati
	 * 
	 * @param offerta Valore dell'offerta
	 * @param offerente Chi ha fatto l'offerta che verrà inviata
	 * @param asta Asta di riferimento
	 * @param dataOraOfferta Momento in cui è stata fatta l'offerta
	 */
	public Offerta(float offerta, Cliente offerente, Asta asta, Timestamp dataOraOfferta){
		super();
		this.id_offerta = -1;	// In questo momento l'id non è necessario
		this.DataOraOfferta = dataOraOfferta;
		this.offerta = offerta;
		this.offerente = offerente;
		this.asta = asta;
	}

	public int getId_offerta() {
		return id_offerta;
	}

	public Timestamp getDataOraOfferta() {
		return DataOraOfferta;
	}

	public float getOfferta() {
		return offerta;
	}

	public Cliente getOfferente() {
		return offerente;
	}

	public Asta getAsta() {
		return asta;
	}
	
	@Override
	public String toString() {
		return "id_offerta: "+id_offerta+"\tData e ora Offerta: "+DataOraOfferta+"\tOfferta: "+offerta
				+"\tofferente: "+offerente.getUSERNAME()+"\tAsta: "+asta.getId_asta();
	}
}