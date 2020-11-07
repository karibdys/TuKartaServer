package ioc.tukartaserver.model;

import ioc.tukartaserver.model.Usuario;
import java.time.LocalTime;
import java.util.Random;

/**
 * Clase que representa un Token de Sesión
 * @author Manu
 */
public class TokenSesion {

//minutos válidos que tendrá la sesión como máximo
private static final long MIN_MAX = 30;

private String token;
private String usuario;
private LocalTime validez;


/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor principal de la clase. Crea un Token a partir de un usuario con un token generado aleatoriamente
 * @param user Usuario al que se vinculará el token
 */
public TokenSesion (Usuario user){
  this.usuario=user.getEmail();
  this.token=generateToken();
  //this.validez= validezHasta(MIN_MAX);
  this.validez=validezHasta(MIN_MAX);
}

/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve el parámetro que representa la sesión abierta
 * @return String de 10 caracteres
 */
public String getToken(){
  return this.token;
}

/**
 * Devuelve el email del usuario vinculado a esta sesión
 * @return String email del usuario
 */
public String getUsuario(){
  return this.usuario;
}

/**
 * Devuelve la fecha y hora máxima hasta que la sesión será aceptada
 * @return 
 */
public LocalTime getValidez(){
  return this.validez;
}

/******************
 * SETTERS
 ******************
 */

/**
 * Establece la cadena que representa una sesión abierta
 * @param token String: cadena de 10 caracteres que representa una sesión
 */
public void setToken(String token) {
  this.token = token;
}

/**
 * Establece el email del usuario al que se vinculará la sesión abierta
 * @param usuario String email del usuario que recibe la sesión
 */
public void setUsuario(String usuario) {
  this.usuario = usuario;
}

/**
 * Establece la validez máxima por la que la sesión estará abierta
 * @param validez LocalTime tiempo hasta que se aceptará la sesión como válida
 */
public void setValidez(LocalTime validez) {
  this.validez = validez;
}

/******************
 * MÉTODOS AUXILIARES
 ******************
 */

/**
 * Genera una cadena de caracteres que representará una sesión en el gestor de sesiones. 
 * @return String cadena de 10 caracteres que representa una sesión
 */
private static String generateToken(){
  StringBuilder token=new StringBuilder(10);
  
  char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  Random random = new Random();
  for (int i = 0; i < 10; i++) {
    char c = chars[random.nextInt(chars.length)];
    token.append(c);
  }
  
  return token.toString();
}

/**
 * Establece una validez máxima a partir del momento actual. Recibe una cantidad de minutos que se añadirán a la hora en la que se pide el método y devolverá un LocalTime con esos minutos añadidos. 
 * @param minutos long minutos a añadir a la hora actual
 * @return LocalTime hasta la que el token será válido
 */
public static LocalTime validezHasta(long minutos){
  LocalTime hora= LocalTime.now().plusMinutes(minutos);
  return hora;
}

/**
 * Devuelve un String con la representación escrita de un TokenSesion
 * @return String
 */
@Override
public String toString(){
  StringBuilder builder = new StringBuilder();
  builder.append("Token: "+this.token);
  builder.append("\nUser: "+this.usuario);
  builder.append("\nValidez: "+this.validez);
  return builder.toString();
}

}
