package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.widget.MessageBox;

/**
 * Manager care se ocupa cu notificarea si restrictionarea utilizatorului atunci cand se incarca sau se asteapta
 * date de pe server
 * 
 * 
 * 
 * @deprecated La deschiderea unor ferestre in timp ce se incarca ceva, fereastra apare in fata componentei,
 * utilizatorul putand accesa elementele din interfata. Drept urmare, componenta a fost inlocuita cu una mai sigura.
 */
@Deprecated
public class LoadingManagerOld {
	
	private static LoadingManagerOld instance;
	
	private int loadings;
	private LoadingBox loadingBox;
	
	private LoadingManagerOld() {
		this.loadings = 0;
		this.loadingBox = new LoadingBox();
	}
	
	public synchronized void loading() {
		this.loadings++;
		if (!this.loadingBox.isVisible()) {
			this.loadingBox.show();
		}
	}
	
	public synchronized void loadingComplete() {
		this.loadings--;
		if (this.loadings == 0) {
			this.loadingBox.close();
		}
	}
	
	public static synchronized LoadingManagerOld get() {
		if (instance == null) {
			instance = new LoadingManagerOld();
		}
		return instance;
	}
	
	private class LoadingBox extends MessageBox {
		
		public LoadingBox() {
		    this.setButtons("");
		    this.setClosable(false);
		    this.setMessage(GwtLocaleProvider.getMessages().PLEASE_WAIT());
		    this.setProgressText(GwtLocaleProvider.getMessages().LOADING());
			this.setTitle(GwtLocaleProvider.getConstants().OPERATION());
		    this.setType(MessageBoxType.WAIT);
		}
	}
}