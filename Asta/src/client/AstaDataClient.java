package client;

import classi.Cliente;
import classi.Offerta;

/**
 * <b>Classe AstaDataClient</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class AstaDataClient {
    private String ip;
    private float prezzoDiBase;
    private Offerta ultimaOfferta;

    private Cliente meStesso;

    private boolean isEnded;

    /**
     * Costruttore AstaDataClient
     * @param prezzoDiBase -> prezzo di base dell'asta 
     * @param ip -> IP dell'asta
     */
    public AstaDataClient(float prezzoDiBase, String ip) {
        this.prezzoDiBase = prezzoDiBase;
        this.ultimaOfferta = new Offerta(this.prezzoDiBase, null, null);;
        this.isEnded = false;
        this.meStesso = null;
    }

    /*GETTERS AND SETTERS*/
    
    public void setMeStesso(Cliente meStesso) {
        this.meStesso = meStesso;
    }
    
    public Cliente getMeStesso() {
        return meStesso;
    }

    public Offerta getOffertaMaggiore() {
        return this.ultimaOfferta;
    }

    public void setOffertaMaggiore(Offerta ultimaOfferta) {
        synchronized (this.ultimaOfferta) {

            // Controllo se l'offerta che mi viene passata è più alta rispetto all'ultima registrata, se si la registro, altrimenti non la registro

            if (this.getOffertaMaggiore().getOfferta() < ultimaOfferta.getOfferta())
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