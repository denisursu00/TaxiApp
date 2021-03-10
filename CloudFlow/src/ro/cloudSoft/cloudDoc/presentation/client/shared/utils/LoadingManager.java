package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.ArrayList;
import java.util.Collection;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.google.gwt.user.client.Timer;

public class LoadingManager {

	private static LoadingManager instance;
	
	private Collection<ComponentWithRenderingProblems> componentsWithRenderingProblems = new ArrayList<ComponentWithRenderingProblems>();
	
	private LoadingManager() {}
	
	private static native void init(String loadingText) /*-{
		$wnd.LoadingManager.enableWithText(loadingText);
    }-*/;
	
	public static synchronized LoadingManager get() {
		if (instance == null) {
			init(GwtLocaleProvider.getMessages().PLEASE_WAIT());
			instance = new LoadingManager();
		}
		return instance;
	}
	
	public void registerComponentWithRenderingProblems(ComponentWithRenderingProblems component) {
		componentsWithRenderingProblems.add(component);
	}
	
	public synchronized void loading() {
		loadingNative();
	}
	
	public synchronized void loadingComplete() {
		loadingCompleteNative();
		ensureComponentsWithRenderingProblemsAreProperlyRendered();
	}
	
	private native void loadingNative() /*-{
		$wnd.LoadingManager.loading();
    }-*/;
	
	private native void loadingCompleteNative() /*-{
		$wnd.LoadingManager.loadingComplete();
    }-*/;
	
	private void ensureComponentsWithRenderingProblemsAreProperlyRendered() {
		new Timer() {
			
			@Override
			public void run() {
				for (ComponentWithRenderingProblems component : componentsWithRenderingProblems) {
					if (component.needsEnsuringIsProperlyRenderedNow()) {
						component.ensureIsProperlyRendered();
					}
				}
			}
		}.schedule(1);
	}
}