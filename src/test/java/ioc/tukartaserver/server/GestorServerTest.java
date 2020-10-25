/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.server;

import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Manu
 */
public class GestorServerTest {
private static GestorServer gestor;
private static Usuario usuarioCorrecto;
private static Usuario usuarioCorrectoAdmin;


public GestorServerTest() {
}

@BeforeClass
public static void setUpClass() {
  gestor = new GestorServer(null, null);
  usuarioCorrecto = new Usuario ("karibdys", "manuPass", "manu@tukarta.com", "manu", "mora", false);
  usuarioCorrectoAdmin = new Usuario ("marc", "marcPass", "marc@tukarta.com", "marc", "abad", true);
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

  /**
   * Comprueba que el mensaje que se manda es de código 40 (datos incorrectos) cuando se envía un usuario null
   * Peticion LOGIN
   */
  @Test
  public void testProcessMensajeLoginNull() {
    System.out.println("processMensajeLogin");
    Usuario usuario = null;
    boolean isGestor = false;
    
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuario, isGestor);
    String codigo = result.getCode().getCode();
    assertEquals(expResult, codigo);
  }
  /**
   * Comprueba que el mensaje que se manda es de código 40 (datos incorrectos) cuando se envía un usuario null
   * Peticion LOGIN_ADMIN
   */
  @Test
  public void testProcessMensajeLoginAdminNull() {
    System.out.println("processMensajeLogin");
    Usuario usuario = null;
    boolean isGestor = true;
    
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuario, isGestor);
    String codigo = result.getCode().getCode();
    assertEquals(expResult, codigo);
  }

   /**
   * Comprueba que el mensaje que se manda es de código 40 (datos incorrectos) cuando se envía un usuario no nulo pero con email nulo
   * Peticion LOGIN
   */
  @Test
  public void testProcessMensajeLoginEmailNull() {
    System.out.println("processMensajeLogin");
    Usuario usuario = new Usuario ("karibdys", "manuPass", null, "manu", "mora", false);
    boolean isGestor = false;    
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuario, isGestor);
    String codigo = result.getCode().getCode();
    assertEquals(expResult, codigo);
  }
     /**
   * Comprueba que el mensaje que se manda es de código 40 (datos incorrectos) cuando se envía un usuario no nulo pero con email nulo
   * Peticion LOGIN_ADMIN
   */
  @Test
  public void testProcessMensajeLoginAdminEmailNull() {
    System.out.println("processMensajeLogin");
    Usuario usuario = new Usuario ("karibdys", "manuPass", null, "manu", "mora", false);
    boolean isGestor = true;    
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuario, isGestor);
    String codigo = result.getCode().getCode();
    assertEquals(expResult, codigo);
  }
  
     /**
   * Comprueba que el mensaje que se manda es de código 40 (datos incorrectos) cuando se envía un usuario no nulo pero con password nula
   * Peticion LOGIN
   */
  @Test
  public void testProcessMensajeLoginPwdNull() {
    System.out.println("processMensajeLogin");
    Usuario usuario = new Usuario ("karibdys", null, "manu@tukareta.com", "manu", "mora", false);
    boolean isGestor = false;    
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuario, isGestor);
    String codigo = result.getCode().getCode();
    assertEquals(expResult, codigo);
  }
  
     /**
   * Comprueba que el mensaje que se manda es de código 40 (datos incorrectos) cuando se envía un usuario no nulo pero con password nula
   * Peticion LOGIN_ADMIN
   */
  @Test
  public void testProcessMensajeLoginAdminPwdNull() {
    System.out.println("processMensajeLogin");
    Usuario usuario = new Usuario ("karibdys", null, "manu@tukareta.com", "manu", "mora", false);
    boolean isGestor = true;    
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuario, isGestor);
    String codigo = result.getCode().getCode();
    assertEquals(expResult, codigo);
  }
  
   /**
   * Comprueba que el mensaje que se manda es de código 10 y además lleva un Token en él
   * Peticion LOGIN
   */
  @Test
  public void testProcessMensajeLoginUserOK() {
    System.out.println("processMensajeLogin");    
    boolean isGestor = false;    
    String expResult = Codes.CODIGO_OK;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuarioCorrecto, isGestor);
    String codigo = result.getCode().getCode();
    //comprobamos que se manda un Token
    if (result.getData()!=null){
      assertEquals(expResult, codigo);
    }else{
      fail();
    }
  }
  
   /**
   * Comprueba que el mensaje que se manda es de código 10 y además lleva un Token en él
   * Peticion LOGIN_ADMIN
   */
  @Test
  public void testProcessMensajeLoginAdminUserOK() {
    System.out.println("processMensajeLogin");    
    boolean isGestor = true;    
    String expResult = Codes.CODIGO_OK;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuarioCorrectoAdmin, isGestor);
    String codigo = result.getCode().getCode();
    //comprobamos que está el Token
    if (result.getData()!=null){
      assertEquals(expResult, codigo);
    }else{
      fail();
    }    
  }
  
  
  
  
  
  
  /**
   * Test of procesarLogout method, of class GestorServer.
   */
  @Test
  public void testProcesarLogout() {
    System.out.println("procesarLogout");
    TokenSesion token = null;
    GestorServer instance = null;
    instance.procesarLogout(token);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of sendCode method, of class GestorServer.
   */
  @Test
  public void testSendCode() {
    System.out.println("sendCode");
    Codes codigo = null;
    String peticion = "";
    GestorServer instance = null;
    instance.sendCode(codigo, peticion);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of sendRespuesta method, of class GestorServer.
   */
  @Test
  public void testSendRespuesta_4args() {
    System.out.println("sendRespuesta");
    Codes codigo = null;
    String peticion = "";
    String data = "";
    String dataUser = "";
    GestorServer instance = null;
    instance.sendRespuesta(codigo, peticion, data, dataUser);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of endConnection method, of class GestorServer.
   */
  @Test
  public void testEndConnection() {
    System.out.println("endConnection");
    GestorServer instance = null;
    instance.endConnection();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of sendRespuesta method, of class GestorServer.
   */
  @Test
  public void testSendRespuesta_MensajeRespuesta() {
    System.out.println("sendRespuesta");
    MensajeRespuesta mensaje = null;
    GestorServer instance = null;
    instance.sendRespuesta(mensaje);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
