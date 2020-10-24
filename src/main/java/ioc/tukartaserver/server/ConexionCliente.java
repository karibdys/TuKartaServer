package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Usuario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Manu
 */
public class ConexionCliente extends Thread {

private Socket socket;
private Server server;
private PrintStream out;
private BufferedReader in;
private GestorServer gestorServer;

private static final Gson gson = new Gson();
private String mensajeIn;
private MensajeSolicitud solicitud;


private static final String CONCLI="CON_CLI: ";

public ConexionCliente (Socket socket) throws IOException {
  this.socket=socket;
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
      gestorServer.sendCode(new Codes (Codes.CODIGO_OK), "Conexión");                
      System.out.println(CONCLI+"Esperando petición de cliente.");
      
      //esperamos la petición del cliente  
      mensajeIn = in.readLine();
      
    } catch (IOException ex) {
      gestorServer.sendCode(new Codes(Codes.CODIGO_ERR), "conexión");
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
}

/**
 * Método que se encarga de procesar la petición que llega desde el cliente. Necesita un JSON con los atributos definidos en la documentación según la petición que solicite.
 * @param json Mensaje de petición que llega al servidor en formato JSONObject.
 * @throws IOException los datos introducidos no son válidos
 * @throws SQLException La conexión con la base de datos ha fallado
 */

public void procesarPeticion(MensajeSolicitud mensaje){  
  //sacamos el tipo de petición
  String data = mensaje.getData();
  Usuario userIn=null;
  switch (mensaje.getPeticion()){
    case Mensaje.FUNCION_LOGIN:
      //sacamos los datos que, en este caso, serán Usuarios
      userIn = gson.fromJson(data, Usuario.class);
      gestorServer.processLogin(userIn, false);
      break;
    case Mensaje.FUNCION_LOGIN_ADMIN:
      userIn = gson.fromJson(data, Usuario.class);
      gestorServer.processLogin(userIn, true);
      break;
    case Mensaje.FUNCION_FIN_SERVER:
      
    default:
      gestorServer.sendCode(new Codes(Codes.CODIGO_FUNCION_ERR), mensaje.getPeticion());      
      break;
  }    
}

/**
 * Envía un mensaje de fin de sesión al cliente y cierra el canal entre ambos.
 */
public void endConnection(){
  gestorServer.sendCode(new Codes(Codes.END_CONNECTION), "fin conexión");
} 
}
