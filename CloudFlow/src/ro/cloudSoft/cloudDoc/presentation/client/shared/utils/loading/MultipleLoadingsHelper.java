package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.loading;


/**
 * Mecanism prin care se poate specifica ce actiune sa se faca dupa terminarea mai multor operatii (asincrone)
 */
public abstract class MultipleLoadingsHelper {
	
	private final int totalLoadingsCount;
	private int completedLoadingsCount;
	
	public MultipleLoadingsHelper(int totalLoadingsCount) {
		this.totalLoadingsCount = totalLoadingsCount;
		this.completedLoadingsCount = 0;
	}
	
	public void loadingComplete() {
		
		completedLoadingsCount++;
		
		if (completedLoadingsCount == totalLoadingsCount) {
			doAfterLoadingsComplete();
		} else if (completedLoadingsCount > totalLoadingsCount) {
			throw new IllegalStateException("S-a notificat de mai multe completari de loading-uri decat era nevoie.");
		}
	}
	
	protected abstract void doAfterLoadingsComplete();
}