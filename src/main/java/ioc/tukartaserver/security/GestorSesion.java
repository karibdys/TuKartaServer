package ioc.tukartaserver.security;


import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import java.util.HashMap;

/**
 * Clase que se encarga de gestionar las sesiones abiertas en el servidor.
 * Consta de un HashMap que incluye como key los token abiertos y como valor el usuario al que se le asocia. 
 * @author Manu
 */
public class GestorSesion {

//el gestor de tokens es un HashMap con el email del usuario como key y el token como value
private HashMap<String, TokenSesion> sesiones;

private static final String GESTOR = "GESTOR SESIÓN: ";

/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor básico de un gestor de sesiones. Crea un HashMap listo para almacenar sesiones que se le lleguen al servidor con un método login
 */
public GestorSesion(){
  sesiones = new HashMap<>();
}

/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve el listado de las sesiones del gestor actual. 
 * @return HashMap <String, TokenSesion> con las sesiones. El primer parámetro es el usuario. El segundo el token. 
 */
public HashMap<String, TokenSesion> getSesion(){
  return this.sesiones;
}

/******************
 * SETTERS
 ******************
 */


/******************
 * MÉTODOS AUXILIARES
 ******************
 */

/**
 * Método que añade una sesión al listado de sesiones activas
 * @param token TokenSesion: con la información del token a añadir al listado
 * @return true: si el token se ha añadido con éxito o false en caso contrario
 */
public boolean addSesion (TokenSesion token){
  boolean ret = false;
  //comprobamos que el token no es nulo
  if (token!=null){
    try{    
      this.sesiones.put(token.getUsuario(), token);
      System.out.println(GESTOR+"sesión añadida con éxito");   
      ret = true;      
    }catch (Exception ex){
      System.out.println(GESTOR+"sesión no añadida con éxito");  
    }
  }  
  return ret;
}

/**
 * Comprueba que el usuario pasado como parámetro (String) está o no en la lista de sesiones
 * @param usuario String con el nombre del usuario
 * @return true si el usuario está y false si no
 */
public boolean isToken(String usuario){
  if (usuario!=null){
    return sesiones.containsKey(usuario);
  }else{
    System.out.println(GESTOR+"Se ha pasado un Usuario nulo para comprobar si está o no en el listado");
    return false;
  }  
}

/**
 * Comprueba que el usuario pasado como parámetro (Usuario) está o no en la lista de sesiones
 * @param usuario Usuario con los datos del usuario
 * @return true si el usuario está en la lista. False si no o si el dato no es correcto. 
 */
public boolean isToken(Usuario usuario){  
  String userString = usuario.getUsuario();  
  if (userString.isEmpty()){    
    return false;
  }else{
    return sesiones.containsKey(userString);
  }
}

/**
 * Elimina una sesión activa del listado de sesiones si se corresponden los objetos. 
 * @param token Token de sesión al que se quiere dar de baja
 */
public boolean removeSesion(TokenSesion token){  
  boolean ret = false;
  //comprobamos que el token no es nulo
  if (token==null){
    return false;
  }else{
    //comprobar que el usuario tiene sesión abierta:    
    String user = token.getUsuario();
    if(isToken(user)){
      //comprobamos que el usuario tiene la misma sesión:      
      String tokenEnviado = token.getToken();
      String tokenAlmacen = sesiones.get(user).getToken();
      System.out.println("TOKEN EN SESION: "+tokenAlmacen);
      System.out.println("TOKEN SOLICITADO: "+tokenEnviado);
                  
      if (tokenEnviado.equals(tokenAlmacen)){
        //si ambos son iguales,               
        sesiones.remove(user);
        ret = true;
      }        
    }
    return ret;
  }  
}

public boolean removeSesion(String email){
  boolean ret = false;
  //comprobamos que el token no es nulo
  if (email!=null && isToken(email)){
    //comprobamos que el usuario tiene la misma sesión:      
    sesiones.remove(email);
    ret = true;
  }
  return ret;
}  
}
