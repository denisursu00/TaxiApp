
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
 *         &lt;element name="MAILADDRESS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="STARTDATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ENDDATE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MAILSUBJECT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MAILBODY" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "mailaddress",
    "startdate",
    "enddate",
    "mailsubject",
    "mailbody"
})
@XmlRootElement(name = "SETOOOSTATUS")
public class SETOOOSTATUS {

    @XmlElement(name = "MAILADDRESS", required = true)
    protected String mailaddress;
    @XmlElement(name = "STARTDATE", required = true)
    protected String startdate;
    @XmlElement(name = "ENDDATE", required = true)
    protected String enddate;
    @XmlElement(name = "MAILSUBJECT", required = true)
    protected String mailsubject;
    @XmlElement(name = "MAILBODY", required = true)
    protected String mailbody;

    /**
     * Gets the value of the mailaddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAILADDRESS() {
        return mailaddress;
    }

    /**
     * Sets the value of the mailaddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAILADDRESS(String value) {
        this.mailaddress = value;
    }

    /**
     * Gets the value of the startdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTARTDATE() {
        return startdate;
    }

    /**
     * Sets the value of the startdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTARTDATE(String value) {
        this.startdate = value;
    }

    /**
     * Gets the value of the enddate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDDATE() {
        return enddate;
    }

    /**
     * Sets the value of the enddate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDDATE(String value) {
        this.enddate = value;
    }

    /**
     * Gets the value of the mailsubject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAILSUBJECT() {
        return mailsubject;
    }

    /**
     * Sets the value of the mailsubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAILSUBJECT(String value) {
        this.mailsubject = value;
    }

    /**
     * Gets the value of the mailbody property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAILBODY() {
        return mailbody;
    }

    /**
     * Sets the value of the mailbody property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAILBODY(String value) {
        this.mailbody = value;
    }

}
