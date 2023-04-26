package client;

import classi.Asta;
import classi.Cliente;
import classi.Offerta;

public class AstaDataClient {
	private String ip;
	private float prezzoDiBase;
	private Offerta ultimaOfferta;

	private Cliente meStesso;
	
	private boolean isEnded;
	
	public AstaDataClient(float prezzoDiBase, String ip) {
		this.prezzoDiBase = prezzoDiBase;
		this.ultimaOfferta = null;
		this.isEnded = false;
		this.meStesso = null;
	}
	
	public void setMeStesso(Cliente meStesso) {
		this.meStesso = meStesso;
	}
	
	/**
	 * Preleva le informazioni riguardo chi sta seguendo l'asta
	 * @return
	 */
	public Cliente getMeStesso() {
		return meStesso;
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
	
	public float getPrezzoDiBase() {
		return this.prezzoDiBase;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void endAsta() {
		this.isEnded = true;
	}
	
	public boolean isEnded() {
		return this.isEnded;
	}
	
}