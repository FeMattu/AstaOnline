package classi;

import java.sql.Date;
import java.sql.Timestamp;

import javax.naming.directory.InvalidAttributesException;

public class Prodotto {

    private final int ID_PRODOTTO;
    private String nome;
    private String descrizione;
    private float prezzoDiBase;
    private boolean venduto;
    private Timestamp dataOra_aggiunta;
    private String categoria;
    private Cliente venditore;
    
    private boolean inVendita = false;
    /**
     * Crea un prodotto
     * @param iD_PRODOTTO
     * @param nome
     * @param descrizione
     * @param prezzoDiBase
     * @param venduto
     * @param dataOra_aggiunta
     * @throws InvalidAttributesException
     */
    public Prodotto(int ID_PRODOTTO, String nome, String descrizione, float prezzoDiBase, boolean venduto,
            Cliente venditore, Timestamp dataOra_aggiunta, String categoria) throws InvalidAttributesException {
        this.ID_PRODOTTO = ID_PRODOTTO;
        this.nome = nome;
        this.descrizione = descrizione;
        if(prezzoDiBase > 0 && prezzoDiBase < 100000000)
        	this.prezzoDiBase = prezzoDiBase;
        else throw new InvalidAttributesException("Attribute: PrezzoDiBase non valido; questo deve essere >0 && <100'000'000");
        this.venduto = venduto;
        this.venditore=venditore;
        this.dataOra_aggiunta = dataOra_aggiunta;
        this.categoria=categoria;
    }

    public int getID_PRODOTTO() {
        return ID_PRODOTTO;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
 
    public float getPrezzoDiBase() {  
        return prezzoDiBase;
    }

    public boolean isVenduto() { 
        return venduto;
    }

    public void setVenduto(boolean venduto) {
        this.venduto = venduto;
    }

    public Timestamp getDataOra_aggiunta() {
        return dataOra_aggiunta;
    }

	public String getCategoria() {
		return categoria;
	} 
	
	public Cliente getVenditore() {
		return venditore;
	}
	
	public synchronized boolean IsInVendita() {
		return inVendita;
	}
	
	public synchronized void setVendita(boolean inVendita) {
		this.inVendita=inVendita;
	}
     
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ID: "+ID_PRODOTTO+"\tnome: "+nome+"\tPrezzo di base: "+prezzoDiBase+"\tCategoria: "+categoria;
	}
	
	
}
