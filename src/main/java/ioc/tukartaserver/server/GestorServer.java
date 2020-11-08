package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.gestorDB.GestorDB;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.security.GestorSesion;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.sql.SQLException;


/**
 * Clase que gestiona la recepción de los mensajes de petición del cliente y prepara su devolución. 
 * @author Manu Mora
 */
public class GestorServer {

private static final String SERVER ="GESTOR SERVER: ";

private PrintStream out;
private BufferedReader in;
private GestorDB gestorDB;
private GestorSesion gestorSesion;
private Codes code = null;
private Gson gson;

private MensajeRespuesta respuesta;

/******************
 * CONSTRUCTOR  
 ******************
 */

/**
 * Único constructor de la clase GestorServer. Necessita los canales de entrada y de salida para poder funcionar. 
 * @param in BufferedReader que gestiona la entrada de mensajes en el Socket del servidor desde el cliente
 * @param out PrintStream que gestiona la salida de mensajes del Socket del servidor hacia el cliente
 */
public GestorServer(BufferedReader in, PrintStream out, GestorSesion gestorsesion){
  this.in=in;
  this.out=out;
  gestorSesion=gestorsesion;
  try{
    gestorDB = new GestorDB();
  } catch (SQLException ex) {
    respuesta = new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_BD), "conexión");
    sendMensaje(respuesta);
    System.out.println("GESTOR DB: ERROR AL CONECTAR CON LA BASE DE DATOS");
  } catch (ClassNotFoundException ex) {
    respuesta = new MensajeRespuesta(new Codes(Codes.CODIGO_ERR), "conexión");
    sendMensaje(respuesta);
    System.out.println(ex.getMessage());
  }
  gson = new Gson();
  if(in==null || out==null){
    System.out.println(SERVER+"LOS CANALES NO SE HAN ABIERTO CORRECTAMENTE");
  }
}

/******************
 * GETTERS
 ******************
 */
  


/******************
 * SETTERS
 ******************
 */

/**
 * Establece un Gestor de sesiones distinto al original 
 * @param gestorSesion GestorSesion
 */
public void setGestorSesion(GestorSesion gestorSesion) {
  this.gestorSesion = gestorSesion;
}

/**
 * Establece un canal de entrada distinto al original
 * @param in BufferedReader 
 */
public void setIn(BufferedReader in) {
  this.in = in;
}

/**
 * Establece un canal del salida distinto al original creado por el constructor. 
 * @param out PrintStream
 */
public void setOut(PrintStream out) {
  this.out = out;
}
  
/******************
 * MÉTODOS AUXILIARES 
 ******************
 */

/**
 * Se encarga de gestionar el proceso de login. Requiere un usuario y el tipo de permisos exigido. Conecta con la base de datos para comprobar que el usuario existe y tiene los permisos indicados
 * @param usuario Usuario con, al menos, los datos de email y pass del usuario que intenta hacer login
 * @param isGestor un boolean que indica si se está solicitando un login de gestor o de empleado
 * @return Devuele un mensaje de tipo MensajeRespuesta con la información solicitada. Si ha habido algún error, el mensaje solo contendrá el código de error y la petición solicitada. Si es correcta, devolverá un codigo 10, un token de sesión y los datos del usuario
 */
public MensajeRespuesta processMensajeLogin(Usuario usuario, boolean isGestor){
  respuesta =null;
  if (usuario==null||usuario.getEmail()==null || usuario.getPwd()==null){
    //mandar mensaje malo
    String pet = (isGestor)? Mensaje.FUNCION_LOGIN_ADMIN:Mensaje.FUNCION_LOGIN;
    respuesta = new MensajeRespuesta (new Codes(Codes.CODIGO_DATOS_INCORRECTOS), pet);
  }else{   
    System.out.println(SERVER+"Accediendo a la base de datos.");
    respuesta = gestorDB.loginMens(usuario.getEmail(), usuario.getPwd(), isGestor);
    code = respuesta.getCode();
    String codeString = code.getCode();    
    System.out.println(SERVER+"código recibido del gestor de la base de datos: "+codeString);
    if(codeString==Codes.CODIGO_OK) {    
      //si todo es OK generamos el token      
      TokenSesion token = new TokenSesion(usuario);
      //comprobamos que el token se registra y si es así, lo mandamos
      if (gestorSesion.addSesion(token)){                    
        respuesta.setData(token);        
      }else{
        System.out.println(SERVER+"sesión no añadida");
        //TODO comprobar el error de usuario no introducido en la sesión
        respuesta = new MensajeRespuesta(code, Mensaje.FUNCION_LOGIN);
        System.out.println("GESTOR SESIÓN: ERROR EN LA SESIÓN DE USUARIO");
      }            
    }else {
      respuesta = new MensajeRespuesta(code, respuesta.getPeticion());
    }  
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición de logout. Necesita un token de sesión para ello
 * @param token TokenSesion con la información de la sesión del usuario. 
 * @return MensajeRespuesta con la confirmación del cierre de sesión o un código de error
 */
public MensajeRespuesta procesarMensajeLogout(TokenSesion token){
  respuesta = null;
  String pet = "logout";
  //comprobamos que el token no es nulo
  if (token==null||token.getUsuario()==null || token.getToken()==null){    
    respuesta = new MensajeRespuesta (new Codes(Codes.CODIGO_DATOS_INCORRECTOS), pet);
  }else{
    //comprobamos que el token está en el listado de gestorSesion
    if (gestorSesion.removeSesion(token)){
      respuesta = new MensajeRespuesta (new Codes(Codes.CODIGO_OK), pet);
    } else{
      respuesta = new MensajeRespuesta (new Codes(Codes.CODIGO_NO_SESION), pet);      
    }
  }
  return respuesta;
}

//TODO javadoc

public MensajeRespuesta procesarMensajeDatosUsuario(TokenSesion token, String email){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_DATOS_USER);
  }else{
    //si el código es un código OK, continuamos con el proceso
    respuesta = gestorDB.selectDataUser(token.getUsuario());
    
    //TODO completar método y respuesta
  }
  return respuesta;  
}


/**
 * Envía un mensaje, ya sea de tipo respuesta o solicitud
 * @param mensaje MensajeRepuesta completo 
 */
public void sendMensaje (Mensaje mensaje){
  String mensajeJSON = gson.toJson(mensaje);
  out.println(mensajeJSON);
  out.flush();
}

/**
 * Envía un mensaje de fin de sesión al cliente y cierra el canal entre ambos.
 */
public void endConnection(){
  respuesta = new MensajeRespuesta (new Codes(Codes.END_CONNECTION), "fin conexión");
  sendMensaje(respuesta);
}


private Codes comprobarSesion(TokenSesion token){
  Codes codigoRet = null;
  //comprobamos que el token es válido
  if (token==null||token.getUsuario()==null || token.getToken()==null){    
    codigoRet = new Codes(Codes.CODIGO_DATOS_INCORRECTOS);
  }else{
    //comprobamos que el token está en el listado de gestorSesion
    if (gestorSesion.isToken(token.getUsuario())){
      codigoRet = new Codes(Codes.CODIGO_OK);
    }else{
      codigoRet = new Codes(Codes.CODIGO_NO_SESION);
    }
       
  }
  return codigoRet;
} 
}
