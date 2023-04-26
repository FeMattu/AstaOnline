package client;

import classi.Asta;
import classi.Cliente;
import classi.Offerta;

public class AstaData {
	private Asta asta;
	private Cliente compratoreUltimaOfferta;
	private Offerta ultimaOfferta;
	
	private boolean isEnded;
	
	public AstaData(Asta asta) {
		this.asta = asta;
		this.compratoreUltimaOfferta = null;
		this.ultimaOfferta = null;
		this.isEnded = false;
	}
	
	public Asta getAsta() {
        return asta;
    }
	
	public Cliente getCliente() {
		return this.compratoreUltimaOfferta;
	}
	
	public Offerta getOffertaMaggiore() {
        return this.ultimaOfferta;
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