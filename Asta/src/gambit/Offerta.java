package gambit;

import java.sql.Date;

public class Offerta {
	private int id_offerta;
	private Date DataOraOfferta;
	private float offerta;
	private Cliente offerente;
	private Asta asta;
		
	public Offerta(int id_offerta, Date dataOraOfferta, float offerta, Cliente offerente, Asta asta) {
		super();
		this.id_offerta = id_offerta;
		DataOraOfferta = dataOraOfferta;
		this.offerta = offerta;
		this.offerente = offerente;
		this.asta = asta;
	}

	public int getId_offerta() {
		return id_offerta;
	}

	public Date getDataOraOfferta() {
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
