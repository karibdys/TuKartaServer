package ioc.tukartaserver.server;

/**
 * Clase central del servidor. Se encarga de abrir canales y recibir peticiones. Delega sus funciones en los gestores y controladores de los demás sistemas como la base de datos, el envío de mensajes y demás. 
 * @author Manu Mora
 */

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.security.GestorSesion;
import ioc.tukartaserver.model.TokenSesion;


public class Server extends Thread{

private boolean endServer =false;
private static final int PORT =200;
private ConexionCliente cc;
private static final String SERVER ="SERVER: ";

private ServerSocket ss;
private Socket cs;
private BufferedReader in;
private PrintStream  out;
private GestorSesion sesiones;
private GestorServer gestorMensajes;

private Gson gson;
private MensajeSolicitud solicitud;
private String mensajeIn;

/**
 * Constructor básico del servidor. Crea el canal de escucha en el sistema, el gestor de sesiones, el de la base de datos y el builder de JSON
 */
public Server() {
  try {
    ss= new ServerSocket(PORT);     
    gson = new Gson();    
   
  } catch (IOException e) {
    System.out.println(SERVER+"ERROR AL CREAR EL SERVER: "+e.getMessage());
    gestorMensajes.sendRespuesta(new Codes(Codes.CODIGO_ERR), "conexión", null, null);
  }
  System.out.println(SERVER+"creado el server para escuchar el puerto "+PORT);
}

/**
 * Inicia el servidor para que empiece a funcionar y establece el funcionamiento básico cuando recibe peticiones. 
 */
public void startServer() {
  System.out.println(SERVER+"Iniciando server...");  
  mensajeIn="";
  while (!endServer) {    
    try {
      cc=new ConexionCliente(ss.accept());
      cc.start();
    } catch (IOException ex) {
      System.out.println("EEEEERROR");
    }
  }

  System.out.println(SERVER+"... iniciando cierre del servidor");
  try {
    closeServer();
  } catch (IOException ex) {
    //ToDo: único error que no controla un código o envío de respuesta
    System.err.println(ex.getMessage());
  }
  System.out.println(SERVER+"Fin de la conexión");
}






//**************************
//Métodos auxiliares
//**************************
public boolean comprobarSesion(TokenSesion token){
  boolean ret = false;
  if(sesiones.isToken(token.getToken())){
    ret =true;
  }
  return ret;
}

/**
 * Envía un mensaje de fin de sesión al cliente y cierra el canal entre ambos.
 */
public void endConnection(){
  gestorMensajes.sendCode(new Codes(Codes.END_CONNECTION), "fin conexión");
}

public void closeServer() throws IOException {
  endServer=true;
  ss.close();
}

@Override
public void run(){
  startServer();
}

}