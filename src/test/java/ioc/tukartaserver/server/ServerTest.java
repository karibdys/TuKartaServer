/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.server;

import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.security.GestorSesion;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Manu Mora
 */
public class ServerTest extends TestCase {

private Server serverPrueba;
private Usuario userConSesion;
private Usuario userSinSesion;
private String HOST = "localhost";
private int PORT = 200;


public ServerTest(String testName) {
  super(testName);
  userConSesion = new Usuario("karibdys", "manuPass", "manu@tukarta.com", null, null, false);    
  userSinSesion = new Usuario("Marc", "marcPass", "marc@tukarta.com", null, null, true);
}

@BeforeClass
public void serverOn(){
  serverPrueba = new Server();
  serverPrueba.start();
  System.out.println("Server Iniciado");
}
@AfterClass
public void serverOff() throws IOException{
  serverPrueba.closeServer();
}

@Override
protected void tearDown() throws Exception {
  super.tearDown();   
}
/*

*/@Test
  public void testStartServer() throws IOException {    
    System.out.println("PRUEBA 1: Iniciando prueba de startServer");  
    /*
    serverPrueba = new Server();
    serverPrueba.start();
    System.out.println("PRUEBA 1: Server Iniciado");
    */
    try{   
      Socket socketPrueba = new Socket(HOST, PORT);
      socketPrueba.close();
      assert(true);
    }catch (Exception e){   
      System.out.println("PRUEBA 1: ha dado error");
      System.out.println("PRUEBA 1: "+e.getMessage());
      assert(false);
    }          
    
  }

  /**
   * Test of comprobarSesion method, of class Server.
   */
  @Test
  public void testComprobarSesionUserIn() {
    System.out.println("PRUEBA 2: Iniciando prueba de comprobarSesion");
    System.out.println("PRUEBA 2: Server iniciado");
    serverPrueba = new Server();
    serverPrueba.start();
    try {
      Socket socketPrueba = new Socket (HOST, PORT);
    } catch (IOException ex) {
      Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println("PRUEBA 2: Server Iniciado");
    GestorSesion gestorSesion=null;    
    if (serverPrueba==null){      
      System.out.println("PRUEBA 2: Sin serverPrueba");
    }else{
      System.out.println("PRUEBA 2:  serverPrueba");
      gestorSesion= serverPrueba.getGestorSesiones();  
    }    
    TokenSesion token =new TokenSesion(userConSesion);
         
    if (gestorSesion.addSesion(token)){
      System.out.println ("PRUEBA 2: Token añadido");

      if (serverPrueba.comprobarSesion(token)){
        assertTrue(true);
      }else{
        assertTrue(false);    
      }              
    }else{
      System.out.println ("PRUEBA 2: Token no añadido");      
      assertTrue(false);
    }
    
  try {
    serverPrueba.closeServer();
  } catch (IOException ex) {
    Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
  }
  }

  public void testComprobarSesionUserOff() {
    GestorSesion gestorSesion = serverPrueba.getGestorSesiones();
    TokenSesion token = new TokenSesion(userSinSesion);
    
    if (!serverPrueba.comprobarSesion(token)){
      assertTrue(true);
    }else{
      assertTrue(false);
    }             
  }
  /*
   
  public void testEndConnection() {
    System.out.println("endConnection");
    Server instance = new Server();
    instance.endConnection();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  
  public void testCloseServer() throws Exception {
    System.out.println("closeServer");
    Server instance = new Server();
    instance.closeServer();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

*/
}
