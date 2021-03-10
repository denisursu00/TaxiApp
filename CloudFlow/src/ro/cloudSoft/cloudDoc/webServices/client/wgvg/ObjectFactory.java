package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ro.cloudSoft.cloudDoc.webServices.client.wgvg package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GenerateResponse_QNAME = new QName("urn:wgvg", "generateResponse");
    private final static QName _GenerateWithHighlighting_QNAME = new QName("urn:wgvg", "generateWithHighlighting");
    private final static QName _GenerateWithHighlightingResponse_QNAME = new QName("urn:wgvg", "generateWithHighlightingResponse");
    private final static QName _Generate_QNAME = new QName("urn:wgvg", "generate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ro.cloudSoft.cloudDoc.webServices.client.wgvg
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GenerateWithHighlighting }
     * 
     */
    public GenerateWithHighlighting createGenerateWithHighlighting() {
        return new GenerateWithHighlighting();
    }

    /**
     * Create an instance of {@link Generate }
     * 
     */
    public Generate createGenerate() {
        return new Generate();
    }

    /**
     * Create an instance of {@link WorkflowGraphEdge }
     * 
     */
    public WorkflowGraphEdge createWorkflowGraphEdge() {
        return new WorkflowGraphEdge();
    }

    /**
     * Create an instance of {@link GenerateResponse }
     * 
     */
    public GenerateResponse createGenerateResponse() {
        return new GenerateResponse();
    }

    /**
     * Create an instance of {@link WorkflowGraphNode }
     * 
     */
    public WorkflowGraphNode createWorkflowGraphNode() {
        return new WorkflowGraphNode();
    }

    /**
     * Create an instance of {@link WorkflowGraphView }
     * 
     */
    public WorkflowGraphView createWorkflowGraphView() {
        return new WorkflowGraphView();
    }

    /**
     * Create an instance of {@link WorkflowGraph }
     * 
     */
    public WorkflowGraph createWorkflowGraph() {
        return new WorkflowGraph();
    }

    /**
     * Create an instance of {@link GenerateWithHighlightingResponse }
     * 
     */
    public GenerateWithHighlightingResponse createGenerateWithHighlightingResponse() {
        return new GenerateWithHighlightingResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:wgvg", name = "generateResponse")
    public JAXBElement<GenerateResponse> createGenerateResponse(GenerateResponse value) {
        return new JAXBElement<GenerateResponse>(_GenerateResponse_QNAME, GenerateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateWithHighlighting }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:wgvg", name = "generateWithHighlighting")
    public JAXBElement<GenerateWithHighlighting> createGenerateWithHighlighting(GenerateWithHighlighting value) {
        return new JAXBElement<GenerateWithHighlighting>(_GenerateWithHighlighting_QNAME, GenerateWithHighlighting.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateWithHighlightingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:wgvg", name = "generateWithHighlightingResponse")
    public JAXBElement<GenerateWithHighlightingResponse> createGenerateWithHighlightingResponse(GenerateWithHighlightingResponse value) {
        return new JAXBElement<GenerateWithHighlightingResponse>(_GenerateWithHighlightingResponse_QNAME, GenerateWithHighlightingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Generate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:wgvg", name = "generate")
    public JAXBElement<Generate> createGenerate(Generate value) {
        return new JAXBElement<Generate>(_Generate_QNAME, Generate.class, null, value);
    }

}