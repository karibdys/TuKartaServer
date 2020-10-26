package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
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
private static final Gson gson = new Gson();
private String mensajeIn;
private MensajeSolicitud solicitud;
private MensajeRespuesta respuesta;


private static final String CONCLI="CON_CLI: ";

public ConexionCliente (Socket socket) throws IOException {
  
  out = new PrintStream(socket.getOutputStream());
  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
}

@Override
public void run(){
  try {              
      System.out.println(CONCLI+"Petición de cliente aceptada.");	
           
      //crea el gestor de mensajes    
      gestorServer = new GestorServer(in, out);  
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

/**
 * Método que se encarga de procesar la petición que llega desde el cliente.Necesita un JSON con los atributos definidos en la documentación según la petición que solicite.
   * @param mensaje MensajeSolicitud un mensaje con la solicitud que se le envía desde un cliente. 
 */

public void procesarPeticion(MensajeSolicitud mensaje){  
  //sacamos el tipo de petición
  String dataString = mensaje.getData();
  String tokenString = mensaje.getToken();
  Usuario userIn=null;
  switch (mensaje.getPeticion()){
    case Mensaje.FUNCION_LOGIN:
      //sacamos los datos que, en este caso, serán Usuarios
      userIn = gson.fromJson(dataString, Usuario.class);
      //gestorServer.processLogin(userIn, false);
      respuesta = gestorServer.processMensajeLogin(userIn, false);
      gestorServer.sendMensaje(respuesta);
      break;
    case Mensaje.FUNCION_LOGIN_ADMIN:
      userIn = gson.fromJson(dataString, Usuario.class);
      respuesta = gestorServer.processMensajeLogin(userIn, true);
      gestorServer.sendMensaje(respuesta);      
      break;
    case Mensaje.FUNCION_LOGOFF:
      TokenSesion token = gson.fromJson(tokenString, TokenSesion.class);
      respuesta = gestorServer.procesarMensajeLogout(token);      
      gestorServer.sendMensaje(respuesta);
    default:    
      gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.CODIGO_FUNCION_ERR), mensaje.getPeticion()));
      break;
  }    
}

/**
 * Envía un mensaje de fin de sesión y cierra el canal entre ambos.
 */
public void endConnection(){
  gestorServer.sendMensaje(new MensajeRespuesta (new Codes(Codes.END_CONNECTION), "fin de conexión"));
} 
}
