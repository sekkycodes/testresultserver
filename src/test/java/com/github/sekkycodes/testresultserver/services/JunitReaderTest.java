package com.github.sekkycodes.testresultserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JunitReaderTest extends TestBase {

  private JunitReader sut;

  private InputStream inputStream;

  @BeforeEach
  void beforeEach() {
    inputStream = this.getClass().getResourceAsStream("/junit-passed.xml");
    sut = new JunitReader();
  }

  @AfterEach
  void afterEach() throws IOException {
    inputStream.close();
  }

  @Test
  void readsTestSuiteFromXmlInputStream() throws ImportException {
    Testsuite suite = sut.readSuite(inputStream);
    assertEquals(1, suite.getTestcase().size());
  }
}
