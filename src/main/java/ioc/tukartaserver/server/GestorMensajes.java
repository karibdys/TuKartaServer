/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.gestorDB.GestorDB;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.security.GestorSesion;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase que gestionará los envíos de los mensajes del servidor
 * @author Manu Mora
 */
public class GestorMensajes {


private boolean endServer =false;
private static final int PORT =200;
private static final String NO_SESSION = "NO SESSION";
private static final String SERVER ="SERVER: ";

private ServerSocket ss;
private Socket cs;
private BufferedReader in;
private PrintStream  out;
private GestorDB gestorDB;
private GestorSesion sesiones;
private Codes code = null;
private Gson gson;

private MensajeRespuesta respuesta;
private String mensajeOut;
private MensajeSolicitud solicitud;
private String mensajeIn;



/**
 * Se encarga de gestionar el proceso de login. Requiere un usuario y conecta con la base de datos para comprobar que el usuario existe y tiene permisos
 * @param usuario Usuario con, al menos, los datos de email y pass del usuario que intenta hacer login
 * @param isGestor un boolean que indica si se está solicitando un login de gestor o de empleado
 */
public void processLogin(Usuario usuario, boolean isGestor){
  System.out.println(SERVER+"Accediendo a la base de datos.");
  //llamamos a la base de datos que nos devolverá directamente un MensajeRespuesta con los datos de la petición
  respuesta = gestorDB.loginMens(usuario.getEmail(), usuario.getPwd(), isGestor);
  code = respuesta.getCode();
  System.out.println(SERVER+"código recibido del gestor de la base de datos: "+code.getCode());
  if(code.getCode()==Codes.CODIGO_OK) {    
    //si todo es OK generamos el token      
    TokenSesion token = new TokenSesion(usuario);
    //comprobamos que el token se registra y si es así, lo mandamos
    if (sesiones.addSesion(token)){                    
      respuesta.setData(token);
      //convertimos la respuesta en JSON y la mandamos
      out.println(gson.toJson(respuesta));
      out.flush();
      System.out.println(SERVER+"mensaje de respuesta enviado");

    }else{
      System.out.println(SERVER+"sesión no añadida");
      //TODO comprobar el error de usuario no introducido en la sesión
      sendCode(code, Mensaje.FUNCION_LOGIN);
      System.out.println("GESTOR SESIÓN: ERROR EN LA SESIÓN DE USUARIO");
    }      

  }else {
    System.out.println(SERVER+"código recibido: "+code.getCode());
    sendCode(code, Mensaje.FUNCION_LOGIN);
  }
  endConnection();
}

//**************************
//Métodos auxiliares
//**************************
/**
 * Envía una petición simple sin datos adicionales, solo con el código y la petición a la que responde
 * @param codigo Codes: código de mensaje
 * @param peticion String: petición a la que responde el código
 */
public void sendCode(Codes codigo, String peticion){
  respuesta = new MensajeRespuesta(codigo, peticion);
  mensajeOut = gson.toJson(respuesta);
  System.out.println(SERVER+"Enviando mensaje: \n  -->"+mensajeOut);
  out.println(mensajeOut);
  out.flush();  
}

/**
 * Envía una petición completa, es decir, con codigo, petición de origen y datos
 * @param codigo Codes: código de respuesta
 * @param peticion String: petición a la que responde el código
 * @param data  String: datos complementarios (formato JSON)
 */
public void sendRespuesta (Codes codigo, String peticion, String data, String dataUser){
  MensajeRespuesta mensajeRes = new MensajeRespuesta(codigo, peticion, data, (String) null);
  System.out.println(SERVER+"mensaje generado:\n"+mensajeRes);
  String mensajeJson = gson.toJson(mensajeRes);
  System.out.println(SERVER+"mensaje JSON:\n"+mensajeJson);
  out.println(mensajeJson);
  out.flush();  
  System.out.println(SERVER+"mensaje enviado");
}

/**
 * Envía un mensaje de fin de sesión al cliente y cierra el canal entre ambos.
 */
public void endConnection(){
  sendCode(new Codes(Codes.END_CONNECTION), "fin conexión");
}
  
}
