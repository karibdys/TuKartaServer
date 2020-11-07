package ioc.tukartaserver.model;

import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase para probar las funcionalidades del modelo Restaurante
 * @author Manu Mora
 */
public class RestauranteTest {

public RestauranteTest() {
}

@BeforeClass
public static void setUpClass() {
}

@AfterClass
public static void tearDownClass() {
}

@Before
public void setUp() {
}

@After
public void tearDown() {
}

 
/******************
 * MÉTODOS ADDMESA
 ******************
 */

@Test
public void testAddMesa() {
  System.out.println("addMesa");
  Mesa mesa = null;
  Restaurante instance = null;
  instance.addMesa(mesa);
  fail("The test case is a prototype.");
}

@Test
public void testRemoveMesa() {
  System.out.println("removeMesa");
  Mesa mesa = null;
  Restaurante instance = null;
  boolean expResult = false;
  boolean result = instance.removeMesa(mesa);
  assertEquals(expResult, result);
  fail("The test case is a prototype.");
  }

}
