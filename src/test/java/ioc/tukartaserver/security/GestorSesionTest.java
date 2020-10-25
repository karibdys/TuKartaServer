package ioc.tukartaserver.security;

import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Manu Mora
 */
public class GestorSesionTest {
private static Usuario userK;
private static Usuario userM;
private static Usuario userZ;
private static GestorSesion gestor;
private static TokenSesion tokenK;
private static TokenSesion tokenM;
private static TokenSesion tokenZ;
TokenSesion tokenNull = null;

public GestorSesionTest() {
}

@BeforeClass
public static void setUpClass() {
  gestor = new GestorSesion();
  userK = new Usuario ("karibdys", "manuPass", "manu@manu.com", "manu", "mora", false);
  userM = new Usuario ("karibdys2", "manuPass", "manu@manu2.com", "manu", "mora", false);
  userZ = new Usuario ("karibdys3", "manuPass", "manu@manu3.com", "manu", "mora", false);
  tokenK = new TokenSesion(userK);
  tokenM = new TokenSesion(userM);
  tokenZ = new TokenSesion(userZ);
  gestor.addSesion(tokenZ);
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

  /*  
  *************
  ADD SESION
  ***************
  */

   /**
   * Comprueba el funcionamiento de un intento de añadir sesión ya está en la lista de sesiones. 
   */
  @Test
  public void testAddSesionSesionExiste() {
    System.out.println("PRUEBA addSesion -- no añade");
    gestor.addSesion(tokenK);
    boolean expResult = false;
    boolean result = gestor.addSesion(tokenK);
    assertEquals(expResult, result);        
  }
   /**
   * Comprueba el funcionamiento de un intento de añadir sesión con una sesión que aún no está en la lista de sesiones.
   */
  @Test
  public void testAddSesionSesionNoExiste() {
    System.out.println("PRUEBA addSesion -- sí añade"); 
    //nos aseguramos de que este token no está:
    gestor.removeSesion(tokenM);
    boolean expResult = true;
    boolean result = gestor.addSesion(tokenM);
    assertEquals(expResult, result);        
  }   
     /**
   * Comprueba el funcionamiento de un intento de añadir sesión con un TokenSesion que es nulo
   */
  @Test
  public void testAddSesionSesionTokenNull() {
    System.out.println("PRUEBA addSesion -- token null"); 
    //nos aseguramos de que este token no está: 
    boolean expResult = false;
    boolean result = gestor.addSesion(tokenNull);
    assertEquals(expResult, result);        
  }  

  /*  
  *************
  REMOVE SESION
  ***************
  */
  
   /**
   * Comprueba que una sesión se elimina de la lista correctamente cuando se le pasa un TokenSesion con una sesión que existe en la lista. 
   */
  @Test
  public void testRemoveSesionElimina() {
    System.out.println("PRUEBA removeSesion -- sí borra");    
    //nos aseguramos de que esta sesión está
    gestor.addSesion(tokenZ);
    boolean expResult = true;
    boolean result = gestor.removeSesion(tokenZ);
    assertEquals(expResult, result);

  }
  
   /**
   * Comprueba que una sesión no se elimina de la lista cuando se le pasa un TokenSesion con una sesión que no existe en la lista
   */
  @Test
  public void testRemoveSesionNoElimina() {
    System.out.println("PRUEBA removeSesion -- no borra");  
    //nos aseguramos de que el Token no está en la lista
    gestor.removeSesion(tokenZ);
    boolean expResult = false;
    boolean result = gestor.removeSesion(tokenZ);
    assertEquals(expResult, result);
  }
  /**
   * Comprueba que una sesión no se elimina de la lista cuando se le pasa un TokenSesion con valor null
   */
  @Test
  public void testRemoveSesionTokenNull() {
    System.out.println("PRUEBA removeSesion -- token null");  
    //nos aseguramos de que el Token no está en la lista    
    boolean expResult = false;
    boolean result = gestor.removeSesion(tokenNull);
    assertEquals(expResult, result);
  }
  
  /*  
  *************
  IS TOKEN (STRING)
  ***************
  */

  /**
   * Comprueba que devuelve false si el parámetro pasado no está en el listado
   */
  @Test
  public void testIsTokenStringSesionNoExiste() {
    System.out.println("PRUEBA isToken -- Strign y la sesión no está en el listado");         
    boolean expResult = false;    
    gestor.removeSesion(tokenM);
    boolean result = gestor.isToken(tokenM.getUsuario());
    assertEquals(expResult, result);
  }

  /**
   * Comprueba que devuelve true si el parámetro pasado está en el listado
   */
  @Test
  public void testIsTokenStringSesionExiste() {
    System.out.println("PRUEBA isToken -- String y la sesión está en el listado");         
    boolean expResult = true;
    gestor.addSesion(tokenM);
    boolean result = gestor.isToken(tokenM.getUsuario());
    assertEquals(expResult, result);
  }
   
  /**
   * Comprueba que devuelve false y no se rompe la app si el parámetro pasado es nulo
   */
  @Test
  public void testIsTokenStringNull() {
    System.out.println("PRUEBA isToken -- String y con token null");         
    boolean expResult = false;    
    boolean result = gestor.isToken((String)null);
    assertEquals(expResult, result);
  }
  
  /*  
  *************
  IS TOKEN (USUARIO)
  ***************
  */
  
  /**
   * Comprueba que devuelve false si se le pasa un Usuario que no tiene abierta sesión en el listado de sesiones
  */  
  @Test
  public void testIsTokenUserSinSesion() {
    //comprobamos que funciona con un usuario nulo
    System.out.println("PRUEBA isToken con objeto Usuario -- la sesión no está");         
    gestor.removeSesion(tokenK);
    boolean expResult = false;    
    boolean result = gestor.isToken(userK);
    assertEquals(expResult, result);
  }
  /**
   * Comprueba que devuelve false si se le pasa un Usuario con un parámetro "usuario" con valor nulo.
  */  
  @Test
  public void testIsTokenUserNull() {
    //comprobamos que funciona con un usuario nulo
    System.out.println("PRUEBA isToken con objeto Usuario -- el usuario es un campo Nulo");         
    Usuario userNull = new Usuario  (null, "manuPass", "manu@manu3.com", "manu", "mora", false);        
    boolean expResult = false;    
    boolean result = gestor.isToken(userNull);
    assertEquals(expResult, result);
  }
  
  /**
   * Comprueba que devuelve true si una sesión está dada de alta en el listado de sesiones y se le pasa un parámetro correcto.
  */  
  @Test
  public void testIsTokenUserConSesion() {
    System.out.println("PRUEBA isToken con objeto Usuario -- la sesión está");             
    boolean expResult = true;
    gestor.addSesion(tokenM);
    boolean result = gestor.isToken(userM);
    assertEquals(expResult, result);
  }

}
