package ioc.tukartaserver.gestorDB;

import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.model.Utiles;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase que controla el funcionamiento del gestor de la base de datos. 
 * @author Manu Mora
 */
public class GestorDBTest {
private static GestorDB gestor;
private static String USER_CORRECTO="manu@tukarta.com";
private static String USER_CORRECTO_ADMIN="marc@tukarta.com";
private static String USER_INCORRECTO="random@tukarta.com";
private static String PASS_CORRECTA="manuPass";
private static String PASS_CORRECTA_ADMIN="marcPass";
private static String PASS_INCORRECTA="randomPass";
private static String MAIL_PRUEBAS = "prueba@tukarta.com";

public GestorDBTest() {
}

@BeforeClass
public static void setUpClass() throws SQLException, ClassNotFoundException {
  gestor = new GestorDB(); 
}

@AfterClass
public static void tearDownClass() {
  gestor.closeConnection();  
}

@Before
public void setUp() {
}

@After
public void tearDown() {
}

  /*  
  *************
   LOGIN
  ***************
  */

  /**
   * Comprueba que el método devuelve un mensaje con código 40 en caso de recibir datos nulos.
   * La petición que recibe es de tipo LOGIN normal. 
   */
  @Test
  public void datosNullLogin(){
    String userNull=null;
    String passNull=null;    
    String user="userCualquiera";
    String pass="passCualquiera";
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;
    //primero comprobamos que devuelve un código 40 si el usuario es nulo y la pass no
    //también comprobamos que la petición recibida es de LOGIN
    MensajeRespuesta res =gestor.loginMens(userNull, pass, false);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (codeResult.equals(expResult) && petResult.equals(Mensaje.FUNCION_LOGIN)){
      //si se cumple el primero,
      //comprobamos que devuelve un código 40 si el usuario no es nulo y la pass sí
      codeResult = gestor.loginMens(user, passNull, false).getCode().getCode();            
    }
    //comprobamos que se cumple el segundo requisito.
    assertEquals(expResult, codeResult);
  }
  
