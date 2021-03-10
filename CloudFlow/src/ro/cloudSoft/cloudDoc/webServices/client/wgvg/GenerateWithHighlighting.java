package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generateWithHighlighting complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generateWithHighlighting">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="workflowGraph" type="{urn:wgvg}workflowGraph" minOccurs="0"/>
 *         &lt;element name="identifierForNodeToHighlight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generateWithHighlighting", propOrder = {
    "workflowGraph",
    "identifierForNodeToHighlight"
})
public class GenerateWithHighlighting {

    protected WorkflowGraph workflowGraph;
    protected String identifierForNodeToHighlight;

    /**
     * Gets the value of the workflowGraph property.
     * 
     * @return
     *     possible object is
     *     {@link WorkflowGraph }
     *     
     */
    public WorkflowGraph getWorkflowGraph() {
        return workflowGraph;
    }

    /**
     * Sets the value of the workflowGraph property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkflowGraph }
     *     
     */
    public void setWorkflowGraph(WorkflowGraph value) {
        this.workflowGraph = value;
    }

    /**
     * Gets the value of the identifierForNodeToHighlight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifierForNodeToHighlight() {
        return identifierForNodeToHighlight;
    }

    /**
     * Sets the value of the identifierForNodeToHighlight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifierForNodeToHighlight(String value) {
        this.identifierForNodeToHighlight = value;
    }

}