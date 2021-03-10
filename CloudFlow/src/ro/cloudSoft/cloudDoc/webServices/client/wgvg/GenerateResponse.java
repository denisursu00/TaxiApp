package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generateResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="workflowGraphView" type="{urn:wgvg}workflowGraphView" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generateResponse", propOrder = {
    "workflowGraphView"
})
public class GenerateResponse {

    protected WorkflowGraphView workflowGraphView;

    /**
     * Gets the value of the workflowGraphView property.
     * 
     * @return
     *     possible object is
     *     {@link WorkflowGraphView }
     *     
     */
    public WorkflowGraphView getWorkflowGraphView() {
        return workflowGraphView;
    }

    /**
     * Sets the value of the workflowGraphView property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkflowGraphView }
     *     
     */
    public void setWorkflowGraphView(WorkflowGraphView value) {
        this.workflowGraphView = value;
    }

}