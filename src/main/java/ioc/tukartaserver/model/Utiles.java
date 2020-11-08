package ioc.tukartaserver.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Manu Mora
 */
public class Utiles {

public static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

public static Date ParseDate(String fecha){
  Date date = null;
  try {
    date = formato.parse(fecha);
  }
  catch (ParseException ex) 
  {
    System.out.println(ex);
  }
  return date;
}

public static String ParseDate(Date fecha){  
  return formato.format(fecha);
}


/***************
 * CONSTRUCTORES DE MENSAJES RÁPIDOS
***************/

/**
  * Construyen un mensaje genérico de error de usuario no encontrado
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 42 y la petición a respondida
  */ 
 public static MensajeRespuesta mensajeErrorNoUser (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_NO_USER), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error producido en la Base de Datos
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 60 y la petición a respondida
  */
 public static MensajeRespuesta mensajeErrorDB(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_BD), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de petición completada con éxito
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 10 y la petición a respondida. Necesita añadir los datos
  */
 public static MensajeRespuesta mensajeOK(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_OK), peticion);  
 }
  
 
 
 
 
 /**
  * construye un mensaje genérico de error de datos incorrectos
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 40 y la petición indicada como parámetro. 
  */
 public static MensajeRespuesta mensajeCErrorDatosIncorrectos (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_DATOS_INCORRECTOS), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error de usuario no encontrado
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 42 y la petición a respondida
  */ 
 public static MensajeRespuesta mensajeCErrorNoUser (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_USER), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error producido en la Base de Datos
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 60 y la petición a respondida
  */
 public static MensajeRespuesta mensajeCErrorDB(String peticion){
   return new MensajeRespuesta(new Codes (Codes.CODIGO_ERR_BD), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de petición completada con éxito
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 10 y la petición a respondida. Necesita añadir los datos
  */
 public static MensajeRespuesta mensajeCOK(String peticion){
   return new MensajeRespuesta(new Codes (Codes.CODIGO_OK), peticion);  
 }
 
 public static MensajeRespuesta mensajeCError(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR), peticion);  
 }
 
  public static MensajeRespuesta mensajeCNoSesion(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_NO_SESION), peticion);  
 }
  
  public static MensajeRespuesta mensajeCEndConnection(){
   return new MensajeRespuesta(new Codes(Codes.END_CONNECTION), "fin de conexion");  
 }
 
  public static MensajeRespuesta mensajeCErrorPWD(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_PWD), peticion);  
 }

  public static MensajeRespuesta mensajeCErrorBaja(String peticion) {
    return new MensajeRespuesta(new Codes(Codes.CODIGO_ERROR_USER_BAJA), peticion);  
  }
  
}
