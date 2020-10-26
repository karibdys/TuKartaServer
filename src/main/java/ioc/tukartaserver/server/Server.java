package ioc.tukartaserver.server;

/**
 * Clase central del servidor. Se encarga de abrir canales y recibir peticiones. Delega sus funciones en los gestores y controladores de los demás sistemas como la base de datos, el envío de mensajes y demás. 
 * @author Manu Mora
 */

import java.io.IOException;
import java.net.ServerSocket;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.security.GestorSesion;


public class Server extends Thread{

private boolean endServer =false;
private static final int PORT =200;
private ConexionCliente cc;
private static final String SERVER ="SERVER: ";

private ServerSocket ss;
private GestorSesion sesiones;
private GestorServer gestorMensajes;
private MensajeRespuesta respuesta;

/**
 * Constructor básico del servidor. Crea el canal de escucha en el sistema, el gestor de sesiones, el de la base de datos y el builder de JSON
 */
public Server() {
  try {
    ss= new ServerSocket(PORT);       
  } catch (IOException e) {
    System.out.println(SERVER+"ERROR AL CREAR EL SERVER: "+e.getMessage());
    respuesta = new MensajeRespuesta(new Codes(Codes.CODIGO_ERR), "conexión");
    gestorMensajes.sendMensaje(respuesta);
  }
  System.out.println(SERVER+"creado el server para escuchar el puerto "+PORT);
}

/**
 * Inicia el servidor para que empiece a funcionar y establece el funcionamiento básico cuando recibe peticiones. 
 */
public void startServer() {
  System.out.println(SERVER+"Iniciando server..."); 
  while (!endServer) {    
    try {
      System.out.println("Esperando petición de algún cliente...");
      cc=new ConexionCliente(ss.accept());
      cc.start();
    } catch (IOException ex) {
      System.out.println(SERVER+"PETE CERRANDO EL SERVER");
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


public void closeServer() throws IOException {
  endServer=true;
  if (!ss.isClosed()){
    ss.close();
  }  
}

@Override
public void run(){
  startServer();
}


//**************************
// Getters y Setters
//**************************


public GestorSesion getGestorSesiones(){
  return this.sesiones;
}

}