  /**
   * Comprueba que el método devuelve un mensaje con código 40 en caso de recibir datos nulos.
   * La petición que recibe es de tipo LOGIN ADMIN. 
   */
  @Test
  public void datosNullLoginAdmin(){
    String userNull=null;
    String passNull=null;    
    String user="userCualquiera";
    String pass="passCualquiera";
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;
    //primero comprobamos que devuelve un código 40 si el usuario es nulo y la pass no
    //también comprobamos que la petición recibida es de LOGIN_ADMIN
    MensajeRespuesta res =gestor.loginMens(userNull, pass, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (codeResult.equals(expResult) && petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){
      //si se cumple el primero,
      //comprobamos que devuelve un código 40 si el usuario no es nulo y la pass sí
      codeResult = gestor.loginMens(user, passNull, true).getCode().getCode();            
    }
    //comprobamos que se cumple el segundo requisito.
    assertEquals(expResult, codeResult);
  }
  
  /**
   * Comprueba que devuelve correctamente un código 61 cuando un usuario no se encuentra en la base de datos
   * Petición: LOGIN
   */
  
  @Test
  public void login_usuarioNoEncontradoNoAdmin(){
     
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;
    
    MensajeRespuesta res =gestor.loginMens(USER_INCORRECTO, PASS_INCORRECTA, false);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }
  
  /**
   * Comprueba que devuelve correctamente un código 61 cuando un usuario no se encuentra en la base de datos
   * Petición: LOGIN_ADMIN
   */
  @Test
  public void login_usuarioNoEncontradoAdmin(){       
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;    
    MensajeRespuesta res =gestor.loginMens(USER_INCORRECTO, PASS_INCORRECTA, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }
  
  
  /**
   * Comprueba que devuelve correctamente un código 61 cuando un usuario correcto intenta acceder a una sesión de administrador
   * Petición: LOGIN_ADMIN
   */

  @Test
  public void login_usuarioEncontradoNoAdmin(){       
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;    
    MensajeRespuesta res =gestor.loginMens(USER_CORRECTO, PASS_CORRECTA, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }

  
  /**
   * Comprueba que devuelve correctamente un código 62 cuando un usuario correcto accede con una pass incorretca
   * Petición: LOGIN
   */
  
  @Test
  public void login_usuarioEncontradoNoPassNoAdmin(){      
    String expResult = Codes.CODIGO_ERR_PWD;    
    MensajeRespuesta res =gestor.loginMens(USER_CORRECTO, PASS_INCORRECTA, false);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }
  
    
  /**
   * Comprueba que devuelve correctamente un código 62 cuando un usuario correcto accede con una pass incorretca
   * Petición: LOGIN_ADMIN
   */
  @Test
  public void login_usuarioEncontradoNoPassAdmin(){        
    String expResult = Codes.CODIGO_ERR_PWD;    
    MensajeRespuesta res =gestor.loginMens(USER_CORRECTO_ADMIN, PASS_INCORRECTA, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }
  
 
  /**
   * Comprueba que devuelve correctamente un código 10 con token cuando un usuario correcto accede con una pass correcta
   * Petición: LOGIN
   */  
  @Test
  public void login_usuarioEncontradoPassOkNoAdmin(){        
    String expResult = Codes.CODIGO_OK;    
    MensajeRespuesta res =gestor.loginMens(USER_CORRECTO, PASS_CORRECTA, false);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN)){      
      fail();
    }else{
      if (res.getData()!=null){
        assertEquals(expResult, codeResult); 
      }else{
        fail();
      }     
    }    
  }
  

  
  /**
   * Comprueba que devuelve correctamente un código 10 con token cuando un usuario correcto accede con una pass correcta
   * Petición: LOGIN_ADMIN
   */ 
  @Test
  public void login_usuarioEncontradoPassOkAdmin(){        
    String expResult = Codes.CODIGO_OK;    
    MensajeRespuesta res =gestor.loginMens(USER_CORRECTO_ADMIN, PASS_CORRECTA_ADMIN, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){      
      fail();
    }else{
      if (res.getData()!=null){
        assertEquals(expResult, codeResult); 
      }else{
        fail();
      }     
    }    
  }
  
  
  /*  
  *************
   CONSTRUCTOR SENTENCIA LOGIN
  ***************
  */
  
  /**
   * Comprueba que se construye la sentencia correctamente en caso de petición LOGIN
   */
  @Test
  public void constructorSentenciaLoginNoAdmin(){
    String email = "miMail";
    boolean isGestor = false;
    String sentencia = gestor.constructorSentenciaLogin(email, isGestor);
    String expResult = "select * from usuario where email='"+email+"'".toLowerCase();
    assertEquals (expResult, sentencia);            
  }
  /**
   * Comprueba que se construye la sentencia correctamente en caso de petición LOGIN_ADMIN
   */
  @Test
  public void constructorSentenciaLoginAdmin(){
    String email = "miMail";
    boolean isGestor = true;
    String sentencia = gestor.constructorSentenciaLogin(email, isGestor);
    String expResult = "select * from usuario where email='"+email+"' and isGestor='true'".toLowerCase();
    assertEquals (expResult, sentencia);            
  }
  
   /*  
  *************
   CREATE USER FROM RESULT
  ***************
  */
  
  /**
   * Comprueba que se crea un usuario correcto a partir de un usuario gestor
   * @throws SQLException 
   */
  @Test
  public void construirUserFromResult_GestorOK() throws SQLException{
    String email = "marc@tukarta.com";
    boolean isGestor =true;
    Usuario expResult = new Usuario("Marc", null, email, "Marc", "Abad", isGestor);
    Connection con = gestor.getCon();
    PreparedStatement stm = con.prepareStatement(GestorDB.SELECT_USER);
    stm.setString(1, email);
    ResultSet result = stm.executeQuery();
    result.next();
    Usuario resultado = gestor.createUserFromResult(result, isGestor);
    assertEquals(expResult.getEmail(), resultado.getEmail());
  }
  
    /**
   * Comprueba que se crea un usuario correcto a partir de un usuario empleado
   * @throws SQLException 
   */
  @Test
  public void construirUserFromResult_EmpleadoOK(){
    System.out.println("\n**********************\nPrueba CONSTRUIR EMPLEADO DESDE RESULT\n");
    //metemos el usuario de pruebas en la BD
    try{
      insertUserPrueba();
    }catch (SQLException ex){
      System.err.println("Usuario repetido");
    }     
    String email = "prueba@tukarta.com";    
    //empleado que esperamos recibir

    Empleado empEsperado = new Empleado();
    empEsperado.setUsuario("Prueba");
    empEsperado.setEmail(email);
    System.out.println(empEsperado);
    ResultSet result = null;
    Usuario resultado = null;
    try{
      Connection con = gestor.getCon();
      PreparedStatement stm = con.prepareStatement(GestorDB.SELECT_USER);
      stm.setString(1, email);
      result = stm.executeQuery();
      result.next();
      resultado = gestor.createUserFromResult(result, false);
    }catch (SQLException ex){
      fail("Fallo en la base de datos");
    }
    System.out.println("usuario esperado: "+empEsperado);
    System.out.println("\nusuario recibido: "+resultado);
    
    try{
      deleteUserPrueba();
    }catch(SQLException ex){
      System.err.println("Usuario no borrado con éxito");
    }
    //nos bastará con ver que los datos básicos son buenos
    assertEquals(empEsperado.getEmail(), resultado.getEmail());
  }
  
 /*  
  *************
   SELECT DATA USER
  ***************
  */
  
  /**
   * Comprueba que el mensaje que retorna el método lleva por código un 40 (datos erróneos)
   */
  @Test
  public void selectDataUser_EmpleadoEmailNull(){
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;    
    String email =null;
    MensajeRespuesta res =gestor.selectDataUser(email);
    String codeResult = res.getCode().getCode();
    assertEquals(expResult, codeResult);    
  }
  
  /**
   * Comprueba que el mensaje que retorna el método lleva por código un 42 (usuario no encontrado) al pasarle un usuario que no está en la base de datos
   */
  @Test
  public void selectDataUser_usuarioNoExiste(){
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;    
    String email = USER_INCORRECTO;
    MensajeRespuesta res =gestor.selectDataUser(email);
    String codeResult = res.getCode().getCode();
    assertEquals(expResult, codeResult);    
  }
  
    
  /**
   * Comprueba que el mensaje que retorna el método lleva por código un 10 al pasarle un usuario gestor que está en la base de datos
   */
  @Test
  public void selectDataUser_gestorOK(){
    String expResult = Codes.CODIGO_OK;        
    String email ="marc@tukarta.com";  
    MensajeRespuesta res =gestor.selectDataUser(email);
    String codeResult = res.getCode().getCode();    
    assertEquals(expResult, codeResult);    
  }
  
  
   /**
   * Comprueba que el mensaje que retorna el método lleva por código un 10 al pasarle un usuario empleado que está en la base de datos
   */

  public void selectDataUser_empleadoOK(){
    String expResult = Codes.CODIGO_OK;   
    String email ="manu@tukarta.com";  
    MensajeRespuesta res =gestor.selectDataUser(email);
    String codeResult = res.getCode().getCode();
    assertEquals(expResult, codeResult);    
  }  
  
  
  /*  
  *************
   BAJA USER
  ***************
  */
  
  /**
   * Comprueba que el mensaje que retorna el método lleva un código 10 al pasarle un email válido que está en la base de datos y puede ser dado de baja
   */
  @Test
  public void bajaUser_empleadoCorrecto() {
    //metemos un usuario en la base de datos para que esté dado de alta
    try{
      insertUserPrueba();
    }catch (SQLException ex){
      fail("No se ha podido hacer la prueba por problemas al conectar con la base de datos");
    }
    //damos de baja al usuario
    MensajeRespuesta res = gestor.bajaUser(MAIL_PRUEBAS);   
    //cuando hemos hecho todo en la base de datos, borramos el usuario
    try{
      deleteUserPrueba();
    }catch(SQLException ex){
      fail("No se ha podido hacer la prueba por problemas al conectar con la base de datos");
    }
    //Esperamos un código 10
    String expResult = Codes.CODIGO_OK;
    String resultadoReal = res.getCode().getCode();
    assertEquals(expResult, resultadoReal);    
  }
  
  /**
   * Comprueba que el mensaje que retorna el método lleva un código 40 ya que se le pasa un usuario null
   */
  @Test
  public void bajaUser_empleadoNull() {    
    MensajeRespuesta res = gestor.bajaUser(null);       
    //Esperamos un código 40
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;
    String resultadoReal = res.getCode().getCode();
    assertEquals(expResult, resultadoReal);    
  }
  
   /**
   * Comprueba que el mensaje que retorna el método lleva un código 42 al introducir un email que no está en la base de datos
   */
  @Test
  public void bajaUser_empleadoNoEnBD() {
    //damos de baja al usuario sin meterlo en la base de datos
    MensajeRespuesta res = gestor.bajaUser(MAIL_PRUEBAS);           
    //Esperamos un código 10
    String expResult = Codes.CODIGO_NO_USER;
    String resultadoReal = res.getCode().getCode();
    assertEquals(expResult, resultadoReal);    
  }
  
   
  /*  
  *************
   AUXILIARES
  ***************
  */
  
  /**
   * Inserta un usuario de prueba en la base de datos para poder trabajar con él
   * @throws SQLException SQLException si hay errores al acceder o ejecutar la sentencia INSERT
   */
  public void insertUserPrueba() throws SQLException{
    Connection con = gestor.getCon();
    String sentenciaInsert = "INSERT INTO usuario (\"usuario\", \"pwd\", \"email\", \"fecha_alta\", \"fecha_modificacion\", \"isgestor\") VALUES ('Prueba', 'pruebaPass', '"+MAIL_PRUEBAS+"', '2020-11-20', '2020-11-20', false)";
    PreparedStatement stm = con.prepareStatement(sentenciaInsert);
    stm.executeUpdate();
    stm.close();    
  }
  
  /**
   * Elimina un usuario de prueba de la base de datos para poder volver a insertarlo cuando sea necesario
   * @throws SQLException SQLException si no se puede acceder a la base de datos o no se puede borrar el usuario
   */
  public void deleteUserPrueba() throws SQLException{
     Connection con = gestor.getCon();
     String sentenciaDelete = "DELETE FROM usuario where email = '"+MAIL_PRUEBAS+"'";   
     PreparedStatement stm = con.prepareStatement(sentenciaDelete);
     stm.executeUpdate();
     stm.close();    
  }
}
