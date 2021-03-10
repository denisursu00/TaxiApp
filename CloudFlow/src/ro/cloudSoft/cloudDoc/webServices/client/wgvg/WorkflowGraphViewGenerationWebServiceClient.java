package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(
	name = "WorkflowGraphViewGenerationWebService",
	targetNamespace = "urn:wgvg"
)
@XmlSeeAlso({ ObjectFactory.class })
public interface WorkflowGraphViewGenerationWebServiceClient {

    @WebMethod
    @WebResult(name = "workflowGraphView", targetNamespace = "")
    @RequestWrapper(
		localName = "generateWithHighlighting", targetNamespace = "urn:wgvg",
		className = "ro.cloudSoft.cloudDoc.webServices.client.wgvg.GenerateWithHighlighting"
	)
    @ResponseWrapper(
		localName = "generateWithHighlightingResponse", targetNamespace = "urn:wgvg",
		className = "ro.cloudSoft.cloudDoc.webServices.client.wgvg.GenerateWithHighlightingResponse"
	)
    public WorkflowGraphView generateWithHighlighting(
        @WebParam(name = "workflowGraph", targetNamespace = "") WorkflowGraph workflowGraph,
        @WebParam(name = "identifierForNodeToHighlight", targetNamespace = "") String identifierForNodeToHighlight
    );

    @WebMethod
    @WebResult(name = "workflowGraphView", targetNamespace = "")
    @RequestWrapper(
		localName = "generate", targetNamespace = "urn:wgvg",
		className = "ro.cloudSoft.cloudDoc.webServices.client.wgvg.Generate"
	)
    @ResponseWrapper(
		localName = "generateResponse", targetNamespace = "urn:wgvg",
		className = "ro.cloudSoft.cloudDoc.webServices.client.wgvg.GenerateResponse"
	)
    public WorkflowGraphView generate(
        @WebParam(name = "workflowGraph", targetNamespace = "") WorkflowGraph workflowGraph
    );
}