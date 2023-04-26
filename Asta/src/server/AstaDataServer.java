package server;

import classi.Asta;
import classi.Cliente;
import classi.Offerta;

public class AstaDataServer {
	private Asta asta;
	private Offerta ultimaOfferta;
	
	private boolean isEnded;
	private boolean waiting;
	
	public AstaDataServer(Asta asta) {
		this.asta = asta;
		this.ultimaOfferta = null;
		this.isEnded = false;
		this.waiting = false;
	}
	
	public Asta getAsta() {
        return asta;
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
        return waiting;
    }
	
	public void setOffertaMaggiore(Offerta ultimaOfferta) {
        synchronized (this.ultimaOfferta) {
        	// Controllo se l'offerta che mi viene passata è più alta rispetto all'ultima registrata, se si la registro, altrimenti non la registro
        	if (this.ultimaOfferta.getOfferta() < ultimaOfferta.getOfferta())
        		this.ultimaOfferta = ultimaOfferta;
        }
    }
	
	public void endAsta() {
		this.isEnded = true;
	}
	
	public boolean isEnded() {
		return this.isEnded;
	}
	
}