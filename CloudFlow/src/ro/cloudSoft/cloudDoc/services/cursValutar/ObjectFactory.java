//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.11.27 at 04:27:44 PM EET 
//


package ro.cloudSoft.cloudDoc.services.cursValutar;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ro.bnr.xsd package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ro.bnr.xsd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataSet }
     * 
     */
    public DataSet createDataSet() {
        return new DataSet();
    }

    /**
     * Create an instance of {@link LTCube }
     * 
     */
    public LTCube createLTCube() {
        return new LTCube();
    }

    /**
     * Create an instance of {@link LTHeader }
     * 
     */
    public LTHeader createLTHeader() {
        return new LTHeader();
    }

    /**
     * Create an instance of {@link DataSet.Body }
     * 
     */
    public DataSet.Body createDataSetBody() {
        return new DataSet.Body();
    }

    /**
     * Create an instance of {@link LTCube.Rate }
     * 
     */
    public LTCube.Rate createLTCubeRate() {
        return new LTCube.Rate();
    }

}
