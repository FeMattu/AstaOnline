package server;

public class TimerAsta extends Thread{
	
	private int i = 20;
	private static boolean continua = false, finito = false;
	
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		while(true) {
			if(isContinua()) {
				resetTimer();
			}
			if(i>0) {
				System.out.println("Mancano "+i+" secondi.");
				i--;
			}else {
				finito = true;
				break;
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