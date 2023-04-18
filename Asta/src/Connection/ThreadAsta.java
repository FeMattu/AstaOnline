package Connection;

import java.sql.Date;
import java.time.LocalDate;

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
    private Resources resources;
    private Asta asta;
    
    /**
     * <b>ThreadAsta constructor</b>
     * @param asta -> asta istance
     * @param r -> resources istance
     */
    public ThreadAsta(Asta asta, Resources resources){
        this.asta = asta;
        this.resources = resources;
    }

    /**
     * <b>Run method for ThreadAsta class</b>
     */
    public void run(){
        super.run();
        //settaggio data e ora inizio asta
        asta.setDataOra_inizio(Date.valueOf(LocalDate.now()));
        
        
        
        //run .....
        System.out.println(asta);
        System.out.println(asta.getProdotto());
        asta.setVincitore(resources.getClienti().get(asta.getId_asta()));        
        
        
        
        //fine asta
        //aggiunta data e ora finea asta
        asta.setDataOra_fine(Date.valueOf(LocalDate.now()));
        resources.getCurrentGambits().remove(asta);
        resources.addAstaIntoDB(asta);
    }
    
}