package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.security.GestorSesion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

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
public ConexionCliente (Socket socket, GestorSesion gestorsesion) throws IOException {  
  out = new PrintStream(socket.getOutputStream());
  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
  gestorSesion=gestorsesion;
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
      //sacamos el mensaje que nos envían
      
      //Enviamos el primer mensaje de respuesta
      System.out.println(CONCLI+"Enviando confirmación de conexión con el cliente.");
      gestorServer.sendMensaje(new MensajeRespuesta (new Codes (Codes.CODIGO_OK), "Conexión"));                
      System.out.println(CONCLI+"Esperando petición de cliente.");
      
      //esperamos la petición del cliente  
      mensajeIn = in.readLine();
      
    } catch (IOException ex) {     
      gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.CODIGO_ERR), "conexión"));
      System.out.println(CONCLI+"Enviando código de error");
    }
    
    //si no ha habido ningún fallo en la recepción, tendremos un mensaje JSON.
    System.out.println(CONCLI+": JSON recibido\n  -->\n"+mensajeIn);   
    solicitud = gson.fromJson(mensajeIn, MensajeSolicitud.class);
    if(mensajeIn!=null){
      solicitud = gson.fromJson(mensajeIn, MensajeSolicitud.class);
      //procesamos la petición
      procesarPeticion(solicitud);
    }else{
      endConnection();
    }      
    endConnection();
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

public void procesarPeticion(MensajeSolicitud mensaje){  
  //sacamos el tipo de petición
  if (mensaje==null){
    gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.CODIGO_DATOS_INCORRECTOS), mensaje.getPeticion()));
  }else{
    String dataString = mensaje.getData();
    String tokenString = mensaje.getToken();
    Usuario userIn=null;
    TokenSesion token = null;
    switch (mensaje.getPeticion()){
      case Mensaje.FUNCION_LOGIN:
        //sacamos los datos que, en este caso, serán Usuarios
        System.out.println(CONCLI+"procesando login");
        userIn = gson.fromJson(dataString, Usuario.class);
        //gestorServer.processLogin(userIn, false);        
        respuesta = gestorServer.processMensajeLogin(userIn, false);
        System.out.println(CONCLI+"mensaje recibido del gestor server:\n"+respuesta);
        System.out.println(CONCLI+"enviando mensaje");
        break;
      case Mensaje.FUNCION_LOGIN_ADMIN:
        System.out.println(CONCLI+"procesando login_admin");
        userIn = gson.fromJson(dataString, Usuario.class);
        respuesta = gestorServer.processMensajeLogin(userIn, true);     
        break;
      case Mensaje.FUNCION_LOGOFF:
        System.out.println(CONCLI+"procesando logout");        
        token = gson.fromJson(tokenString, TokenSesion.class);
        respuesta = gestorServer.procesarMensajeLogout(token); 
      case Mensaje.FUNCION_DATOS_USER:
        System.out.println(CONCLI+"procesando petición de datos de usuario");
        //necesitamos el token para hacer la petición
        token = gson.fromJson(tokenString, TokenSesion.class);
        //hacemos la petición al gestorServer:
        respuesta = gestorServer.procesarMensajeDatosUsuario(token, null);
        break;
      case Mensaje.FUNCION_DATOS_OTRO_USER:
        System.out.println(CONCLI+"procesando petición de datos de usuario");
        //necesitamos el token para hacer la petición
        token = gson.fromJson(tokenString, TokenSesion.class);
        Usuario userMail = gson.fromJson(dataString, Usuario.class);
        //hacemos la petición al gestorServer:
        respuesta = gestorServer.procesarMensajeDatosUsuario(token, userMail.getEmail());
        break;
      case Mensaje.FUNCION_ADD_EMP:
        System.out.println(CONCLI+"procesando petición de insertar datos de un usuario");
        //necesitamos el token para hacer la petición
        token = gson.fromJson(tokenString, TokenSesion.class);
        userIn = gson.fromJson(dataString, Empleado.class);
        //hacemos la petición al gestorServer:
        respuesta = gestorServer.procesarMensajeAddUser(token, userIn, false);
        break;
      case Mensaje.FUNCION_ADD_GESTOR:   
        System.out.println(CONCLI+"procesando petición de insertar datos de un usuario");
        //necesitamos el token para hacer la petición
        token = gson.fromJson(tokenString, TokenSesion.class);
        userIn = gson.fromJson(dataString, Usuario.class);
        //hacemos la petición al gestorServer:
        respuesta = gestorServer.procesarMensajeAddUser(token, userIn, true);  ;
        break;
      case Mensaje.FUNCION_BAJA_USER:
        System.out.println(CONCLI+"procesando petición de insertar datos de un usuario");
        //necesitamos el token para hacer la petición
        token = gson.fromJson(tokenString, TokenSesion.class);
        //sacamos el usuario que hay que eliminar
        userIn = gson.fromJson(dataString, Usuario.class);
        String email = userIn.getEmail();
        respuesta = gestorServer.procesarMensajeBajaUser(token, email);        
        break;
      case Mensaje.FUNCION_UPDATE_EMP:
        System.out.println(CONCLI+"procesando petición de insertar datos de un usuario");
        //necesitamos el token para hacer la petición
        token = gson.fromJson(tokenString, TokenSesion.class);
        //sacamos los datos a cambiar
        Empleado emp = gson.fromJson(dataString, Empleado.class);
        respuesta = gestorServer.procesarMensajeUpdateEmp(token, emp);        
        break;
      default:    
        gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.CODIGO_FUNCION_ERR), mensaje.getPeticion()));
        break;
    }     
    gestorServer.sendMensaje(respuesta);
    System.out.println(CONCLI+"Mensaje enviado:\n"+respuesta);
  }
  
}

/**
 * Envía un mensaje de fin de sesión y cierra el canal entre ambos.
 */
public void endConnection(){
  gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.END_CONNECTION), "fin de conexión"));
} 

}
