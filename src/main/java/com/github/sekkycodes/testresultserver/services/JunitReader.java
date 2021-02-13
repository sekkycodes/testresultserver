package com.github.sekkycodes.testresultserver.services;

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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

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
   * @throws IOException in case reading from provided input stream fails
   * @throws JAXBException if reading XML fails
   * @throws XMLStreamException in case creating a StreamReader for the provided xmlContent fails
   */
  public Testsuite readSuite(InputStream in)
      throws JAXBException, XMLStreamException, IOException {

    try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      // read all text first before parsing - this seems to be necessary for the unmarshalling to work
      String xmlContent = reader.lines().collect(Collectors.joining("\n"));

      JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

      XMLStreamReader streamReader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xmlContent));
      return unmarshaller.unmarshal(streamReader, Testsuite.class).getValue();
    }
  }
}