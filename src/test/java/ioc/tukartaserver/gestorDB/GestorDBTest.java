package ioc.tukartaserver.gestorDB;

import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Pedido;
import ioc.tukartaserver.model.Usuario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

private static final String ID_PEDIDO_PRUEBAS = "201120/2021-mesa1CanMarc";
private static final String ID_PRODUCTO_PRUEBAS = "B001";
private static final String ID_MESA_PRUEBAS = "mesa1CanMarc";
private static final float PRECIO_FINAL_PRUEBAS =100;
private static final String ID_PROD_PRUEBAS = "Prueba001";
private static final String ID_REG_PROD_PRUEBAS = "registro1";

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
    gestor.openConnection();
    Connection con = gestor.getCon();
    PreparedStatement stm = con.prepareStatement(GestorDB.SELECT_USER);
    stm.setString(1, email);
    ResultSet result = stm.executeQuery();
    result.next();
    Usuario resultado = gestor.createUserFromResult(result, isGestor);
    gestor.closeConnection();
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
      gestor.openConnection();
      Connection con = gestor.getCon();
      PreparedStatement stm = con.prepareStatement(GestorDB.SELECT_USER);
      stm.setString(1, email);
      result = stm.executeQuery();
      result.next();
      resultado = gestor.createUserFromResult(result, false);
      gestor.closeConnection();
    }catch (SQLException ex){
      System.out.println(ex.getMessage());
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
    System.out.println("\n**********************\nPrueba BAJA USUARIO CORRECTO\n");

    //metemos un usuario en la base de datos para que esté dado de alta
    try{
      insertUserPrueba();
    }catch (SQLException ex){
       System.out.println(ex.getMessage());
      fail("No se ha podido hacer la prueba por problemas al conectar con la base de datos");
    }
    //damos de baja al usuario
    MensajeRespuesta res = gestor.bajaUser(MAIL_PRUEBAS);   
    //cuando hemos hecho todo en la base de datos, borramos el usuario
    try{
      deleteUserPrueba();
    }catch(SQLException ex){
      System.out.println(ex.getMessage());
      fail("error al borrar el usuario de prueba");
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
    System.out.println("\n**********************\nPrueba BAJA USUARIO NO EN LA BASE DE DATOS\n");

    //damos de baja al usuario sin meterlo en la base de datos
    MensajeRespuesta res = gestor.bajaUser(MAIL_PRUEBAS);           
    //Esperamos un código 10
    String expResult = Codes.CODIGO_NO_USER;
    String resultadoReal = res.getCode().getCode();
    assertEquals(expResult, resultadoReal);    
  }
  
  
  
    /*  
  *************
   UPDATE_PEDIDO
  ***************/
  @Test
  public void updateData_pedidoOK(){
    System.out.println("\n**********************\nPrueba UPDATE PEDIDO CORRECTO\n");

    MensajeRespuesta respuesta=null;

    float precioFinal =0;
    try {
      addPedidoPrueba();
      Pedido pedido = new Pedido();
      pedido.setId(ID_PEDIDO_PRUEBAS);
      pedido.setPrecio_final(PRECIO_FINAL_PRUEBAS);
      respuesta = gestor.updateData(pedido, Mensaje.FUNCION_UPDATE_PEDIDO);        
      gestor.openConnection();
      Connection con = gestor.getCon();
      PreparedStatement pstm = con.prepareStatement("SELECT precio_final FROM pedido WHERE id = '"+ID_PEDIDO_PRUEBAS+"'");
      ResultSet set = pstm.executeQuery();
      if (set.next()){
        precioFinal = set.getFloat(1);
      }
      deletePedidoPrueba();
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
      fail("Error de SQL");
    }
    String expResult = Codes.CODIGO_OK;
    String respuestaReal = respuesta.getCode().getCode();
    if (precioFinal == PRECIO_FINAL_PRUEBAS){
      assertEquals(expResult, respuestaReal);
    }else{
      fail("no se ha actualizado bien el pedido");
    }
    
  }
  
  @Test
  public void updateData_pedidoNO(){
    System.out.println("\n**********************\nPrueba UPDATE PEDIDO QUE NO ESTÁ EN LA BASE DE DATOS\n");
    MensajeRespuesta respuesta=null;
    Pedido pedido = new Pedido();
    pedido.setId("pedidoraro");
    pedido.setPrecio_final(PRECIO_FINAL_PRUEBAS);
    respuesta = gestor.updateData(pedido, Mensaje.FUNCION_UPDATE_PEDIDO);                          
    
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;
    String respuestaReal = respuesta.getCode().getCode();
    
    assertEquals(expResult, respuestaReal);            
  }
  
  /*  
  *************
   ADD_PRODUCTO_ESTADO (SIMPLE)
  ***************/
  
  /**
  * Comprueba que el mensaje que retorna el método lleva un código 10 al introducir un conjunto de datos correcto (id de producto y de pedido que existen en la BD) y con un estado nulo
  */
  @Test
  public void addProductoEstado_productoSi_pedidoSi_estadoNull(){
    //añadimos el pedido
    try{
      addPedidoPrueba();
    }catch(SQLException ex){
      fail("No se ha podido insertar el pedido de prueba");
    }
    String[] datos = {ID_PRODUCTO_PRUEBAS, ID_PEDIDO_PRUEBAS, null};
    String expResult = Codes.CODIGO_OK;
    //esperamos un código 10
    MensajeRespuesta res = gestor.addProductoEstado(datos[0], datos[1], datos[2], Mensaje.FUNCION_ADD_PRODUCTO_TO);
    String resultadoReal = res.getCode().getCode();
    //como se habrá insertado un registro, lo eliminamos
    try {
      deleteProductoDePedidoPrueba();
      deletePedidoPrueba();
    } catch (SQLException ex) {
      fail("Error en la base de datos");
    }
    
    assertEquals(expResult, resultadoReal);   
  }
  
  /**
  * Comprueba que el mensaje que retorna el método lleva un código 61 al introducir un producto que no está en la base de datos y con un estado nulo
  */
  @Test
  public void addProductoEstado_productoNo_pedidoSi_estadoNull(){
    String[] datos = {"X099", ID_PEDIDO_PRUEBAS, null};
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;
    //esperamos un código 61
    MensajeRespuesta res = gestor.addProductoEstado(datos[0], datos[1], datos[2], Mensaje.FUNCION_ADD_PRODUCTO_TO);
    String resultadoReal = res.getCode().getCode();     
    assertEquals(expResult, resultadoReal);   
  }
  
  /**
  * Comprueba que el mensaje que retorna el método lleva un código 61 al introducir un id de pedido que no está en la base de datos y con un estado nulo
  */
  @Test
   public void addProductoEstado_productoSi_pedidoNo_estadoNull(){
    String[] datos = {ID_PRODUCTO_PRUEBAS, "pedidoError", null};
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;
    //esperamos un código 61
    MensajeRespuesta res = gestor.addProductoEstado(datos[0], datos[1], datos[2], Mensaje.FUNCION_ADD_PRODUCTO_TO);
    String resultadoReal = res.getCode().getCode();     
    assertEquals(expResult, resultadoReal);   
  }
  
  /**
  * Comprueba que el mensaje que retorna el método lleva un código 10 al introducir un conjunto de datos correcto (id de producto y de pedido que existen en la BD) y con un estado no nulo
  */
  @Test
  public void addProductoEstado_productoSi_pedidoSi_estadoSi(){
    //añadimos pedido de prueba
    try{
      addPedidoPrueba();
    }catch(SQLException ex){
      fail("No se ha podido insertar el pedido de prueba");
    }
     String[] datos = {ID_PRODUCTO_PRUEBAS, ID_PEDIDO_PRUEBAS, "pendiente"};
    String expResult = Codes.CODIGO_OK;
    //esperamos un código 10
    MensajeRespuesta res = gestor.addProductoEstado(datos[0], datos[1], datos[2], Mensaje.FUNCION_ADD_PRODUCTO_TO);
    String resultadoReal = res.getCode().getCode();
    //como se habrá insertado un registro, lo eliminamos
    try {
      deleteProductoDePedidoPrueba();
      deletePedidoPrueba();
    } catch (SQLException ex) {
      fail("Error en la base de datos");
    }
    assertEquals(expResult, resultadoReal);   
  }
  
  /**
  * Comprueba que el mensaje que retorna el método lleva un código 61 al introducir un producto que no está en la base de datos y con un estado no nulo
  */
  @Test
  public void addProductoEstado_productoNo_pedidoSi_estadoSi(){
    String[] datos = {"X099", ID_PEDIDO_PRUEBAS, "pendiente"};
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;
    //esperamos un código 61
    MensajeRespuesta res = gestor.addProductoEstado(datos[0], datos[1], datos[2], Mensaje.FUNCION_ADD_PRODUCTO_TO);
    String resultadoReal = res.getCode().getCode();     
    assertEquals(expResult, resultadoReal);   
  }
  
   /**
  * Comprueba que el mensaje que retorna el método lleva un código 61 al introducir un id de pedido que no está en la base de datos y con un estado no nulo
  */
  @Test
  public void addProductoEstado_productoSi_pedidoNo_estadoSi(){
    String[] datos = {ID_PRODUCTO_PRUEBAS, "pedidoError", "pendiente"};
    String expResult = Codes.CODIGO_ERR_PK_NOT_FOUND;
    //esperamos un código 61
    MensajeRespuesta res = gestor.addProductoEstado(datos[0], datos[1], datos[2], Mensaje.FUNCION_ADD_PRODUCTO_TO);
    String resultadoReal = res.getCode().getCode();     
    assertEquals(expResult, resultadoReal);   
  }
  
  /*  
  **********************************
  DELETE PRODUCTO FROM
  *********************************/
  
  /**
   * Comprueba que funciona el método cuando se le pasa un id de registro concreto   
   */
  @Test
  public void deleteProductoFrom_id_ok(){
    System.out.println("\n**********************\nPrueba DELETE PRODUCTO EN PEDIDO POR ID\n");
    MensajeRespuesta res=null;
    String expResult = Codes.CODIGO_OK;  
    try {
      //necesitamos insertar un producto estado en un pedido de prueba
      addPedidoPrueba();
      addProductoPrueba();
      addProductoEnPedidoPrueba();
      //ahora comprobamos que todo funciona bien
      res = gestor.deleteProductoEstado(ID_REG_PROD_PRUEBAS, null, null, null, Mensaje.FUNCION_DELETE_PRODUCTO_FROM_ID);           
      if (res.getCode().getCode().equals(Codes.CODIGO_OK)){
        System.out.println("intentando eliminar el registro creado");
        deleteProductoPrueba();
      }
    } catch (SQLException ex) {     
      System.out.print(ex.getMessage());
    }finally{
      try{
         deleteProductoPrueba();
         deletePedidoPrueba();
      }catch (SQLException ex){
        fail("error al eliminar los elementos");
      }
    }
    assertEquals(expResult, res.getCode().getCode());
  }
  
    /**
   * Comprueba que funciona el método cuando se le pasa un id de registro concreto   
   */
  @Test
  public void deleteProductoFrom_detalles_ok(){
    System.out.println("\n**********************\nPrueba DELETE PRODUCTO EN PEDIDO POR DETALLES\n");
    MensajeRespuesta res=null;
    String expResult = Codes.CODIGO_OK;  
    try {
      //necesitamos insertar un producto estado en un pedido de prueba
      addPedidoPrueba();
      addProductoPrueba();
      addProductoEnPedidoPrueba();
      //ahora comprobamos que todo funciona bien
      res = gestor.deleteProductoEstado(null, ID_PROD_PRUEBAS, ID_PEDIDO_PRUEBAS, "listo", Mensaje.FUNCION_DELETE_PRODUCTO_FROM);           
      if (res.getCode().getCode().equals(Codes.CODIGO_OK)){
        System.out.println("intentando eliminar el registro creado");
        deleteProductoPrueba();
      }
    } catch (SQLException ex) {     
      System.out.print(ex.getMessage());
    }finally{
      try{
         deleteProductoPrueba();
         deletePedidoPrueba();
      }catch (SQLException ex){
        fail("error al eliminar los elementos");
      }
    }
    assertEquals(expResult, res.getCode().getCode());
  }
  
  
  
   /**********************
   UPDATE PEDIDO
   **********************/
  
/**
 * Comprueba que se actualiza un pedido correctamente y devuelve un mensaje 10 si se envía una petición con un ID correcto. 
 */
  public void updateProductoFrom_id_ok(){
    System.out.println("\n**********************\nPrueba UPDATE PRODUCTO EN PEDIDO POR ID\n");
    MensajeRespuesta res=null;
    String expResult = Codes.CODIGO_OK;  
    try {
      //necesitamos insertar un producto estado en un pedido de prueba
      addPedidoPrueba();
      addProductoPrueba();
      addProductoEnPedidoPrueba();
      //ahora comprobamos que todo funciona bien
      res = gestor.updateProductoFromPedido(ID_REG_PROD_PRUEBAS, null, null, null, "preparando", Mensaje.FUNCION_UPDATE_PRODUCTO_FROM);                        
    } catch (SQLException ex) {     
      System.out.print(ex.getMessage());
    }finally{
      try{
        deleteProductoPrueba();
        deleteProductoPrueba();
        deletePedidoPrueba();
      }catch (SQLException ex){
        fail("error al eliminar los elementos");
      }
    }
    assertEquals(expResult, res.getCode().getCode());
  }
 
  
  
 /*  
  *************
  LIST PRODUCTOS PENDIENTES
  ***************
  */
  @Test
  public void listProductosPendientes(){
    System.out.println("\n**********************\nPrueba LIST PRODUCTOS PENDIENTES\n");
    MensajeRespuesta ret = null;
    ret = gestor.listProductosPendientes();   
    String expResult = Codes.CODIGO_OK;
    assertEquals(expResult, ret.getCode().getCode());

  }
  
 /*  
  *************
   COMPROBAR PEDIDO
  ***************
  */
  
  @Test
  public void comprobarProducto_si(){
    boolean respuesta = false;
  try {
    //añadimos el producto de prueba
    addProductoPrueba();
    respuesta = gestor.comprobarProducto(ID_PROD_PRUEBAS);   
    deleteProductoPrueba();
  } catch (SQLException ex) {
    fail("no se ha podido acceder a la base de datos");
  }
  assertTrue(respuesta);
    
  }
  
   @Test
  public void comprobarProducto_no(){
  try {
    //añadimos el producto de prueba    
    boolean respuesta = gestor.comprobarProducto(ID_PEDIDO_PRUEBAS);
    assertFalse(respuesta);
  } catch (SQLException ex) {
    fail("no se ha podido acceder a la base de datos");
  }
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
    gestor.openConnection();
    Connection con = gestor.getCon();
    String sentenciaInsert = "INSERT INTO usuario (\"usuario\", \"pwd\", \"email\", \"fecha_alta\", \"fecha_modificacion\", \"isgestor\") VALUES ('Prueba', 'pruebaPass', '"+MAIL_PRUEBAS+"', '2020-11-20', '2020-11-20', false)";
    PreparedStatement stm = con.prepareStatement(sentenciaInsert);
    stm.executeUpdate();
    stm.close();    
    gestor.closeConnection();
  }
  
  /**
   * Elimina un usuario de prueba de la base de datos para poder volver a insertarlo cuando sea necesario
   * @throws SQLException SQLException si no se puede acceder a la base de datos o no se puede borrar el usuario
   */
  public void deleteUserPrueba() throws SQLException{
    gestor.openConnection();
     Connection con = gestor.getCon();
     String sentenciaDelete = "DELETE FROM usuario where email = '"+MAIL_PRUEBAS+"'";   
     PreparedStatement stm = con.prepareStatement(sentenciaDelete);
     stm.executeUpdate();
     stm.close();    
     gestor.closeConnection();
  }
  
  public void addPedidoPrueba() throws SQLException{
    gestor.openConnection();
    Connection con = gestor.getCon();    
    String sentencia = "INSERT INTO pedido VALUES ('"+ID_PEDIDO_PRUEBAS+"', '"+USER_CORRECTO+"', '"+ID_MESA_PRUEBAS+"', 0, true, '2020-11-20')";
    System.out.println("ADD_PEDIDO_PRUEBA: "+sentencia);
    PreparedStatement pstm = con.prepareStatement(sentencia);
    pstm.executeUpdate();
    pstm.close();
    gestor.closeConnection();
  }
  
  public void addProductoEnPedidoPrueba() throws SQLException{
    gestor.openConnection();
    Connection con = gestor.getCon();                 
    String sentencia = "INSERT INTO pedido_estado VALUES ('"+ID_REG_PROD_PRUEBAS+"', '"+ID_PEDIDO_PRUEBAS+"', '"+ID_PROD_PRUEBAS+"', 'listo')";
    System.out.println("ADD_PRODUCTO_A_PEDIDO_PRUEBA: "+sentencia);
    PreparedStatement pstm = con.prepareStatement(sentencia);
    pstm.executeUpdate();
    pstm.close();
    gestor.closeConnection();
  }
  
  public void deletePedidoPrueba() throws SQLException{
    gestor.openConnection();
     Connection con = gestor.getCon();
     String sentenciaDelete = "DELETE FROM pedido where id= '"+ID_PEDIDO_PRUEBAS+"'";   
     PreparedStatement stm = con.prepareStatement(sentenciaDelete);
     stm.executeUpdate();
     stm.close();    
     gestor.closeConnection();
  }
  
  public void deleteProductoDePedidoPrueba() throws SQLException{
    gestor.openConnection();
    Connection con = gestor.getCon();    
    String sentencia = "DELETE FROM pedido_estado WHERE id = (SELECT id FROM pedido_estado WHERE id_pedido = '"+ID_PEDIDO_PRUEBAS+"' order by id desc limit 1)";
    PreparedStatement pstm = con.prepareStatement(sentencia);
    pstm.executeUpdate();
    pstm.close();
    gestor.closeConnection();
  }
  
  public void addProductoPrueba() throws SQLException{
    gestor.openConnection();
    Connection con = gestor.getCon();    
    String sentencia = "INSERT INTO producto VALUES ('"+ID_PROD_PRUEBAS+"', 'Producto Prueba', null, 25, 10, 0, null, 0, null)";
    System.out.println("DELETE_PEDIDO_PRUEBA: "+sentencia);
    PreparedStatement pstm = con.prepareStatement(sentencia);    
    if (pstm.executeUpdate()>0){
      System.out.println("SENTENCIA INTRODUCIDA");
    };
    pstm.close();
    gestor.closeConnection();
  }
  
  public void deleteProductoPrueba() throws SQLException{
    gestor.openConnection();
    Connection con = gestor.getCon();    
    String sentencia = "DELETE FROM producto WHERE id = '"+ID_PROD_PRUEBAS+"'";
        System.out.println(sentencia);
    PreparedStatement pstm = con.prepareStatement(sentencia);
    if (pstm.executeUpdate()>0){
      System.out.println("Eliminando registro");
    };
    pstm.close();
    gestor.closeConnection();
  }
}
