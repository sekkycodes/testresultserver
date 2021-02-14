package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.junit.ObjectFactory;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Converts raw input streams from JUnit XML files and converts them into java objects
 */
@Service
public class JunitReader {

  /**
   * Converts raw input streams from JUnit XML files and converts them into java objects
   *
   * @param in input stream of the XML
   * @return test suite
   * @throws ImportException in case reading from provided input stream and parsing suite fails
   */
  public Testsuite readSuite(InputStream in) throws ImportException {
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      // read all text first before parsing - this seems to be necessary for the unmarshalling to work
      String xmlContent = reader.lines().collect(Collectors.joining("\n"));
      return unmarshal(xmlContent);
    } catch (SAXException | ParserConfigurationException | JAXBException | IOException e) {
      throw new ImportException("Failed to read and convert XML: " + e.getMessage(), e);
    }
  }

  @SuppressWarnings("unchecked")
  private Testsuite unmarshal(String xml)
      throws SAXException, ParserConfigurationException, JAXBException {

    //Disable XXE
    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
    spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

    //Do unmarshall operation
    Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(),
        new InputSource(new StringReader(xml)));
    JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class);
    Unmarshaller um = jc.createUnmarshaller();
    return ((JAXBElement<Testsuite>) um.unmarshal(xmlSource)).getValue();
  }
}