package ioc.tukartaserver.security;


import ioc.tukartaserver.model.TokenSesion;
import java.util.HashMap;
import java.util.Random;

/**
 * Clase que se encarga de gestionar las sesiones abiertas en el servidor.
 * Consta de un HashMap que incluye como key los token abiertos y como valor el usuario al que se le asocia. 
 * @author Manu
 */
public class GestorSesion {

private HashMap<String, String> sesiones;

private static final String GESTOR = "GESTOR SESIÓN: ";

public GestorSesion(){
  sesiones = new HashMap<>();
  System.out.println(GESTOR+"Iniciado el registro de sesiones");
}

public HashMap<String, String> getSesion(){
  return this.sesiones;
}

public boolean addSesion (TokenSesion token){
  boolean ret = false;
  int numSesion = sesiones.size();
  this.sesiones.put(token.getToken(), token.getUsuario());
  int numSesionAct = sesiones.size();
  if(numSesion!=numSesionAct){
    ret = true;
  }
  
  return ret;
}

public void removeSesion(String token){
  sesiones.remove(token);
}

public boolean isToken(String token){
  return sesiones.containsKey(token);
}

public String getUser(String token){  
  String userMail ="";
  if (sesiones.containsKey(token)){
    userMail=sesiones.get(token);
  }
  return userMail;
}

public String generateToken(){
  StringBuilder token=new StringBuilder(10);
  int num = sesiones.size();
  char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  Random random = new Random();
  for (int i = 0; i < 10; i++) {
    char c = chars[random.nextInt(chars.length)];
    token.append(c);
  }
  token.append(num);  
  return token.toString();
}
  
}
