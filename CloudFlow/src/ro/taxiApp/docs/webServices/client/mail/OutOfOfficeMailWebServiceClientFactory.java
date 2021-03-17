package ro.taxiApp.docs.webServices.client.mail;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * 
 */
@WebServiceClient(
	name = "OOOInfoService",
	targetNamespace = "urn:DefaultNamespace"
)
public class OutOfOfficeMailWebServiceClientFactory extends Service {
	
	private static final String PORT_NAME = "Domino";
	
	private static final QName SERVICE_QNAME = new QName("urn:DefaultNamespace", "OOOInfoService");
	private static final QName PORT_QNAME = new QName("urn:DefaultNamespace", PORT_NAME);

	/**
	 * Constructor implicit creat doar pentru a satisface framework-ul (JAX-WS).
	 * NU trebuie folosit intrucat NU exista o locatie implicita pt. WSDL. Trebuie specificata locatia manual.
	 */
	public OutOfOfficeMailWebServiceClientFactory() {
		super(null, null);
		throw new UnsupportedOperationException("Trebuie folosit constructorul ce specifica URL-ul WSDL-ului.");
	}

    public OutOfOfficeMailWebServiceClientFactory(String wsdlLocation) throws MalformedURLException {
    	super(new URL(wsdlLocation), SERVICE_QNAME);
    }
    
    @WebEndpoint(name = PORT_NAME)
    public OutOfOfficeMailWebServiceClient getPort() {
        return super.getPort(PORT_QNAME, OutOfOfficeMailWebServiceClient.class);
    }
}