package server;

import server.AstaDataServer;

public class TimerAsta extends Thread{
	
	private int i = 20;
	private static boolean continua = false, finito = false;
	private AstaDataServer astaDataServer;
	
	public TimerAsta(AstaDataServer asta) {
		this.astaDataServer = asta;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		// Finchè l'asta non è finita
		while (!this.astaDataServer.isEnded()) {
			
			// Se c'è un offerta comincio a contare per fermare l'asta
			if (this.astaDataServer.getOffertaMaggiore() != null) {
				// Conto
				
				if(isContinua()) {
	                resetTimer();
	            }
	            if(i>0) {
	                System.out.println("Mancano "+i+" secondi.");
	                i--;
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }else {
	            	// Imposto la fine dell'asta
	                this.astaDataServer.endAsta();
	                break;
	            }
			}
		}
	}

	public int getI() {
		return i;
	}
	
	private static boolean isContinua() {
		return continua;
	}
	
	public static boolean isFinito() {
		return finito;
	}
	
	public static void setContinua(boolean continua) {
		TimerAsta.continua = continua;
	}
	
	public static void setFinito(boolean finito) {
		TimerAsta.finito = finito;
	}
	
	private void resetTimer() {
		i = 20;
	}
	
}