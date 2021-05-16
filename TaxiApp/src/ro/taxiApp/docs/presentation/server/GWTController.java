package ro.taxiApp.docs.presentation.server;

import static ro.taxiApp.docs.core.AppExceptionCodes.APPLICATION_ERROR;
import static ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationExceptionCodes.UNAUTHENTICATED;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.UnexpectedException;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.core.AppExceptionUtils;
import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationException;
import ro.taxiApp.docs.presentation.server.services.GxtServiceImplBase;
import ro.taxiApp.docs.presentation.server.utils.PresentationExceptionUtils;

public class GWTController extends RemoteServiceServlet implements Controller, ServletContextAware, InitializingBean {

	private static final long serialVersionUID = 1L;
    
    private ServletContext servletContext;
	
    /**
     * Reprezinta o variabila thread-local pentru stocarea sesiunii HTTP.
     * <br>
     * Aceasta este necesara pentru ca serviciile GWT RPC au nevoie de sesiune (sau obiecte din ea), dar aceste
     * servicii nu sunt apelate de catre container-ul web, ci de catre aceasta clasa si astfel serviciile nu au
     * acces direct la sesiune.
     */
	private static ThreadLocal<HttpSession> threadLocalSession = new ThreadLocal<HttpSession>();

    private GxtServiceImplBase remoteService;
    private Class<? extends GxtServiceImplBase> remoteServiceClass;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			remoteService
		);
	}
    
    public static HttpSession getThreadLocalSession(GxtServiceImplBase caller) {
    	return threadLocalSession.get();
    }

    /**
     * Call GWT's RemoteService doPost() method and return null.
     *
     * @param request The current HTTP request
     * @param response The current HTTP response
     * @return A ModelAndView to render, or null if handled directly
     * @throws Exception In case of errors
     */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	/*
    	 * La fiecare request se va tine minte sesiunea legata de acesta (tinand cont ca 1 request = 1 thread separat).
    	 * <br>
    	 * 1. Se tine minte sesiunea pt. request.
    	 * 2. Se apeleaza indirect serviciul GWT RPC.
    	 * 3. Dupa apel se sterge referinta intrucat nu mai este necesara.
    	 */
    	
    	threadLocalSession.set(request.getSession());
    	doPost(request, response);        
        threadLocalSession.remove();
        
        return null; // Raspunsul va fi trimis de catre metoda de mai sus.
    }

    @Override
    public String processCall(String payload) throws SerializationException {
    	
    	if (isNotLoggedIn()) {
    		return RPC.encodeResponseForFailure(null, new PresentationException(UNAUTHENTICATED));
    	}

    	RPCRequest rpcRequest = null;
        try {
            rpcRequest = RPC.decodeRequest(payload, remoteServiceClass, this);
            return RPC.invokeAndEncodeResponse(remoteService, rpcRequest.getMethod(),
        		rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
        } catch (Exception e) {
        	
        	// Caut exceptie de aplicatie printre cauze (daca exista).
        	AppException appException = AppExceptionUtils.getAppExceptionFromExceptionCause(e);
        	if (appException != null) {
        		// Am gasit exceptie de aplicatie, voi trimite mesajul in interfata.
        		return RPC.encodeResponseForFailure(null, PresentationExceptionUtils.getPresentationException(appException));
        	}
        	
        	if (e instanceof IncompatibleRemoteServiceException) {
        		
        		IncompatibleRemoteServiceException irse = (IncompatibleRemoteServiceException) e;
        		
            	String message = null;
            	if (rpcRequest != null) {
            		message = "Exista o incompatibilitate intre partea client si server " +
    	        		"a serviciului GWT RPC [" + rpcRequest.getMethod().getDeclaringClass().getName() + "]. " +
    					"Pentru detalii consultati JavaDoc-ul clasei GWT [" + IncompatibleRemoteServiceException.class.getName() + "].";
            	} else {
            		message = "Exista o incompatibilitate intre partea client si server " +
            			"a serviciului GWT RPC cu implementarea [" + remoteService.getClass().getName() + "].";
            	}
                return RPC.encodeResponseForFailure(null, irse);
            } else if (e instanceof UnexpectedException) {
            	
            	UnexpectedException ue = (UnexpectedException) e;
            	
            	Throwable realException = ue.getCause();
            	logNonRpcExceptionOnRpcCall(rpcRequest, realException);
            	return encodeNonRpcException(realException);
            } else {
            	logNonRpcExceptionOnRpcCall(rpcRequest, e);
            	return encodeNonRpcException(e);
            }
        }
    }
    
    /**
     * Logheaza o exceptie non-RPC (GWT) care a aparut in timpul unui apel GWT RPC.
     * 
     * @param rpcRequest apelul GWT RPC
     * @param exception exceptia
     */
    private void logNonRpcExceptionOnRpcCall(RPCRequest rpcRequest, Throwable exception) {
    	String message = null;
    	if (rpcRequest != null) {
	    	message = "Exceptie la apelarea prin GWT RPC a metodei [" + rpcRequest.getMethod() + "] " +
				"cu parametrii [" + StringUtils.join(rpcRequest.getParameters(), ", ") + "], " +
				"clasa de implementare [" + remoteService.getClass().getName() + "]";
    	} else {
    		message = "Exceptie la apelarea prin GWT RPC cu implementarea [" + remoteService.getClass().getName() + "]";
    	}
    }
    
    /** Trimite catre partea client un mesaj encode-at ca urmare a aparitiei unei exceptii neasteptate (non-RPC GWT). */
    private String encodeNonRpcException(Throwable exception) throws SerializationException {
    	return RPC.encodeResponseForFailure(null, PresentationExceptionUtils.getPresentationException(APPLICATION_ERROR));
    }
    
    private boolean isNotLoggedIn() {
    	SecurityManager userSecurity = remoteService.getSecurity();
    	return ((userSecurity == null) || StringUtils.isEmpty(userSecurity.getUserIdAsString()));
    }
    
    @Override
    public String getServletName() {
    	return remoteServiceClass.getSimpleName();
    }

    public void setRemoteService(GxtServiceImplBase remoteService) {
        this.remoteService = remoteService;
        this.remoteServiceClass = this.remoteService.getClass();
    }
    
    @Override
    public ServletContext getServletContext() {
    	return servletContext;
    }
    @Override
    public void setServletContext(ServletContext servletContext) {
    	this.servletContext = servletContext;
    }
}