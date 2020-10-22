package ioc.tukartaserver.server;

/**
 * Clase que gestiona el servidor y sus métodos y peticiones
 * @author Manu Mora
 */

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import ioc.tukartaserver.gestorDB.*;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.security.GestorSesion;
import ioc.tukartaserver.model.TokenSesion;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.json.JSONObject;


public class Server{

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
private MensajeSolicitud solicitud;

/**
 * Constructor básico del servidor. Crea el canal de escucha en el sistema, el gestor de sesiones, el de la base de datos y el builder de JSON
 */
public Server() {
  try {
    ss= new ServerSocket(PORT);      
    sesiones=new GestorSesion();
    gson = new Gson();
    
  } catch (IOException e) {
    System.out.println(SERVER+"ERROR AL CREAR EL SERVER: "+e.getMessage());
  }
  System.out.println(SERVER+"creado el server para escuchar el puerto "+PORT);
  try{
    gestorDB = new GestorDB();
  } catch (SQLException ex) {
    sendRespuesta(new Codes(Codes.CODIGO_ERR), "conexión", "");
  } catch (ClassNotFoundException ex) {
    sendRespuesta(new Codes(Codes.CODIGO_ERR), "conexión", "");
  }
}

/**
 * Inicia el servidor para que empiece a funcionar y establece el funcionamiento básico cuando recibe peticiones. 
 */
public void startServer() {
  System.out.println(SERVER+"Iniciando server...");  
  String mensajeIn="";
  while (!endServer) {    
    try {
      cs = ss.accept();
      System.out.println(SERVER+"Petición de cliente aceptada.");	
      //sacamos los canales
      in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
      out = new PrintStream(cs.getOutputStream()); 
      System.out.println(SERVER+"Canales abiertos.");
      System.out.println(SERVER+"Enviando confirmación de conexión con el cliente.");
      sendCode(new Codes (Codes.CODIGO_OK), "Conexión");      
      //esperamos la petición del cliente    
      System.out.println(SERVER+"Esperando petición de cliente.");
      mensajeIn = in.readLine();
    } catch (IOException ex) {
      sendCode(new Codes(Codes.CODIGO_ERR), "conexión");
      System.out.println(SERVER+"Enviando código de error");
    }                
    System.out.println(SERVER+": JSON recibido\n  -->\n"+mensajeIn);  
    MensajeSolicitud mensajeSol = gson.fromJson(mensajeIn, MensajeSolicitud.class);
    procesarPeticion(mensajeSol);
    System.out.println(SERVER+"esperando nueva petición");
  }

  System.out.println(SERVER+"... iniciando cierre del servidor");
  try {
    closeServer();
  } catch (IOException ex) {
    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
  }
  System.out.println(SERVER+"Fin de la conexión");
}

public void closeServer() throws IOException {
  //TODO configurar bien el servidor  
  ss.close();	
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
      processLogin(userIn, false);
      break;
    case Mensaje.FUNCION_LOGIN_ADMIN:
      userIn = gson.fromJson(data, Usuario.class);
      processLogin(userIn, true);
      break;
    default:
      sendCode(new Codes(Codes.CODIGO_FUNCION_ERR), mensaje.getPeticion());
      break;
  }
}
/*
public void procesarPeticion(JSONObject json) throws IOException, SQLException {		
  //sacamos la petición que pide el cliente:
  String peticion = json.getString(Mensaje.ATT_PETICION);    
  System.out.println("CLIENTE: petición de "+peticion);
  JSONObject userJson = json.getJSONObject("usuario");    
  Usuario user = new Usuario(userJson);
  System.out.println(SERVER+"email: "+user.getEmail());
  //preparación de los datos
  
  String mail=null;
  String pass=null;
  String nomUser =null;
  String nombre = null;
  String apellido = null;
  
  //derivamos según el tipo de petición
  switch (peticion){
    case Mensaje.FUNCION_LOGIN:
      //controlamos que se han introducido los datos necesarios      
      System.out.println(SERVER+"Procesando petición de login");            
      if (user.getEmail().isEmpty()){
        System.out.println(SERVER+"El user no se ha introducido");
        sendCode(new Codes(Codes.CODIGO_NO_USER));
      }else{        
        if (user.getPwd().isEmpty()){
          System.out.println(SERVER+"La pass no se ha introducido");
          sendCode(new Codes(Codes.CODIGO_NO_PASS));        
        }else{          
          processLogin(user);     
        }           
      }            
      break;  
    case Mensaje.FUNCION_SIGNIN:   
      System.out.println(SERVER+"Procesando petición de signIn");
      //comprobar datos
      
      if(user.getEmail().isEmpty()){
        sendCode(new Codes(Codes.CODIGO_NO_USER));
      }else{
        mail = userJson.getString(Usuario.EMAIL);
        if (!userJson.has(Usuario.PASSWORD)){
          sendCode(new Codes(Codes.CODIGO_NO_PASS));
        }else{
          pass=userJson.getString(Usuario.PASSWORD);
          if (!userJson.has(Usuario.NOM_USUARIO)){
            nomUser = userJson.getString(Usuario.EMAIL);      
          }else{
            nomUser= userJson.getString(Usuario.NOM_USUARIO);
            if (!userJson.has(Usuario.NOM_REAL)){
              sendCode(new Codes(Codes.CODIGO_DATOS_INCORRECTOS));
            }else{
              nombre = userJson.getString(Usuario.NOM_REAL);
              if (!userJson.has(Usuario.APE_REAL)){
                sendCode(new Codes(Codes.CODIGO_DATOS_INCORRECTOS));               
              }else{
                apellido = userJson.getString(Usuario.APE_REAL);
                processSignIn(mail, pass, nomUser, nombre, apellido);
              }
            }
          }
        }
      }
      break;
    case Mensaje.FUNCION_ALTER:
      System.out.println(SERVER+"Procesando petición de modificación de datos de usuario.");
      processModify(json);
      break;
    case Mensaje.FUNCION_LOGOFF:
      System.out.println(SERVER+"Procesando petición de finalizar sesión.");
      processLogOff(json);
      break;  
    default:
      System.out.println(SERVER+"El servidor no soporta esta función. Cerrando la conexión");
      sendCode(new Codes(Codes.CODIGO_FUNCION_ERR));
      break;
  }
  endConnection();
}
*/

