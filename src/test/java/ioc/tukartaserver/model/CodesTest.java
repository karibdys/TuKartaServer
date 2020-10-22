package ioc.tukartaserver.model;

import junit.framework.TestCase;
import org.json.JSONObject;

/**
 * Clase para probar el funcionamiento de la clase Codes 
 * @author Manu Mora
 */
public class CodesTest extends TestCase {

public CodesTest(String testName) {
  super(testName);
}

@Override
protected void setUp() throws Exception {
  super.setUp();
}

@Override
protected void tearDown() throws Exception {
  super.tearDown();
}

  /**
   * Test of initMap method, of class Codes.
   */
  public void testInitMap() {
    System.out.println("initMap");
    Codes.initMap();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCode method, of class Codes.
   */
  public void testGetCode() {
    System.out.println("getCode");
    Codes instance = null;
    String expResult = "";
    String result = instance.getCode();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMessage method, of class Codes.
   */
  public void testGetMessage() {
    System.out.println("getMessage");
    Codes instance = null;
    String expResult = "";
    String result = instance.getMessage();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of parseCode method, of class Codes.
   */
  public void testParseCode() {
    System.out.println("parseCode");
    Codes instance = null;
    JSONObject expResult = null;
    JSONObject result = instance.parseCode();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of toString method, of class Codes.
   */
  public void testToString() {
    System.out.println("toString");
    Codes instance = null;
    String expResult = "";
    String result = instance.toString();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
