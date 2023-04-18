package Connection;

import gambit.Asta;
import gambit.GestisciAste;
import gambit.Resources;

/**
 * <b>ThreadAsta class</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */
public class ThreadAsta extends Thread{

    //private final int ID_ASTA;
    //private Date DataOra_inizio;
    //private Date DataOra_fine;
    //private String multicastIP;
    //private String porta;
    //private MulticastSocket socket;
    //private Prodotto prodotto;
    private Resources r;
    private Asta asta;
    
    /**
     * <b>ThreadAsta constructor</b>
     * @param asta -> asta istance
     * @param r -> resources istance
     */
    public ThreadAsta(Asta asta, Resources r){
        this.asta = asta;
        this.r = r;
    }

    /**
     * <b>Run method for ThreadAsta class</b>
     */
    public void run(){
        super.run();
        //prodotto.toString();
        //System.out.println(prodotto);
        
        
        // Tutte le volte che un'asta termina, il contatore delle aste runtime viene aggiornato. Il valore false dice che devo diminuire il numero delle aste runti
        GestisciAste.aggiornaAsteInCorso(false);
        r.addAstaIntoDB(asta);
    }
    
}