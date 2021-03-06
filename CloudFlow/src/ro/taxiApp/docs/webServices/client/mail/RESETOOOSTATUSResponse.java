
package ro.taxiApp.docs.webServices.client.mail;

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
 *         &lt;element name="RESETOOOSTATUSReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "resetooostatusReturn"
})
@XmlRootElement(name = "RESETOOOSTATUSResponse")
public class RESETOOOSTATUSResponse {

    @XmlElement(name = "RESETOOOSTATUSReturn", required = true)
    protected String resetooostatusReturn;

    /**
     * Gets the value of the resetooostatusReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRESETOOOSTATUSReturn() {
        return resetooostatusReturn;
    }

    /**
     * Sets the value of the resetooostatusReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRESETOOOSTATUSReturn(String value) {
        this.resetooostatusReturn = value;
    }

}
