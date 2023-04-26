package classi;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.directory.InvalidAttributesException;

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
     * 
     * Mando meno parametri in quanto ogni client è predisposto per gestire una sola asta per volta, pertanto, non risulta necessario inviare dati relativi all'asta per cui è riferita l'offerta
     */
    public Offerta(float offerta, Cliente offerente, Timestamp dataOraOfferta){
        super();
        this.id_offerta = -1;    // In questo momento l'id non è necessario
        this.DataOraOfferta = dataOraOfferta;
        this.offerta = offerta;
        this.offerente = offerente;
    }
	
	public Offerta(String toString) {
		/*
		 	"id_offerta:"+id_offerta+",Data e ora Offerta:"+DataOraOfferta+",Offerta:"+offerta
				+",offerente:"+offerente.getUSERNAME()+",Asta:"+asta.getId_asta();
		 */
		
		String[] attributi = toString.split(",");	// Divido per il simbolo che divide i vari attributi
		
		// A questo punto itero sugli attributi
		
		for (int i = 0; i < attributi.length; i++) {
			// Leggo i valori dividendo la stringa con i :
			
			String[] attributo = attributi[i].split(":");
				// 0: nome dell'attributo
				// 1: valore dell'attributo
			
			switch(attributo[0]) {
				case "id_offerta":
					this.id_offerta = Integer.parseInt(attributo[1]);
					break;
					
				case "Data e ora Offerta":

					// Essendo che il toString del timestamp ritorna un dato di questo tipo: 2023-04-26 18 va trasformato in modo che possa essere riutilizzato

					SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd HH");

					try {
						Date parsedDate = data.parse(attributo[1]);
						this.DataOraOfferta = new Timestamp(parsedDate.getTime());
					} catch (Exception e) {
						e.printStackTrace();
					}

					break;
					
				case "Offerta":
					this.offerta = Float.parseFloat(attributo[1]);
					break;
					
				case "offerente":
					this.offerente = new Cliente(attributo[1]);
					break;
					
				default:
					break;
			}
		}
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
		return "id_offerta:"+id_offerta+",Data e ora Offerta:"+DataOraOfferta+",Offerta:"+offerta
				+",offerente:"+offerente.getUSERNAME()+",Asta:"+asta.getId_asta();
	}

	public static void main(String[] args) {
		// public Offerta(int id_offerta, Timestamp dataOraOfferta, float offerta, Cliente offerente, Asta asta) {
		// public Cliente(String USERNAME, String nome, String cognome, Date data_nascita, String residenza,
				// String password, String email) {
		// public Asta(int id_asta, Prodotto prodotto, String ip) {
		// public Prodotto(int ID_PRODOTTO, String nome, String descrizione, float prezzoDiBase, boolean venduto,
        // Cliente venditore, Timestamp dataOra_aggiunta, String categoria)
		try {
			Offerta o = new Offerta(-1, new Timestamp(System.currentTimeMillis()), 1000, new Cliente("username", "", "", null, "", "", ""), new Asta(-1, new Prodotto(-1, "PRODOTTO", "", 10000, false, null, null, ""), ""));
			
			Offerta p = new Offerta(o.toString());
			
			System.out.println(p.toString());
			
		} catch (InvalidAttributesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}