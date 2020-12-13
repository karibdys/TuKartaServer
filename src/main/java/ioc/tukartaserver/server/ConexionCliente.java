package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Gestor;
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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Clase que procesa los mensajes recibidos y responde en función de lo que le pide el cliente.
 * @author Manu
 */
public class ConexionCliente extends Thread {

//variables del objeto
private PrintStream out;
private BufferedReader in;
private GestorServer gestorServer;
private GestorSesion gestorSesion;
private String mensajeIn;
private MensajeSolicitud solicitud;
private MensajeRespuesta respuesta;
private GestorCrypto crypto;

//otros
private static final String CONCLI="CON_CLI: ";
private static final Gson gson = new Gson();

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
      log("Petición de cliente aceptada.");	           
      //crea el gestor de mensajes    
      gestorServer = new GestorServer(gestorSesion);       
      //Enviamos el primer mensaje de respuesta: el que acepta la conexión
      System.out.println(CONCLI+"Enviando confirmación de conexión con el cliente.");      
      MensajeRespuesta respuesta = Utiles.mensajeOK("Conexión");
      //enviamos el mensaje ENCRIPTADO
      sendMensajeCrypted(respuesta, crypto);      
      
      log("Esperando petición de cliente.");      
      mensajeIn = in.readLine();
      
    } catch (IOException ex) {     
      respuesta = Utiles.mensajeError("conexion");
      sendMensajeCrypted(respuesta, crypto);      
    } catch (SQLException ex){
      respuesta = Utiles.mensajeErrorDB("conexion");
      sendMensajeCrypted(respuesta, crypto);      
    } catch (ClassNotFoundException ex) {
      respuesta = Utiles.mensajeErrorDB("conexion");
      sendMensajeCrypted(respuesta, crypto);
  }
    
    //si no ha habido ningún fallo en la recepción, tendremos un mensaje JSON.  
    try{           
      if(mensajeIn!=null){
        System.out.println("\n==================================");
        log("Petición encriptada recibida: \n"+mensajeIn);
        solicitud = (MensajeSolicitud)crypto.decryptData(mensajeIn, Mensaje.MENSAJE_SOLICITUD);
        log("Petición recibida desencriptada: \n"+ solicitud);
        System.out.println("==================================\n");
        //solicitud = gson.fromJson(mensajeIn, MensajeSolicitud.class);
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
    //si el mensaje no es correcto porque es nulo, enviamos una respuesta errónea
    respuesta = Utiles.mensajeErrorDatosIncorrectos("sin petición accesible");
    sendMensajeCrypted(mensaje, crypto);
  }else{
    //si el mensaje existe, pasamos a sacar los datos del mismo
    String dataString = mensaje.getData();
    String tokenString = mensaje.getToken();
    Usuario userIn=null;
    Pedido pedidoIn=null;
    TokenSesion token = null;
    Restaurante restIn = null;
    String[] datosString=null;
    Mesa mesaIn=null;
    //mensaje desencriptado
    if (!mensaje.getPeticion().contains("login")){
      token = gson.fromJson(tokenString, TokenSesion.class);  
    }
    switch (mensaje.getPeticion()){
      //GESTIÓN DE LOGIN******************************
      case Mensaje.FUNCION_LOGIN:
        //sacamos los datos que, en este caso, serán Usuarios
        log("procesando login");
        userIn = gson.fromJson(dataString, Usuario.class);     
        respuesta = gestorServer.processMensajeLogin(userIn, false);        
        break;
      case Mensaje.FUNCION_LOGIN_ADMIN:
        log("procesando login_admin");
        userIn = gson.fromJson(dataString, Usuario.class);
        respuesta = gestorServer.processMensajeLogin(userIn, true);     
        break;
      case Mensaje.FUNCION_LOGOFF:
        log("procesando logout");            
        respuesta = gestorServer.procesarMensajeLogout(token); 
      
      //GESTIÓN DE USUARIOS ******************************
      case Mensaje.FUNCION_DATOS_USER:
        log("procesando petición de datos de usuario");       
        respuesta = gestorServer.procesarMensajeDatosUsuario(token, null);
        break;
      case Mensaje.FUNCION_DATOS_OTRO_USER:
        log("procesando petición de datos de otro usuario");       
        Usuario userMail = gson.fromJson(dataString, Usuario.class);        
        respuesta = gestorServer.procesarMensajeDatosUsuario(token, userMail.getEmail());
        break;
      case Mensaje.FUNCION_ADD_EMP:
        log("procesando petición de insertar datos de un usuario");        
        userIn = gson.fromJson(dataString, Empleado.class);        
        respuesta = gestorServer.procesarMensajeAddUser(token, userIn, false);
        break;
      case Mensaje.FUNCION_ADD_GESTOR:   
        log("procesando petición de insertar datos de un gestor");       
        userIn = gson.fromJson(dataString, Usuario.class);        
        respuesta = gestorServer.procesarMensajeAddUser(token, userIn, true);  ;
        break;
      case Mensaje.FUNCION_BAJA_USER:
        log("procesando petición de dar de baja un usuario");                
        userIn = gson.fromJson(dataString, Usuario.class);
        String email = userIn.getEmail();
        respuesta = gestorServer.procesarMensajeBajaUser(token, email);        
        break;
      case Mensaje.FUNCION_UPDATE_EMP:
        log("procesando petición de actualizar datos de un usuario");       
        //sacamos los datos a cambiar
        Empleado emp = gson.fromJson(dataString, Empleado.class);
        respuesta = gestorServer.procesarMensajeUpdateUser(token, emp);        
        break;
      case Mensaje.FUNCION_UPDATE_GESTOR:
        log("procesando petición de actualizar datos de un gestor");       
        //sacamos los datos a cambiar
        Usuario user = gson.fromJson(dataString, Usuario.class);
        respuesta = gestorServer.procesarMensajeUpdateUser(token, user);        
        break;
      case Mensaje.FUNCION_LIST_USERS_FROM_GESTOR:
        log("procesando petición de listar empleados asociados a un gestor");      
        //no necesita datos adicionales, los coge del token
        respuesta = gestorServer.procesarMensajeListUsersFrom(token, null, Mensaje.FUNCION_LIST_USERS_FROM_GESTOR);
        break;    
      case Mensaje.FUNCION_LIST_USERS_FROM_REST:
        log("procesando petición de listar empleados asociados a un restaurante");     
        //sacamso el id del Restaurante
        String id = gson.fromJson(dataString, Restaurante.class).getId();
        respuesta = gestorServer.procesarMensajeListUsersFrom(token, id, Mensaje.FUNCION_LIST_USERS_FROM_REST);
        break;   
      case Mensaje.FUNCION_GET_EMPLEADO_FROM_ID:                
        log("procesando petición de obtener un empleado por su ID");
        respuesta = gestorServer.procesarMensajeGetEmpleadoFromId(token, Mensaje.FUNCION_GET_EMPLEADO_FROM_ID);
        break;
        
      //GESTIÓN DE PEDIDOS************************************
      case Mensaje.FUNCION_ADD_PEDIDO:
        log("procesando petición de añadir un nuevo pedido");
        //sacamos el pedido a añadir
        pedidoIn = gson.fromJson(dataString, Pedido.class);
        respuesta = gestorServer.procesarMensajeAddPedido(token, pedidoIn);        
        break;   
      case Mensaje.FUNCION_LIST_PEDIDO_FROM_USER:
        log("procesando petición de listar pedidos de usuario");
        //no hace falta dato adicional porque lo toma del token
        respuesta = gestorServer.procesarMensajeListPedidoFrom(token, null, Mensaje.FUNCION_LIST_PEDIDO_FROM_USER);        
        break; 
      case Mensaje.FUNCION_LIST_PEDIDO_FROM_OTHER_USER:
        log("procesando petición de listar pedidos de otro usuario");
        //sacamos el string con el email
        id = gson.fromJson(dataString, Empleado.class).getEmail();
        respuesta = gestorServer.procesarMensajeListPedidoFrom(token, id, Mensaje.FUNCION_LIST_PEDIDO_FROM_USER);        
        break;   
      case Mensaje.FUNCION_DELETE_PEDIDO:
        log("procesando petición de eliminar pedido");
        String pedidoId = gson.fromJson(dataString, Pedido.class).getId();
        respuesta = gestorServer.procesarMensajeDeletePedido(token, pedidoId);
       break;
      case Mensaje.FUNCION_UPDATE_PEDIDO:
        log("procesando petición de actualizar un pedido");
        pedidoIn = gson.fromJson(dataString, Pedido.class);
        respuesta = gestorServer.procesarMensajeUpdatePedido(token, pedidoIn);
        break;
      case Mensaje.FUNCION_ADD_PRODUCTO_TO:
        log("procesando petición de añadir producto a un pedido");
        datosString = gson.fromJson(dataString, String[].class);
        respuesta = gestorServer.procesarMensajeAddProductoTo(token, datosString, Mensaje.FUNCION_ADD_PRODUCTO_TO);
        break;
      case Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_USER:
        log("procesando petición de listar pedidos completos de un usuario");
        String idEmp = gson.fromJson(dataString, Empleado.class).getEmail();
        respuesta = gestorServer.procesarMensajeListPedidoCompletoFrom(token, idEmp, Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_USER);
        break;
      case Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_ID:
        log("procesando petición de listar un pedido completo por su ID");
        String idPedido = gson.fromJson(dataString, Pedido.class).getId();
        respuesta = gestorServer.procesarMensajeListPedidoCompletoFrom(token, idPedido, Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_ID);
        break;
      case Mensaje.FUNCION_DELETE_PRODUCTO_FROM:
        log("procesando petición de eliminar un producto de un pedido");
        datosString = gson.fromJson(dataString, String[].class);
        respuesta = gestorServer.procesarMensajeDeleteProductoFrom(token, datosString, Mensaje.FUNCION_DELETE_PRODUCTO_FROM);
        break;
      case Mensaje.FUNCION_DELETE_PRODUCTO_FROM_ID:
        log("procesando petición de eliminar un producto de un pedido por su ID");
        respuesta = gestorServer.procesarMensajeDeleteProductoFrom(token, datosString, Mensaje.FUNCION_DELETE_PRODUCTO_FROM_ID);
        break;
      case Mensaje.FUNCION_UPDATE_PRODUCTO_FROM:
        log("procesando petición de actualizar un producto en un pedido");
        datosString = gson.fromJson(dataString, String[].class);
        respuesta = gestorServer.procesarMensajeUpdateProductoFrom(token, datosString, Mensaje.FUNCION_DELETE_PRODUCTO_FROM);
        break;
      case Mensaje.FUNCION_LIST_PRODUCTOS_PENDIENTES:
        log("procesando petición para listar productos pendientes de entregar en un restaurante");
        respuesta = gestorServer.procesarMensajeListProductosPendientes(token);
        break;
       case Mensaje.FUNCION_LIST_PRODUCTOS_FROM_PEDIDO_ID:
        log("procesando petición para listar productos asociados a un ID de pedido");
        String pedidoID = gson.fromJson(dataString, Pedido.class).getId();
        respuesta = gestorServer.procesarMensajeListProductosFromId(token, pedidoID, Mensaje.FUNCION_LIST_PRODUCTOS_FROM_PEDIDO_ID);
        break;
        
      //GESTIÓN DE RESTAURANTES *************************
      case Mensaje.FUNCION_ADD_RESTAURANTE:        
        log("procesando función para añadir un restaurante");
        restIn = gson.fromJson(dataString, Restaurante.class);
        respuesta = gestorServer.procesarMensajeAddRestaurante(token, restIn, Mensaje.FUNCION_ADD_RESTAURANTE);
        break;
      case Mensaje.FUNCION_LIST_RESTAURANTES:
        log("procesando función para listar restaurantes de un gestor");
        Gestor gestorIn = gson.fromJson(dataString, Gestor.class);  
        respuesta = gestorServer.procesarMensajeListRestaurante(token, gestorIn, Mensaje.FUNCION_ADD_RESTAURANTE);
        break;        
      case Mensaje.FUNCION_UPDATE_RESTAURANTE:
        log("procesando función para actualizar un restaurante de un gestor");
        restIn = gson.fromJson(dataString, Restaurante.class);
        respuesta = gestorServer.procesarMensajeUpdateRestaurante(token, restIn, Mensaje.FUNCION_ADD_RESTAURANTE);
        break;     
      
      //GESTIÓN DE MESAS ********************************
      case Mensaje.FUNCION_ADD_MESA:
        log("procesando función para añadir una mesa a un restaurante");
        mesaIn = gson.fromJson(dataString, Mesa.class);
        respuesta = gestorServer.procesarMensajeAddMesa(token, mesaIn, Mensaje.FUNCION_ADD_MESA);
        break;
      case Mensaje.FUNCION_LIST_MESAS_LIBRES:
        log("procesando petición para listar mesas sin pedidos");
        String restID = gson.fromJson(dataString, Restaurante.class).getId();
        respuesta = gestorServer.procesarMensajeListMesas(token, restID, Mensaje.FUNCION_LIST_MESAS_LIBRES);        
        break;      
      case Mensaje.FUNCION_UPDATE_MESA:
        log("procesando petición para actualizar mesas");
        mesaIn = gson.fromJson(dataString, Mesa.class);
        respuesta = gestorServer.procesarMensajeUpdateMesa(token, mesaIn, Mensaje.FUNCION_LIST_MESAS_LIBRES);        
        break; 
        
      //GESTIÓN DE PRODUCTOS
      case Mensaje.FUNCION_ADD_PRODUCTO:
        log("procesando petición para añadir un producto a la base de datos");
        Producto prodIn = gson.fromJson(dataString, Producto.class);
        respuesta = gestorServer.procesarMensajeAddProducto(token, prodIn, Mensaje.FUNCION_ADD_PRODUCTO);
        break;
      case Mensaje.FUNCION_DELETE_PRODUCTO:
        log("procesando petición para eliminar un producto de la base de datos");
        String prodId = gson.fromJson(dataString, Producto.class).getId();
        respuesta = gestorServer.procesarMensajeDeleteProducto(token, prodId, Mensaje.FUNCION_DELETE_PRODUCTO);
        break;
      case Mensaje.FUNCION_LIST_PRODUCTOS:
        log("procesando petición para listar productos de un restaurante");
        respuesta = gestorServer.procesarMensajeListProductos(token, Mensaje.FUNCION_LIST_PRODUCTOS);
        break;    
      case Mensaje.FUNCION_UPDATE_PRODUCTO:
        log("procesando petición para actualizar un producto");
        prodIn = gson.fromJson(dataString, Producto.class);
        respuesta = gestorServer.procesarMensajeUpdateProducto(token, prodIn, Mensaje.FUNCION_LIST_PRODUCTOS);
        break;  
        
      //INFORMES
      case Mensaje.FUNCION_INFORME_VENTAS:
        log("procesando petición de informe de ventas de un restaurante");
        String restId = gson.fromJson(dataString, Restaurante.class).getId();
        respuesta = gestorServer.procesarInformeVentas(token, Mensaje.FUNCION_INFORME_VENTAS, restId);
        break;
      
      //FUNCIONES NO SOPORTADAS*************
      default:    
        respuesta = Utiles.mensajeErrorFuncionNoSoportada(mensaje.getPeticion());
        sendMensajeCrypted(respuesta, crypto);        
        break;
    }     
    sendMensajeCrypted(respuesta, crypto);
    //gestorServer.sendMensaje(respuesta);  
  }  
}

/**
 * Envía un mensaje de fin de sesión y cierra el canal entre ambos.
 */
public void endConnection(){
  respuesta = Utiles.mensajeEndConnection();
  sendMensajeCrypted(respuesta, crypto);  
} 

/**
 * Método de ayuda que muestra por consola un texto determinado usando la etiqueta de la clase
 * @param texto String con el mensaje a enviar
 */
public void log(String texto){
  System.out.println(CONCLI+": "+texto);
}

/**
 * Encripta y envía un mensaje usando un Gestor de Criptografía
 * @param mensaje Mensaje a encriptar y enviar
 * @param crypto GestorCrypto que contiene los datos necesarios para hacer la encriptación
 */
public void sendMensajeCrypted (Mensaje mensaje, GestorCrypto crypto){
  System.out.println("\n*************************");
  log("mensaje original enviado: \n"+mensaje);
  String mensajeCrypto = crypto.encryptData((MensajeRespuesta)mensaje);
  log("mensaje encriptado enviado: \n"+mensajeCrypto);
  System.out.println("*************************\n");
  out.println(mensajeCrypto);
  out.flush();  
}

}
