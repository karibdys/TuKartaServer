package ioc.tukartaserver.model;

import com.google.gson.Gson;

/**
 * Clase que representa un mensaje de solicitud hacia una de las aplicaciones. Inlcuye un String con la petición a relalizar (codificada en la interfaz Mensaje), el token de sesión (solo el string) en caso de ser necesario y los datos necesarios para procesar la petición. 
 * @author Manu Mora y David Domenech
 */
public class MensajeSolicitud implements Mensaje{
private String peticion;
private String token;
private String data;
private static Gson gson = new Gson();

/**
 * Constructor básico de la clase. No incluye ni petición ni datos ni token.
 */
public MensajeSolicitud(){  
}

/**
 * Constructor que permite componer un mensaje a través de tres datos en formato String.
 * @param funcion un String con la función a solicitar. Es una constante codificada en la interfaz Mensajes
 * @param objetoJson  los datos necesarios para poder procesar la petición (sin contar con el Token) en formato JSON
 * @param token el String que representa la sesión del usuario
 */
public MensajeSolicitud(String funcion, String objetoJson, String token){
  this.peticion=funcion;
  this.data=objetoJson;
  this.token= token;
}


/**
 * Constructor que permite componer un mensaje a través de tres objetos compuestos sin convertir previamente a JSON
 * @param funcion un String con la función a solicitar. Es una constante codificada en la interfaz Mensajes
 * @param objetoJson  los datos necesarios para poder procesar la petición (sin contar con el Token) en su formato original
 * @param token un objeto de la clase TokenSesion que representa la sesión del usuario. 
 */
public MensajeSolicitud(String function, Object data, TokenSesion token){
  this.peticion=function;
  this.data = gson.toJson(data);
  this.token=gson.toJson(token);
}
/**
 * Devuelve la petición del mensaje
 * @return un string con la petición del mensaje
 */
public String getPeticion() {
  return peticion;
}

/**
 * Devuelve los datos del mensaje en formato JSON
 * @return un string en formato JSON de los datos del mensaje
 */
public String getData() {
  return data;
}

/**
 * Devuelve el token de sesión vinculado a este mensaje de petición
 * @return un string con el token de la sesión 
 */
public String getToken() {
  return token;
}

/**
 * Permite establecer el token de la sesión indicando el String
 * @param token String con la cadena de caracteres que representa el token
 */
public void setToken(String token) {
  this.token = token;
}

/**
 * Permite establecer el token de la sesión a través de un objeto TokenSesion
 * @param token un objeto TokenSesion con todos los datos de la sesión del usuario
 */
public void setToken (TokenSesion token){
  this.token = token.getToken();
}

/**
 * Permite establecer la función a solicitar o la petición 
 * @param peticion String con la petición a realizar. Está codificada en la interfaz Mensaje
 */
public void setPeticion(String peticion) {
  this.peticion = peticion;
}

/**
 * Permite establecer los datos de la solicitud a través de un String con formato JSON
 * @param data String con formato JSON que contiene los datos necesarios para completar la solicitud
 */
public void setData(String data) {
  this.data = data;
}

/**
 * Permite establecer los datos de la solicitud incluyendo directamente el objeto a mandar en la petición
 * @param data Objeto a enviar en la petición
 */
public void setData(Object data){
  this.data = gson.toJson(data);
} 

/**
 * Devuelve un String con los datos básicos del mensaje para que el programador pueda comprobar su formato
 * @return un String con los campos del Mensaje en formato JSON
 */
@Override
public String toString(){
  StringBuilder builder = new StringBuilder();
  builder.append("FUNCIÓN: ").append(this.peticion);
  builder.append("\nOBJETO: ").append(this.data);
  if (token!=null){
    builder.append("\nTOKEN: "+this.data);
  }    
  return builder.toString();
}
}
