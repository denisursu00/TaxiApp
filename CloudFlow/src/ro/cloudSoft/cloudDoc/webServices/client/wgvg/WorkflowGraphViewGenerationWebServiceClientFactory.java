package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

@WebServiceClient(
	name = "WorkflowGraphViewGenerationWebService",
	targetNamespace = "urn:wgvg"
)
public class WorkflowGraphViewGenerationWebServiceClientFactory extends Service {
	
	private static final QName SERVICE_NAME = new QName("urn:wgvg", "WorkflowGraphViewGenerationWebService");
	private static final QName PORT_NAME = new QName("urn:wgvg", "WorkflowGraphViewGenerationWebServicePort");

	/**
	 * Constructor implicit creat doar pentru a satisface framework-ul (JAX-WS).
	 * NU trebuie folosit intrucat NU exista o locatie implicita pt. WSDL. Trebuie specificata locatia manual.
	 */
	public WorkflowGraphViewGenerationWebServiceClientFactory() {
		super(null, null);
		throw new UnsupportedOperationException("Trebuie folosit constructorul ce specifica URL-ul WSDL-ului.");
	}

    public WorkflowGraphViewGenerationWebServiceClientFactory(String wsdlLocation) throws MalformedURLException {
    	super(new URL(wsdlLocation), SERVICE_NAME);
    }
    
    @WebEndpoint(name = "WorkflowGraphViewGenerationWebServicePort")
    public WorkflowGraphViewGenerationWebServiceClient getPort() {
        return super.getPort(PORT_NAME, WorkflowGraphViewGenerationWebServiceClient.class);
    }
}