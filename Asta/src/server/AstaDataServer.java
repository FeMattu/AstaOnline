package server;

/**
 * <b>Classe AstaDataServer</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
import classi.Asta;
import classi.Cliente;
import classi.Offerta;

public class AstaDataServer {
	private Asta asta;
	private Offerta ultimaOfferta;
	
	private boolean isEnded;
	private boolean waiting;
	private boolean nuovaOfferta;
	private Object o; //Lock per il set per la nuova offerta
	
	/**
	 * Costruttore classe AstaDataServer
	 * @param asta -> asta passata come parametro
	 */
	public AstaDataServer(Asta asta) {
		this.asta = asta;
		this.ultimaOfferta = new Offerta(asta.getProdotto().getPrezzoDiBase(), null, null);;
		this.isEnded = false;
		this.waiting = false;
		this.nuovaOfferta = false;
		this.o = new Object();
	}
	
	/*GETTERS AND SETTERS*/
	
	public Asta getAsta() {
        return this.asta;
    }
	
	public Cliente getCliente() {
		return this.ultimaOfferta.getOfferente();
	}
	
	public Offerta getOffertaMaggiore() {
        return this.ultimaOfferta;
    }
	
	public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }
	
	public boolean isWaiting() {
        return this.waiting;
    }
	
	public void setOffertaMaggiore(Offerta ultimaOfferta) {
        synchronized (this.ultimaOfferta) {
        	// Controllo se l'offerta che mi viene passata è più alta rispetto all'ultima registrata, se si la registro, altrimenti non la registro
        	if (this.ultimaOfferta.getOfferta() < ultimaOfferta.getOfferta()) {
        		this.ultimaOfferta = ultimaOfferta;
        		this.setNuovaOfferta(true);
        	}
        }
    }
	
	public boolean isNuovaOfferta() {
		return nuovaOfferta;
	}
	
	public void setNuovaOfferta(boolean nuovaOfferta) {
		synchronized(this.o) {
			this.nuovaOfferta = nuovaOfferta;
		}
	}
	
	public void endAsta() {
		this.isEnded = true;
	}
	
	public boolean isEnded() {
		return this.isEnded;
	}
	
}