package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Mesa;
import ioc.tukartaserver.model.Pedido;
import ioc.tukartaserver.model.Producto;
import ioc.tukartaserver.model.Restaurante;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.model.Utiles;
import ioc.tukartaserver.pruebas.pruebas;
import ioc.tukartaserver.security.GestorCrypto;
import ioc.tukartaserver.security.GestorSesion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Clase que procesa los mensajes recibidos y responde en función de lo que le pide el cliente.
 * @author Manu
 */
public class ConexionCliente extends Thread {
private PrintStream out;
private BufferedReader in;
private GestorServer gestorServer;
private GestorSesion gestorSesion;
private static final Gson gson = new Gson();
private String mensajeIn;
private MensajeSolicitud solicitud;
private MensajeRespuesta respuesta;
private GestorCrypto crypto;

private static final String CONCLI="CON_CLI: ";

/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor principal de una conexión cliente
 * @param socket Socket del servidor conectado con el cliente. Será el que mande los mensajes del servidor y reciba los del cliente.
 * @param gestorsesion GestorSesion que controla el acceso de los usuarios a la plataforma. 
 * @throws IOException Si los canales no se pueden recuperar o son nulos. 
 */
public ConexionCliente (Socket socket, GestorSesion gestorsesion, GestorCrypto gCrypto) throws IOException {  
  out = new PrintStream(socket.getOutputStream());
  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
  gestorSesion=gestorsesion;
  crypto = gCrypto;  
}

/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve el canal de salida del Sokect que gestiona la conexiónd del lado del servidor y manda las peticiones. 
 * @return PrintStream
 */
public PrintStream getOut() {
  return out;
}

/**
 * Devuelve el canal de entrada del Socket que gestiona la conexión del lado del servidor y recibe las peticiones.
 * @return BufferedReader
 */
public BufferedReader getIn() {
  return in;
}

/**
 * Devuelve el gestor server que se encargará de gestionar el envío de respuestas
 * @return GestorServer
 */
public GestorServer getGestorServer() {
  return gestorServer;
}

/******************
 * SETTERS
 ******************
 */

/**
 * Establece el canal de entrada del Socket que gestiona la conxeión del lado del servidor. 
 * @param in nuevo BufferedReader
 */
public void setIn(BufferedReader in) {
  this.in = in;
}

/**
 * Establece un gestor server distinto al original 
 * @param gestorServer nuevo GestorServer
 */
  public void setGestorServer(GestorServer gestorServer) {
    this.gestorServer = gestorServer;
  }

/******************
 * RUN 
 ******************
 */

/**
 * Método principal que se encarga de ejecutar la tarea que tiene asignada. En este caso, abrir los canales y procesará la petición que se le manda. 
 */
@Override
public void run(){
  try {              
      System.out.println(CONCLI+"Petición de cliente aceptada.");	           
      //crea el gestor de mensajes    
      gestorServer = new GestorServer(in, out, gestorSesion);  
      System.out.println(CONCLI+"Canales abiertos.");      
      //Enviamos el primer mensaje de respuesta
      System.out.println(CONCLI+"Enviando confirmación de conexión con el cliente.");      
      MensajeRespuesta respuesta = Utiles.mensajeOK("Conexión");
      //TODO obtener modulos
      PublicKey pubKey = crypto.getPublicKey();
      BigInteger[] datosKey = getPublicKeyData(pubKey);
      String datosKeyJSON = gson.toJson(datosKey);
      respuesta.setData(datosKeyJSON);      
      gestorServer.sendMensaje(respuesta);                
      System.out.println(CONCLI+"Esperando petición de cliente.");
      
      //esperamos la petición del cliente  
      mensajeIn = in.readLine();
      
    } catch (IOException ex) {     
      gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.CODIGO_ERR), "conexion"));
      System.out.println(CONCLI+"Enviando código de error");
    } catch (InvalidKeySpecException ex) {
      gestorServer.sendMensaje(Utiles.mensajeErrorKey("conexion"));
    } catch (NoSuchAlgorithmException ex) {
      gestorServer.sendMensaje(Utiles.mensajeErrorKey("conexion"));
    }
    
    //si no ha habido ningún fallo en la recepción, tendremos un mensaje JSON.
    System.out.println(CONCLI+": JSON recibido\n  -->\n"+mensajeIn);   
    try{      
      if(mensajeIn!=null){
        solicitud = gson.fromJson(mensajeIn, MensajeSolicitud.class);
        //procesamos la petición
        procesarPeticion(solicitud);
      }else{
        endConnection();
      }      
    }catch (Exception ex){
      System.out.println(ex.getMessage());
    }finally{
      endConnection();
    }
    
    System.out.println(CONCLI+"finalizando conexión");
}


