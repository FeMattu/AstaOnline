package gambit;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import Connection.ThreadAsta;


/**
 * Classe thread che decide ed avvia le aste, questo non controlla le aste,
 * ma decide solamente il prodotto da mettere all'asta ed crea l'asta apposita con i vari parametri
 * inserendola poi nelle {@link Resources}
 * @param resources classe monitor di tutte le risorse e gestore della connessione con il database.
 */
public class GestisciAste extends Thread{
	
	private final int NUMERO_MASSIMO_ASTE_CONTEMPORANEE = 30; 

    private Resources resources;
    private LinkedList<Prodotto> prodotti;
    private Asta asta;
    private Prodotto prodotto;
    private int id_asta;
	public static int asteInCorso;
    

    /**
     * Crea un thread che decide il prodotto e lo mette all'asta.
     * @param resources classe monitor di tutte le risorse e gestore della connessione con il database.
     */
    public GestisciAste(Resources resources) {
        this.resources=resources;
    }

    @Override
    public void run() {
    	super.run();
    	prodotti = resources.getProdotti();
    	id_asta = resources.getIdUltimaAsta() + 1;
    	asteInCorso = 0;    	
    	
    	System.out.println("Inizio creazione delle aste...");
    	
    	while(true) {
    		while (asteInCorso <= NUMERO_MASSIMO_ASTE_CONTEMPORANEE) {
    			prodotto = prodottoDaMettereAllAsta();
    			if (prodotto != null) {
    		    	asta = creaAsta(id_asta,Date.valueOf(LocalDate.now()),prodotto, resources.getIndirizziMulticast().remove(0));
    				new ThreadAsta(asta,resources).start();
    		    	System.out.println("Asta con id " + id_asta + " e con prodotto " + prodotto + " creata con successo.");
    				id_asta++;
        		    aggiornaAsteInCorso(true);
    			}
    		}
    	}
    }
    
    
    /**
     * Serve ad aggiornare il numero delle aste per poter creare un numero di aste complessive >= a 30. Il numero massimo delle aste runtime contemporaneamente è 30 ma il numero massimo delle aste generabili è pari al numero di prodotti da vendere
     * Se viene passato true aumenta il numero delle aste, in caso contrario lo diminuisce.
     */
    public static synchronized void aggiornaAsteInCorso(boolean azione) {
    	if (azione) asteInCorso++;
    	else asteInCorso--;
    }
    
    private Prodotto prodottoDaMettereAllAsta() {
    	for (Prodotto prodotto : prodotti) {
			if(!prodotto.isVenduto()) {
				return prodotto;
			}
		}
    	prodotti = resources.getProdotti();
    	for (Prodotto prodotto : prodotti) {
			if(!prodotto.isVenduto()) {
				return prodotto;
			}
		}
    	return null;
    }
    
    private Asta creaAsta(int id_asta, Date date, Prodotto prodotto, String ip) {
    	return new Asta(id_asta,date,prodotto,ip);
    }
}
