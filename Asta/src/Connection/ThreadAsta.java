package Connection;

import java.net.MulticastSocket;
import java.sql.Date;

import gambit.Asta;
import gambit.GestisciAste;
import gambit.Prodotto;
import gambit.Resources;


public class ThreadAsta extends Thread{

    private final int ID_ASTA;
    private Date DataOra_inizio;
    private Date DataOra_fine;
    private String multicastIP;
    private String porta;
    private MulticastSocket socket;
    private Resources r;
    private Prodotto prodotto;
    private Asta asta;
    

    public ThreadAsta(int ID, String multicastIP, Prodotto prodotto, Resources r, Asta asta){
        this.ID_ASTA=ID;
        this.multicastIP=multicastIP;
        this.prodotto=prodotto;
        this.r = r;
        this.asta = asta;
    }

    public void run(){
        super.run();
        //prodotto.toString();
        //System.out.println(prodotto);
        
        
        // Tutte le volte che un'asta termina, il contatore delle aste runtime viene aggiornato. Il valore false dice che devo diminuire il numero delle aste runti
        GestisciAste.aggiornaAsteInCorso(false);
        r.addAstaIntoDB(asta);
    }
    
}