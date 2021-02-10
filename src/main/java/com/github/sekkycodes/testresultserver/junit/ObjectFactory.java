
package com.github.sekkycodes.testresultserver.junit;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.github.sekkycodes.testresultserver.junit package. 
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

    private static final QName _Testsuite_QNAME = new QName("", "testsuite");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.github.sekkycodes.testresultserver.junit
     * 
     */
    public ObjectFactory() {
        // for serialization
    }

    /**
     * Create an instance of {@link Testsuites }
     * 
     */
    public Testsuites createTestsuites() {
        return new Testsuites();
    }

    /**
     * Create an instance of {@link com.github.sekkycodes.testresultserver.junit.Testsuite }
     * 
     */
    public com.github.sekkycodes.testresultserver.junit.Testsuite createTestsuite() {
        return new com.github.sekkycodes.testresultserver.junit.Testsuite();
    }

    /**
     * Create an instance of {@link com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase }
     * 
     */
    public com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase createTestsuiteTestcase() {
        return new com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase();
    }

    /**
     * Create an instance of {@link com.github.sekkycodes.testresultserver.junit.Testsuite.Properties }
     * 
     */
    public com.github.sekkycodes.testresultserver.junit.Testsuite.Properties createTestsuiteProperties() {
        return new com.github.sekkycodes.testresultserver.junit.Testsuite.Properties();
    }

    /**
     * Create an instance of {@link Testsuites.Testsuite }
     * 
     */
    public Testsuites.Testsuite createTestsuitesTestsuite() {
        return new Testsuites.Testsuite();
    }

    /**
     * Create an instance of {@link com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Error }
     * 
     */
    public com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Error createTestsuiteTestcaseError() {
        return new com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Error();
    }

    /**
     * Create an instance of {@link com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Failure }
     * 
     */
    public com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Failure createTestsuiteTestcaseFailure() {
        return new com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Failure();
    }

    /**
     * Create an instance of {@link com.github.sekkycodes.testresultserver.junit.Testsuite.Properties.Property }
     * 
     */
    public com.github.sekkycodes.testresultserver.junit.Testsuite.Properties.Property createTestsuitePropertiesProperty() {
        return new com.github.sekkycodes.testresultserver.junit.Testsuite.Properties.Property();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link com.github.sekkycodes.testresultserver.junit.Testsuite }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "testsuite")
    public JAXBElement<com.github.sekkycodes.testresultserver.junit.Testsuite> createTestsuite(com.github.sekkycodes.testresultserver.junit.Testsuite value) {
        return new JAXBElement<>(_Testsuite_QNAME, com.github.sekkycodes.testresultserver.junit.Testsuite.class, null, value);
    }

}
