package gambit;

import java.util.LinkedList;
import Connection.ThreadAsta;


/**
 * Classe thread che decide ed avvia le aste, questo non controlla le aste,
 * ma decide solamente il prodotto da mettere all'asta ed crea l'asta apposita con i vari parametri
 * inserendola poi nelle {@link Resources}
 * @param resources classe monitor di tutte le risorse e gestore della connessione con il database.
 */
public class GestisciAste extends Thread{
	
    private Resources resources;
    private int id;
    
    /**
     * Crea un thread che decide il prodotto e lo mette all'asta.
     * @param resources classe monitor di tutte le risorse e gestore della connessione con il database.
     */
    public GestisciAste(Resources resources) {
        this.resources=resources;
        this.id = resources.getIdUltimaAsta()+1;
    }
    
    
    @Override
    public void run() {
    	super.run();
    	System.out.println("Creazione aste...");
    	
    	LinkedList<Prodotto> prodotti = resources.getProdotti();
    	
    	while(resources.getIndirizziMulticast().size() > 0) {
    		Asta asta = new Asta(id, prodotti.get(id < 1000 ? id : id%1000), 
    				resources.getIndirizziMulticast().remove(0));
    		//ThreadAsta threadAsta = new ThreadAsta(asta, resources);
    		//threadAsta.run();
    		resources.addActiveAsta(asta);
    		id++;
    	}
    	
    	if(resources.getCurrentGambits().size() == 30)
    		System.out.println("Creazione aste avventua con successo");
    	
    }
    
}
