
package ro.cloudSoft.cloudDoc.webServices.client.mail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SETOOOSTATUSReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "setooostatusReturn"
})
@XmlRootElement(name = "SETOOOSTATUSResponse")
public class SETOOOSTATUSResponse {

    @XmlElement(name = "SETOOOSTATUSReturn", required = true)
    protected String setooostatusReturn;

    /**
     * Gets the value of the setooostatusReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSETOOOSTATUSReturn() {
        return setooostatusReturn;
    }

    /**
     * Sets the value of the setooostatusReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSETOOOSTATUSReturn(String value) {
        this.setooostatusReturn = value;
    }

}
