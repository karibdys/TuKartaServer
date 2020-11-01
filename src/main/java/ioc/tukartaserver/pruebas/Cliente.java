package ioc.tukartaserver.pruebas;

/**
 *
 * @author Manu
 */

import com.google.gson.Gson;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Rol;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.Socket;

import org.json.JSONObject;

public class Cliente {
//accesos local
private int LOCAL_PORT =200;
private final String LOCAL_HOST = "localhost";

private final String CLIENTE="CLIENTE: ";
private Socket cs;
private PrintStream  out;	
private Gson gson;

private TokenSesion tokenUser;

BufferedReader in;

public Cliente() throws IOException{
  gson = new Gson();
  try{ 
    cs = new Socket(LOCAL_HOST, LOCAL_PORT);
  }catch (Exception e) {    
    System.out.println(CLIENTE+"ERROR CREADO EL CLIENTE: "+e.getMessage());   
  }
  System.out.println(CLIENTE+"Cliente creado.");
}

public void startClient() {
  try {
    //creamos los canales de entrada y salida:
    out= new PrintStream(cs.getOutputStream());
    in= new BufferedReader(new InputStreamReader(cs.getInputStream()));
    //leemos
    System.out.println(CLIENTE+"preparado para leer");
    String mensajeString=in.readLine();    
    System.out.println(CLIENTE+"mensaje recibido: \n  -->"+mensajeString);
    MensajeRespuesta mensajeRes = gson.fromJson(mensajeString, MensajeRespuesta.class);    
    Codes code=mensajeRes.getCode();
    System.out.println("SERVER: código "+code.getCode()); 
    
    if (code.getCode().equals(Codes.CODIGO_OK)) {
      
      //prueba de login***************************            
      System.out.println(CLIENTE+"Procediendo a hacer petición de login");  
      //creamos usuario
      //Usuario user = new Usuario("karibdys", "manuPass", "manu@tukarta.com", null, null, false);
      Usuario user = new Usuario("Marc", "marcPass", "marc@tukarta.com", null, null, true);    
      //emleado
      Empleado emp = new Empleado ("Manu", "manuPass", "manu@tukarta.com", null, null, user, Rol.CAMARERO);
        
      //lo convertimos en JSON
      String userJson = gson.toJson(user);
      //creamos el mensajeRes      
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LOGIN_ADMIN, userJson, (String) null);
   
      /*
     //prueba de logout***************************   
      System.out.println(CLIENTE+"Procediendo a hacer petición de logout");      
      Usuario user = new Usuario("Marc", "marcPass", "marc@tukarta.com", null, null, true);
      String userJson = gson.toJson(user);
      TokenSesion token = new TokenSesion(user);
      token.setToken("WdJpbyRyuU");
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LOGOFF, userJson, token);
  */
      //convertimos el mensajeRes en JSON
      String mensajeOutJson = gson.toJson(mensajeOut);
      System.out.println(CLIENTE+" enviando JSON\n  -->"+mensajeOutJson);
      out.println(mensajeOutJson);          
      out.flush();      
    
    }
    System.out.println(CLIENTE+"esperando al server..."); 
    String codigo="";
    do{                
      mensajeString = in.readLine();
      System.out.println(CLIENTE+"Recibiendo mensaje:");     
      System.out.println(mensajeString);
      System.out.println(CLIENTE+"Mensaje en formato Mensaje:");
      mensajeRes = gson.fromJson(mensajeString, MensajeRespuesta.class);
      codigo = mensajeRes.getCode().getCode();
      tokenUser = gson.fromJson(mensajeRes.getData(), TokenSesion.class);
      System.out.println(CLIENTE+"SACANDO TOKEN: "+tokenUser.getToken());
      if (codigo.equals(Codes.END_CONNECTION)){
        System.out.println(CLIENTE+"Cerrando conexión con el servidor");				
        closeClient();
        break;
      }else {				
        System.out.println(CLIENTE+"enviando respuesta OK");
        sendCode(new Codes(Codes.CODIGO_OK));        
      }      
    }while (codigo!=Codes.END_CONNECTION);     
    
  }catch (Exception e) {   
  }
  
  
  
}

public void closeClient() throws IOException {
  cs.close();
}

public String getCodeFromJSON(String mensaje){
  JSONObject json = new JSONObject(mensaje);
  return json.getString("codigo");
}

public void sendCode(Codes codigo){  
  JSONObject jsonCode= codigo.parseCode();
  out.println(jsonCode);
  out.flush();  
}
}

