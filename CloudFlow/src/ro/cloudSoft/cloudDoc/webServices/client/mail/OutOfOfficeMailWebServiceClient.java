
package ro.cloudSoft.cloudDoc.webServices.client.mail;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * 
 */
@WebService(
	name = "OOOInfo",
	targetNamespace = "urn:DefaultNamespace"
)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface OutOfOfficeMailWebServiceClient {

    @WebMethod(operationName = "RESETOOOSTATUS")
    @WebResult(name = "RESETOOOSTATUSReturn", targetNamespace = "")
    @RequestWrapper(
		localName = "RESETOOOSTATUS", targetNamespace = "urn:DefaultNamespace",
		className = "ro.cloudSoft.cloudDoc.webServices.client.mail.RESETOOOSTATUS"
	)
    @ResponseWrapper(
		localName = "RESETOOOSTATUSResponse", targetNamespace = "urn:DefaultNamespace",
		className = "ro.cloudSoft.cloudDoc.webServices.client.mail.RESETOOOSTATUSResponse"
	)
    public String resetOooStatus(
        @WebParam(name = "MAILADDRESS", targetNamespace = "") String mailAddress
    );

    @WebMethod(operationName = "SETOOOSTATUS")
    @WebResult(name = "SETOOOSTATUSReturn", targetNamespace = "")
    @RequestWrapper(
		localName = "SETOOOSTATUS", targetNamespace = "urn:DefaultNamespace",
		className = "ro.cloudSoft.cloudDoc.webServices.client.mail.SETOOOSTATUS"
	)
    @ResponseWrapper(
		localName = "SETOOOSTATUSResponse", targetNamespace = "urn:DefaultNamespace",
		className = "ro.cloudSoft.cloudDoc.webServices.client.mail.SETOOOSTATUSResponse"
	)
    public String setOooStatus(
        @WebParam(name = "MAILADDRESS", targetNamespace = "") String mailAddress,
        @WebParam(name = "STARTDATE", targetNamespace = "") String startDate,
        @WebParam(name = "ENDDATE", targetNamespace = "") String endDate,
        @WebParam(name = "MAILSUBJECT", targetNamespace = "") String mailSubject,
        @WebParam(name = "MAILBODY", targetNamespace = "") String mailBody
    );
}