/**
 * Se encarga de gestionar el proceso de login. Requiere un usuario y conecta con la base de datos para comprobar que el usuario existe y tiene permisos
 * @param usuario 
 */
public void processLogin(Usuario usuario, boolean isGestor){
  System.out.println(SERVER+"Accediendo a la base de datos.");
  code=gestorDB.login(usuario.getEmail(), usuario.getPwd(), isGestor);
  if(code.getCode()==Codes.CODIGO_OK) {    
    System.out.println(SERVER+"código recibido: "+code.getCode());
    //si todo es OK generamos el token      
    TokenSesion token = new TokenSesion(usuario);
    //comprobamos que el token se registra y si es así, lo mandamos
    if (sesiones.addSesion(token)){
      System.out.println(SERVER+"sesión añadida");
      System.out.println(SERVER+"preparando y enviando mensaje de respuesta");
      sendRespuesta (code, Mensaje.FUNCION_LOGIN, gson.toJson(token));         
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

/**
 * Se encarga de gestionar el proceso de login de un usuario administrador. Requiere un usuario y conecta con la base de datos para comprobar que el usuario existe y tiene permisos
 * @param usuario 
 */
public void processLoginAdmin(Usuario user){
  //comprobar que el usuario tiene permisos
  
}

/*
public void processSignIn(String mail, String pass, String nombreUsuario, String nombreReal, String apellidoReal) throws SQLException{
  System.out.println(SERVER+"Accediendo a la base de datos.");
  code = gestorDB.signIn(mail, pass, nombreUsuario, nombreReal, apellidoReal);    
  sendCode(code);
  endConnection();
}
*/
/*
public void processModify(JSONObject json){
  //comprobamos los datos básicos:
  String token = "";
  String userMail="";
  
  //comprobamos que el usuario ha iniciado sesión  
  if ((token=comprobarSesion(json)).equals(NO_SESSION)){
    //si no está la sesión, lanzamos un código de error
    code = new Codes(Codes.CODIGO_NO_SESION);    
  }else{
    //si la sesión existe y concuerda con un token pasado como parámetro, entonces iniciamos la petición
    userMail=sesiones.getUser(token);    
    //sacamos el parámetro a cambiar y el nuevo valor
    String param = json.getString(Mensaje.ATT_PARAM);
    String valor = json.getString(Mensaje.ATT_VALOR);
    System.out.println(SERVER+"El usuario: "+userMail+" pide el cambio de :");
    System.out.println("  -->"+param+": "+valor);
    try {
      //mandamos al gestor de la base de datos que nos haga la petición:
      code = gestorDB.modify(userMail, param, valor);
    } catch (SQLException ex) {
      code = new Codes(Codes.CODIGO_ERR);
      System.out.print(SERVER+"ERROR: "+ex.getMessage());
    }
  }
  sendCode(code);
  endConnection();  
}
*/

/*
public void procesarLogout(JSONObject json){
  //comprobamos los datos básicos:
  String token = "";
  String userMail="";
  
  //comprobamos que el usuario ha iniciado sesión  
  if ((token=comprobarSesion(json)).equals(NO_SESSION)){
    //si no está la sesión, lanzamos un código de error
    code = new Codes(Codes.CODIGO_NO_SESION);   
  }else{
    sesiones.removeSesion(token);
    code=new Codes (Codes.CODIGO_OK);
  } 
  sendCode(code);
  endConnection();  
  
}
*/
/**
 * Envía una petición simple sin datos adicionales, solo con el código y la petición a la que responde
 * @param codigo Codes: código de mensaje
 * @param peticion String: petición a la que responde el código
 */
public void sendCode(Codes codigo, String peticion){
  respuesta = new MensajeRespuesta(codigo, peticion);
  String mensajeJson = gson.toJson(respuesta);
  System.out.println(SERVER+"Enviando mensaje: \n  -->"+mensajeJson);
  out.println(mensajeJson);
  out.flush();  
}

/**
 * Envía una petición completa, es decir, con codigo, petición de origen y datos
 * @param codigo Codes: código de respuesta
 * @param peticion String: petición a la que responde el código
 * @param data  String: datos complementarios (formato JSON)
 */
public void sendRespuesta (Codes codigo, String peticion, String data){
  MensajeRespuesta mensajeRes = new MensajeRespuesta(codigo, peticion, data);
  System.out.println(SERVER+"mensaje generado:\n"+mensajeRes);
  String mensajeJson = gson.toJson(mensajeRes);
  System.out.println(SERVER+"mensaje JSON:\n"+mensajeJson);
  out.println(mensajeJson);
  out.flush();  
  System.out.println(SERVER+"mensaje enviado");
}

public String comprobarSesion(JSONObject json){  
  String token = json.getString(Mensaje.ATT_TOKEN);
  if (sesiones.isToken(token)){
    return token;
  }else{
    return NO_SESSION;
  }  
}

/**
 * Envía un mensaje de fin de sesión al cliente y cierra el canal entre ambos.
 */
public void endConnection(){
  sendCode(new Codes(Codes.END_CONNECTION), "fin conexión");
}
}