/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.security.GestorSesion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
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
public class GestorServerTest {

private static GestorServer gestor;
private static Usuario usuarioCorrecto;
private static Usuario usuarioCorrectoAdmin;
private static TokenSesion tokenCorrecto;
private ServerSocket ss;
private Socket scServer;
private Socket scCliente;
private PrintStream serverOut;
private BufferedReader clienteIn;
private static final Gson gson= new Gson();

public GestorServerTest() {
}

@BeforeClass
public static void setUpClass() {
  gestor = new GestorServer(null, null, new GestorSesion());
  usuarioCorrecto = new Usuario ("karibdys", "manuPass", "manu@tukarta.com", "manu", "mora", false);
  usuarioCorrectoAdmin = new Usuario ("marc", "marcPass", "marc@tukarta.com", "marc", "abad", true);
  tokenCorrecto = new TokenSesion(usuarioCorrecto);
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
  ******************
  PROCESS MENSAJE LOGIN
  ******************
  */
  /**
   * Comprueba que el mensaje que se manda es de código 40 (datos incorrectos) cuando se envía un usuario null
   * Peticion LOGIN
   */
  @Test
  public void testProcessMensajeLoginNull() {
    mostrarMetodo("Login con user nulo");
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
    mostrarMetodo("Login Admin con user nulo");
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
    mostrarMetodo("Login con email nulo");
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
    mostrarMetodo("Login Admin con email nulo");
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
    mostrarMetodo("Login con password nula");
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
    mostrarMetodo("Login Admin con password nula");
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
    mostrarMetodo("Login correcto");
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
    mostrarMetodo("Login Admin correcto");  
    boolean isGestor = true;    
    String expResult = Codes.CODIGO_OK;    
    MensajeRespuesta result = gestor.processMensajeLogin(usuarioCorrectoAdmin, isGestor);
    String codigo="";
    if (result!=null){
      codigo = result.getCode().getCode();
       System.out.println("Mensaje no nulo");
    } else{
      System.out.println("Mensaje nulo");
    }   
    //comprobamos que está el Token
    if (result.getData()!=null){
      assertEquals(expResult, codigo);
    }else{
      fail();
    }    
  }
  
  /*
  ******************
  PROCESS MENSAJE LOGOUT
  ******************
  */
  
  /**
   * Comprueba que se genera un mensaje correcto con código 40 si el Token introducido es nulo
   */
  @Test
  public void testPorcessMensajeLogoutNull(){
    mostrarMetodo("Logout con ToksenSesion nulo");
    MensajeRespuesta result = gestor.procesarMensajeLogout(null);
    String codigo = result.getCode().getCode();
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    assertEquals(expResult, codigo);
  }
 /**
   * Comprueba que se genera un mensaje correcto con código 40 si el Token introducido no es nulo pero tiene un usuario nulo
   */
  @Test
  public void testPorcessMensajeLogoutUserNull(){
    mostrarMetodo("Logout con TokenSesion no nulo pero user nulo");
    TokenSesion token = new TokenSesion(usuarioCorrecto);
    token.setUsuario(null);    
    MensajeRespuesta result = gestor.procesarMensajeLogout(token);
    String codigo = result.getCode().getCode();
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    assertEquals(expResult, codigo);
  }
    
  /**
   * Comprueba que se genera un mensaje correcto con código 40 si el Token introducido no es nulo pero tiene un token nulo
   */
  @Test
  public void testPorcessMensajeLogoutTokenNull(){
    mostrarMetodo("Logout con TokenSesion no nulo pero token nulo");
    TokenSesion token = new TokenSesion(usuarioCorrecto);
    token.setToken(null);    
    MensajeRespuesta result = gestor.procesarMensajeLogout(token);
    String codigo = result.getCode().getCode();
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    assertEquals(expResult, codigo);
  }  
  
   /**
   * Comprueba que se genera un mensaje correcto con código 44 si el Token introducido no es nulo pero la sesión no está abierta en el servidor
   */
  @Test
  public void testPorcessMensajeLogoutNoSesion(){   
    mostrarMetodo("Logout de un token sin sesion");
    MensajeRespuesta result = gestor.procesarMensajeLogout(tokenCorrecto);
    System.out.println("-->"+result);
    String codigo = result.getCode().getCode();
    String expResult = Codes.CODIGO_NO_SESION;    
    assertEquals(expResult, codigo);
  }  
  
   /**
   * Comprueba que se genera un mensaje correcto con código 10 si el Token introducido no es nulo y hay una sesión en el gestor que cierra
   */
  @Test
  public void testPorcessMensajeLogoutSiSesion(){    
    mostrarMetodo("Logout de un token registrado");
    //metemos la sesión en el gestor
    GestorSesion gestorSesion = new GestorSesion();
    gestorSesion.addSesion(tokenCorrecto);
    //obligamos al server que registre este gestor de sesión en su contenido
    gestor.setGestorSesion(gestorSesion);
    //lanzamos el mensaje
    MensajeRespuesta result = gestor.procesarMensajeLogout(tokenCorrecto);
    String codigo = result.getCode().getCode();
    String expResult = Codes.CODIGO_OK;    
    assertEquals(expResult, codigo);
  }  
  
  /*
  ******************
  SEND MENSAJE
  ******************
  */
  
  //métodos auxiliares
  public void initCon() throws IOException{
    ss = new ServerSocket(200);
    scServer = new Socket ("localhost", 200);
    //aceptar conexión
    scCliente = ss.accept();
    serverOut = new PrintStream(scServer.getOutputStream());
    
    clienteIn = new BufferedReader(new InputStreamReader(scCliente.getInputStream()));   

  }
  
  public void closeCon() throws IOException{    
    if (clienteIn!=null){
      clienteIn.close();
      System.out.println("Canal cliente cerrado");
    }else{
      System.out.println("El clienteIn ya estaba cerrado");
    }
    if (serverOut!=null){            
      serverOut.close();
      System.out.println("Canal server cerrado");
    }if (scCliente.isConnected()){
      scCliente.close();
      System.out.println("Socket cliente cerrado");    
    }if (scServer.isConnected()){      
      scServer.close();
      System.out.println("Socket server cerrado");
    }if (ss!=null){      
      ss.close();
      System.out.println("SockerServer cerrado");
    }    
  }
  
  /**
   * Comprueba que el envía un MensajeRespuesta con solo códigos y petición correctamente
   * @throws IOException 
   */
  @Test
  public void testSendMensajeCodigoPetOK() throws IOException {
    mostrarMetodo("sendMensaje con dos parámetros correcto");
    try{
      initCon();  
      //fabricar mensaje
      MensajeRespuesta mensaje = new MensajeRespuesta(new Codes(Codes.CODIGO_OK), "login");
      String mensajeJSON = gson.toJson(mensaje);
      gestor.setOut(serverOut);
      gestor.sendMensaje(mensaje);
      serverOut.close();
      String mensajeJSONRecibido = clienteIn.readLine();
      clienteIn.close(); 
      assertEquals(mensajeJSON, mensajeJSONRecibido);       
    } catch (IOException ex) {
      fail();
    }finally{
      closeCon();
    }    
  }
  
  /**
   * Comprueba que el envía un MensajeRespuesta con código, petición y data correctamente 
   * @throws IOException 
   */
  @Test
  public void testSendRespuestaCodigoPetData() throws IOException {
    mostrarMetodo("sendMensaje con tres parámetros correcto");
    try{
      initCon();  
      //fabricar mensaje
      MensajeRespuesta mensaje = new MensajeRespuesta(new Codes(Codes.CODIGO_OK), "login", tokenCorrecto);
      String mensajeJSON = gson.toJson(mensaje);
      gestor.setOut(serverOut);
      gestor.sendMensaje(mensaje);
      serverOut.close();
      String mensajeJSONRecibido = clienteIn.readLine();
      clienteIn.close(); 
      assertEquals(mensajeJSON, mensajeJSONRecibido);    
    } catch (IOException ex) {
      fail();
    }finally{
      closeCon();
    }    
  }
  
  /**
   * Comprueba que el envía un MensajeRespuesta con código, petición, data y Usuario correctamente 
   * @throws IOException 
   */
  @Test
  public void testSendRespuestaCodigoPetDataUser() throws IOException {
    mostrarMetodo("sendMensaje con cuatro parámetros correcto");
    try{
      initCon();  
      //fabricar mensaje
      MensajeRespuesta mensaje = new MensajeRespuesta(new Codes(Codes.CODIGO_OK), "login", tokenCorrecto, usuarioCorrecto);
      String mensajeJSON = gson.toJson(mensaje);
      gestor.setOut(serverOut);
      gestor.sendMensaje(mensaje);
      serverOut.close();
      String mensajeJSONRecibido = clienteIn.readLine();
      clienteIn.close(); 
      assertEquals(mensajeJSON, mensajeJSONRecibido);    
    } catch (IOException ex) {
      fail();
    }finally{
      closeCon();
    }    
  }  
  
  /**
   * Comprueba el comportamiento de un mensaje que es completamente nulo
   * @throws IOException 
   */
  @Test
  public void testSendRespuestaCodigoNull() throws IOException {
    mostrarMetodo("sendMensaje con un mensaje nulo");
    try{
      initCon();  
      //fabricar mensaje
      MensajeRespuesta mensaje = null;
      String mensajeJSON = gson.toJson(mensaje);
      gestor.setOut(serverOut);
      gestor.sendMensaje(mensaje);
      serverOut.close();
      String mensajeJSONRecibido = clienteIn.readLine();
      clienteIn.close(); 
      assertEquals(mensajeJSON, mensajeJSONRecibido);    
    } catch (IOException ex) {
      fail();
    }finally{
      closeCon();
    }    
  }
  
  public void mostrarMetodo(String metodo){
    System.out.println("--------------------------\n"+metodo+"\n--------------------------\n");  
  }
  
}
