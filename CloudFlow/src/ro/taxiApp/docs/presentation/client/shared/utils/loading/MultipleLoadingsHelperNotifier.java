package ro.taxiApp.docs.presentation.client.shared.utils.loading;

/**
 * Notifica un <tt>MultipleLoadingsHelper</tt> de completarea unei operatii (asincrone).
 */
public class MultipleLoadingsHelperNotifier implements Runnable {
	
	private final MultipleLoadingsHelper multipleLoadingsHelper;
	
	public MultipleLoadingsHelperNotifier(MultipleLoadingsHelper multipleLoadingsHelper) {
		this.multipleLoadingsHelper = multipleLoadingsHelper;
	}
	
	@Override
	public void run() {
		multipleLoadingsHelper.loadingComplete();
	}
}