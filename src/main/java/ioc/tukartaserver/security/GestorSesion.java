package ioc.tukartaserver.security;


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

public boolean addSesion(String user, String token){  
  int sesiones = this.sesiones.size();
  this.sesiones.put(token, user);        
  int nuevasSesiones = this.sesiones.size();
  Boolean added = false;
  if (sesiones!=nuevasSesiones){
    added=true;
  }
  return added;
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
