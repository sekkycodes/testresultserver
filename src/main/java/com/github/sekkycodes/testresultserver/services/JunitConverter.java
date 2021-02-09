package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.junit.ObjectFactory;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;

/**
 * Converts raw input streams from JUnit XML files and converts them into java objects
 */
@Service
public class JunitConverter {

  /**
   * Converts raw input streams from JUnit XML files and converts them into java objects
   *
   * @param in stream input from XML
   * @return test suite
   * @throws JAXBException if reading XML fails
   */
  @SuppressWarnings("unchecked")
  public Testsuite readSuite(InputStream in) throws JAXBException {

    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

    return ((JAXBElement<Testsuite>) unmarshaller.unmarshal(in)).getValue();
  }
}