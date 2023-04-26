package server;

/**
 * <b>Classe TimerAsta</b>
 * @author <i>Federico Mattucci<br>
 * 			  Tommaso Giannecchini<br>
 * 			  Federico Massanti<br>
 * 			  Lorenzo Rapposelli<br>
 * 			  Giacomo Diridoni</i>
 *
 */ 
public class TimerAsta extends Thread{
	
	private int i = 20;
	private AstaDataServer asta;
	
	/**
	 * Costruttore classe TimerAsta
	 * @param asta -> istanza classe AstaDataServer passata come parametro
	 */
	public TimerAsta(AstaDataServer asta) {
		this.asta = asta;
	}
	
	/**
	 * Metodo run della classe TimerAsta
	 */
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		/* Parte di codice che non va perché non entra nel secondo if
		while (!this.asta.isEnded()) {
			System.out.println("Sono entrato nel primo while-true, condizione di quello sotto: "+this.asta.isWaiting());
			// Se c'è un offerta comincio a contare per fermare l'asta
			if (i>0 && this.asta.isWaiting()) {
				// Conto
				System.out.println("Sono entrato qua dentro.");
				if(this.asta.isNuovaOfferta()) {
	                resetTimer();
	            }
				System.out.println("Mancano "+i+" secondi.");
	            i--;
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }else {
	        	if(this.asta.getOffertaMaggiore().getOfferente() != null) {
	        		this.asta.endAsta();
	        		break;
	        	}
	        	// Imposto la fine dell'asta
	        }
		}*/
	}

	/*GETTERS AND SETTERS*/
	
	public int getI() {
		return i;
	}
	
	public AstaDataServer getAsta() {
		return asta;
	}
	
	private void resetTimer() {
		i = 20;
	}
	
}