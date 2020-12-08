package ioc.tukartaserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Utiles;
import ioc.tukartaserver.security.GestorCrypto;
import ioc.tukartaserver.security.GestorSesion;

/**
 * Clase central del servidor. Se encarga de abrir canales y recibir peticiones. Delega sus funciones en los gestores y controladores de los demás sistemas como la base de datos, el envío de mensajes y demás. 
 * @author Manu Mora
 */
public class Server extends Thread{

//datos del servidor
private boolean endServer =false;
private static final int PORT =200;

//parámetros del servidor
private ConexionCliente cc;
private ServerSocket ss;
private GestorSesion sesiones;
private GestorServer gestorMensajes;
private MensajeRespuesta respuesta;
private GestorCrypto crypto;

//otros
private static final String SERVER ="SERVER: ";
private static final String PETICION = "conexion";


/**
 * Constructor básico del servidor. Crea el canal de escucha en el sistema, el gestor de sesiones, el de la base de datos, y el de seguridad 
 */
public Server() {
  try {
    ss= new ServerSocket(PORT);     
    sesiones=new GestorSesion();
    crypto = new GestorCrypto();
  } catch (IOException e) {
    log("ERROR AL CREAR EL SERVER: "+e.getMessage());
    respuesta = Utiles.mensajeError(PETICION);
    cc.sendMensajeCrypted(respuesta, crypto);
    //gestorMensajes.sendMensaje(respuesta);
  } catch (Exception ex){
    log("ERROR DE SEGURIDAD: "+ex.getMessage());
    respuesta = Utiles.mensajeErrorSeguridad("conexión");
    respuesta = new MensajeRespuesta(new Codes(Codes.CODIGO_ERR), PETICION);
    cc.sendMensajeCrypted(respuesta, crypto);
  }
  System.out.println(SERVER+"creado el server para escuchar el puerto "+PORT);
}

/**
 * Inicia el servidor para que empiece a funcionar y establece el funcionamiento básico cuando recibe peticiones. 
 */
public void startServer() {
  log("Iniciando server..."); 
  while (!endServer) {    
    try {
      System.out.println("Esperando petición de algún cliente...");
      cc=new ConexionCliente(ss.accept(), sesiones, crypto);
      cc.start();
    } catch (IOException ex) {
      System.out.println(SERVER+"PETE CERRANDO EL SERVER");
    }
  }

  log("... iniciando cierre del servidor");
  try {
    closeServer();
  } catch (IOException ex) {
    respuesta = Utiles.mensajeError("Cierre de sesióN");
    cc.sendMensajeCrypted(respuesta, crypto);
  }
  log("Fin de la conexión");
}


//**************************
//Métodos auxiliares
//**************************

/**
 * Cierra el servidor 
 * @throws IOException Si no se puede cerrar correctamente 
 */
public void closeServer() throws IOException {
  endServer=true;
  if (!ss.isClosed()){
    ss.close();
  }  
}

/**
 * Inicial la ejecución de la tarea de un servidor
 */
@Override
public void run(){
  startServer();
}


//**************************
// Getters y Setters
//**************************

/**
 * Obtiene el gestor de Sesiones del servidor
 * @return 
 */
public GestorSesion getGestorSesiones(){
  return this.sesiones;
}

public void log(String texto){
  System.out.println(SERVER+": "+texto);
}

}