/******************
 * MÉTODOS AUXILIARES
 ******************
 */

/**
 * Método que se encarga de procesar la petición que llega desde el cliente.Necesita un JSON con los atributos definidos en la documentación según la petición que solicite.
   * @param mensaje MensajeSolicitud un mensaje con la solicitud que se le envía desde un cliente. 
 */

public void procesarPeticion(MensajeSolicitud mensaje) throws Exception{  
  //sacamos el tipo de petición
  if (mensaje==null){
    gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.CODIGO_DATOS_INCORRECTOS), mensaje.getPeticion()));
  }else{
    String dataString = mensaje.getData();
    String tokenString = mensaje.getToken();
    Usuario userIn=null;
    Pedido pedidoIn=null;
    TokenSesion token = null;
    Restaurante restIn = null;
    String[] datosString=null;
    Mesa mesaIn=null;
    if (!mensaje.getPeticion().contains("login")){
      token = gson.fromJson(tokenString, TokenSesion.class);  
    }
    switch (mensaje.getPeticion()){
      case Mensaje.FUNCION_LOGIN:
        //sacamos los datos que, en este caso, serán Usuarios
        System.out.println(CONCLI+"procesando login");
        userIn = gson.fromJson(dataString, Usuario.class);     
        respuesta = gestorServer.processMensajeLogin(userIn, false);        
        break;
      case Mensaje.FUNCION_LOGIN_ADMIN:
        System.out.println(CONCLI+"procesando login_admin");
        userIn = gson.fromJson(dataString, Usuario.class);
        respuesta = gestorServer.processMensajeLogin(userIn, true);     
        break;
      case Mensaje.FUNCION_LOGOFF:
        System.out.println(CONCLI+"procesando logout");            
        respuesta = gestorServer.procesarMensajeLogout(token); 
      case Mensaje.FUNCION_DATOS_USER:
        System.out.println(CONCLI+"procesando petición de datos de usuario");       
        //hacemos la petición al gestorServer:
        respuesta = gestorServer.procesarMensajeDatosUsuario(token, null);
        break;
      case Mensaje.FUNCION_DATOS_OTRO_USER:
        System.out.println(CONCLI+"procesando petición de datos de usuario");       
        Usuario userMail = gson.fromJson(dataString, Usuario.class);
        //hacemos la petición al gestorServer:
        respuesta = gestorServer.procesarMensajeDatosUsuario(token, userMail.getEmail());
        break;
      case Mensaje.FUNCION_ADD_EMP:
        System.out.println(CONCLI+"procesando petición de insertar datos de un usuario");        
        userIn = gson.fromJson(dataString, Empleado.class);
        //hacemos la petición al gestorServer:
        respuesta = gestorServer.procesarMensajeAddUser(token, userIn, false);
        break;
      case Mensaje.FUNCION_ADD_GESTOR:   
        System.out.println(CONCLI+"procesando petición de insertar datos de un gestor");       
        userIn = gson.fromJson(dataString, Usuario.class);
        //hacemos la petición al gestorServer:
        respuesta = gestorServer.procesarMensajeAddUser(token, userIn, true);  ;
        break;
      case Mensaje.FUNCION_BAJA_USER:
        System.out.println(CONCLI+"procesando petición de dar de baja un usuario");        
        //sacamos el usuario que hay que eliminar
        userIn = gson.fromJson(dataString, Usuario.class);
        String email = userIn.getEmail();
        respuesta = gestorServer.procesarMensajeBajaUser(token, email);        
        break;
      case Mensaje.FUNCION_UPDATE_EMP:
        System.out.println(CONCLI+"procesando petición de actualizar datos de un usuario");       
        //sacamos los datos a cambiar
        Empleado emp = gson.fromJson(dataString, Empleado.class);
        respuesta = gestorServer.procesarMensajeUpdateUser(token, emp);        
        break;
      case Mensaje.FUNCION_UPDATE_GESTOR:
        System.out.println(CONCLI+"procesando petición de actualizar datos de un gestor");       
        //sacamos los datos a cambiar
        Usuario user = gson.fromJson(dataString, Usuario.class);
        respuesta = gestorServer.procesarMensajeUpdateUser(token, user);        
        break;
      case Mensaje.FUNCION_LIST_USERS_FROM_GESTOR:
        System.out.println(CONCLI+"procesando petición de listar empleados asociados a un gestor");      
        //no necesita datos adicionales, los coge del token
        respuesta = gestorServer.procesarMensajeListUsersFrom(token, null, Mensaje.FUNCION_LIST_USERS_FROM_GESTOR);
        break;    
      case Mensaje.FUNCION_LIST_USERS_FROM_REST:
        System.out.println(CONCLI+"procesando petición de listar empleados asociados a un restaurante");     
        //sacamso el id del Restaurante
        String id = gson.fromJson(dataString, Restaurante.class).getId();
        respuesta = gestorServer.procesarMensajeListUsersFrom(token, id, Mensaje.FUNCION_LIST_USERS_FROM_REST);
        break;   
      case Mensaje.FUNCION_GET_EMPLEADO_FROM_ID:                
        respuesta = gestorServer.procesarMensajeGetEmpleadoFromId(token, Mensaje.FUNCION_GET_EMPLEADO_FROM_ID);
        break;
      case Mensaje.FUNCION_ADD_PEDIDO:
        System.out.println(CONCLI+"procesando petición de añadir un nuevo pedido");
        //sacamos el pedido a añadir
        pedidoIn = gson.fromJson(dataString, Pedido.class);
        respuesta = gestorServer.procesarMensajeAddPedido(token, pedidoIn);        
        break;   
      case Mensaje.FUNCION_LIST_PEDIDO_FROM_USER:
        System.out.println(CONCLI+"procesando petición de listar pedidos de usuario");
        //no hace falta dato adicional porque lo toma del token
        respuesta = gestorServer.procesarMensajeListPedidoFrom(token, null, Mensaje.FUNCION_LIST_PEDIDO_FROM_USER);        
        break; 
      case Mensaje.FUNCION_LIST_PEDIDO_FROM_OTHER_USER:
        System.out.println(CONCLI+"procesando petición de listar pedidos de usuario");
        //sacamos el string con el email
        id = gson.fromJson(dataString, Empleado.class).getEmail();
        respuesta = gestorServer.procesarMensajeListPedidoFrom(token, id, Mensaje.FUNCION_LIST_PEDIDO_FROM_USER);        
        break;   
      case Mensaje.FUNCION_DELETE_PEDIDO:
        System.out.println(CONCLI+"procesando petición de eliminar pedido");
        String pedidoId = gson.fromJson(dataString, Pedido.class).getId();
        respuesta = gestorServer.procesarMensajeDeletePedido(token, pedidoId);
       break;
      case Mensaje.FUNCION_UPDATE_PEDIDO:
        System.out.println(CONCLI+"procesando petición de actualizar un pedido");
        pedidoIn = gson.fromJson(dataString, Pedido.class);
        respuesta = gestorServer.procesarMensajeUpdatePedido(token, pedidoIn);
        break;
      case Mensaje.FUNCION_ADD_PRODUCTO_TO:
        System.out.println(CONCLI+"procesando petición de añadir producto a un pedido");
        datosString = gson.fromJson(dataString, String[].class);
        respuesta = gestorServer.procesarMensajeAddProductoTo(token, datosString, Mensaje.FUNCION_ADD_PRODUCTO_TO);
        break;
      case Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_USER:
        String idEmp = gson.fromJson(dataString, Empleado.class).getEmail();
        respuesta = gestorServer.procesarMensajeListPedidoCompletoFrom(token, idEmp, Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_USER);
        break;
      case Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_ID:
        String idPedido = gson.fromJson(dataString, Pedido.class).getId();
        respuesta = gestorServer.procesarMensajeListPedidoCompletoFrom(token, idPedido, Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_ID);
        break;
      case Mensaje.FUNCION_DELETE_PRODUCTO_FROM:
        datosString = gson.fromJson(dataString, String[].class);
        respuesta = gestorServer.procesarMensajeDeleteProductoFrom(token, datosString, Mensaje.FUNCION_DELETE_PRODUCTO_FROM);
        break;
      case Mensaje.FUNCION_DELETE_PRODUCTO_FROM_ID:
        //como solo debería venir en mensaje, no hacen falta más transformaciones
        respuesta = gestorServer.procesarMensajeDeleteProductoFrom(token, datosString, Mensaje.FUNCION_DELETE_PRODUCTO_FROM_ID);
        break;
      case Mensaje.FUNCION_UPDATE_PRODUCTO_FROM:
        datosString = gson.fromJson(dataString, String[].class);
        respuesta = gestorServer.procesarMensajeUpdateProductoFrom(token, datosString, Mensaje.FUNCION_DELETE_PRODUCTO_FROM);
        break;
      case Mensaje.FUNCION_LIST_PRODUCTOS_PENDIENTES:
        respuesta = gestorServer.procesarMensajeListProductosPendientes(token);
        break;
      case Mensaje.FUNCION_ADD_RESTAURANTE:        
        restIn = gson.fromJson(dataString, Restaurante.class);
        respuesta = gestorServer.procesarMensajeAddRestaurante(token, restIn, Mensaje.FUNCION_ADD_RESTAURANTE);
        break;
      case Mensaje.FUNCION_ADD_MESA:
        mesaIn = gson.fromJson(dataString, Mesa.class);
        respuesta = gestorServer.procesarMensajeAddMesa(token, mesaIn, Mensaje.FUNCION_ADD_MESA);
        break;
      case Mensaje.FUNCION_ADD_PRODUCTO:
        Producto prodIn = gson.fromJson(dataString, Producto.class);
        respuesta = gestorServer.procesarMensajeAddProducto(token, prodIn, Mensaje.FUNCION_ADD_PRODUCTO);
        break;
      case Mensaje.FUNCION_DELETE_PRODUCTO:
        String prodId = gson.fromJson(dataString, Producto.class).getId();
        respuesta = gestorServer.procesarMensajeDeleteProducto(token, prodId, Mensaje.FUNCION_DELETE_PRODUCTO);
        break;
      case Mensaje.FUNCION_LIST_PRODUCTOS:
        //no necesita parámetros, solamente el token de sesion
        respuesta = gestorServer.procesarMensajeListProductos(token, Mensaje.FUNCION_LIST_PRODUCTOS);
        break;
      case Mensaje.FUNCION_LIST_PRODUCTOS_FROM_PEDIDO_ID:
        String pedidoID = gson.fromJson(dataString, Pedido.class).getId();
        respuesta = gestorServer.procesarMensajeListProductosFromId(token, pedidoID, Mensaje.FUNCION_LIST_PRODUCTOS_FROM_PEDIDO_ID);
        break;
      case Mensaje.FUNCION_LIST_MESAS_LIBRES:
        String restID = gson.fromJson(dataString, Restaurante.class).getId();
        respuesta = gestorServer.procesarMensajeListMesas(token, restID, Mensaje.FUNCION_LIST_MESAS_LIBRES);        
        break;

      default:    
        gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.CODIGO_FUNCION_ERR), mensaje.getPeticion()));
        break;
    }     
    gestorServer.sendMensaje(respuesta);
    System.out.println(CONCLI+"Mensaje enviado:\n"+respuesta);
  }
  
}


public static BigInteger[] getPublicKeyData (PublicKey pKey) throws InvalidKeySpecException, NoSuchAlgorithmException{
  BigInteger[] datos = new BigInteger[2];
  KeyFactory fact = KeyFactory.getInstance("RSA");
  RSAPublicKeySpec pub = fact.getKeySpec(pKey ,RSAPublicKeySpec.class);   
  datos[0]= pub.getModulus();
  datos[1]= pub.getPublicExponent();
  
  return datos;
}
/**
 * Envía un mensaje de fin de sesión y cierra el canal entre ambos.
 */
public void endConnection(){
  gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.END_CONNECTION), "fin de conexión"));
} 

}
