package server;

import java.util.LinkedList;

import classi.*;

/**
 * <b>Classe GestisciAste</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class GestisciAste extends Thread{
	
    private Resources resources;
    private int id;
    
    /**
     * Costruttore classe GestisciAste
     * @param resources -> risorse passate come parametro
     */
    public GestisciAste(Resources resources) {
        this.resources=resources;
        this.id = resources.getIdUltimaAsta()+1;
    }
    
    /**
     * Metodo run della classe GestisciAste
     */
    @Override
    public void run() {
    	super.run();
    	System.out.println("Creazione aste...");
    	
    	LinkedList<Prodotto> prodotti = resources.getProdotti();
    	
    	while(resources.getIndirizziMulticast().size() > 0) {
    		Asta asta = new Asta(id, prodotti.get(id < 1000 ? id : id%1000), 
    				resources.getIndirizziMulticast().remove(0));
    		
    		ThreadAstaServer threadAsta = new ThreadAstaServer(new AstaDataServer(asta), resources);
    		//resources.addToThreadAstaServer(threadAsta);
    		threadAsta.start();
    		
    		resources.addActiveAsta(asta);
    		id++;
    	}
    	if(resources.getCurrentGambits().size() == 5)
    		System.out.println("Creazione aste avventua con successo");
    	
    }
    
}
