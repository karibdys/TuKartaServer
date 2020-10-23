package ioc.tukartaserver.security;


import ioc.tukartaserver.model.TokenSesion;
import java.util.HashMap;

/**
 * Clase que se encarga de gestionar las sesiones abiertas en el servidor.
 * Consta de un HashMap que incluye como key los token abiertos y como valor el usuario al que se le asocia. 
 * @author Manu
 */
public class GestorSesion {

private HashMap<String, TokenSesion> sesiones;

private static final String GESTOR = "GESTOR SESIÓN: ";

public GestorSesion(){
  sesiones = new HashMap<>();
  System.out.println(GESTOR+"Iniciado el registro de sesiones");
}

public HashMap<String, TokenSesion> getSesion(){
  return this.sesiones;
}


/**
 * Método que añade una sesión al listado de sesiones activas
 * @param token TokenSesion: con la información del token a añadir al listado
 * @return true: si el token se ha añadido con éxito o false en caso contrario
 */
public boolean addSesion (TokenSesion token){
  boolean ret = false;
  try{
    int numSesion = sesiones.size();
    this.sesiones.put(token.getUsuario(), token);
    int numSesionAct = sesiones.size();
    if(numSesion!=numSesionAct){
      ret = true;
    }
  }catch (Exception ex){
    System.err.println(ex.getMessage());
  }
  
  
  return ret;
}

public boolean isToken(String usuario){
  return sesiones.containsKey(usuario);
}

/**
 * Elimina una sesión activa del listado de sesiones si se corresponden los objetos. 
 * @param token Token de sesión al que se quiere dar de baja
 */

public boolean removeSesion(TokenSesion token){  
  boolean ret = false;
  //comprobar que el usuario tiene sesión abierta:
  String user = token.getUsuario();
  if(isToken(user)){
    //comprobamos que el usuario tiene la misma sesión:
    TokenSesion tokenSesion = sesiones.get(user);
      if (tokenSesion==token){
        //si ambos son iguales,               
        sesiones.remove(user);
        ret = true;
      }        
  }
  return ret;
}  
}
