package ioc.tukartaserver.model;

import com.google.gson.Gson;

/**
 * Clase que representa un mensaje de respuesta del cliente o del servidor al otro. 
 * @author Manu Mora y David Domenech
 */
public class MensajeRespuesta implements Mensaje{
private static Gson gson = new Gson();

private Codes code;
private String peticion;
private String data;
private String dataUser;

/******************
 * CONSTRUCTOR
 ******************
 */
/**
 * Constructor básico de un MensajeRespuesta
 */
public MensajeRespuesta(){  
}

/**
 * Constructor de la clase que recibe un mensaje y una petición respondida. No añade datos extras. 
 * @param code Codes con la respuesta a la petición solicitada
 * @param peticion String con la petición a la que responde el mensaje
 */
public MensajeRespuesta(Codes code, String peticion){
  this.code=code;
  this.peticion=peticion;
}

/**
 * Constructor de la clase que recibe un mensaje y una petición respondida. No añade datos extras. 
 * @param code Codes con la respuesta a la petición solicitada
 * @param peticion String con la petición a la que responde el mensaje
 * @param data String con los datos, en formato JSON, que acompañan al mensaje
 */
public MensajeRespuesta(Codes code, String peticion, String data){
  this.code=code;
  this.peticion=peticion;
  this.data=data;  
}

/**
 * Constructor de la clase que recibe un mensaje y una petición respondida. No añade datos extras. 
 * @param code Codes con la respuesta a la petición solicitada
 * @param peticion String con la petición a la que responde el mensaje
 * @param data Object de cualquier tipo que debe de ir en el mensaje. El constructor lo convertirá en un String en formato JSON
 */
public MensajeRespuesta(Codes code, String peticion, Object data){
  gson = new Gson();
  this.code=code;
  this.peticion=peticion;
  this.data = gson.toJson(data); 
}

/**
 * Constructor de la clase que recibe un mensaje y una petición respondida. No añade datos extras. 
 * @param code Codes con la respuesta a la petición solicitada
 * @param peticion String con la petición a la que responde el mensaje
 * @param data String con los datos, en formato JSON, que acompañan al mensaje
 * @param dataUser String con los datos del usuario en formato JSON
 */
public MensajeRespuesta(Codes code, String peticion, String data, String dataUser){
  this.code=code;
  this.peticion=peticion;
  this.data=data;
  this.dataUser=dataUser;
}

/**
 * Constructor de la clase que recibe un mensaje y una petición respondida. No añade datos extras. 
 * @param code Codes con la respuesta a la petición solicitada
 * @param peticion String con la petición a la que responde el mensaje
 * @param data Object de cualquier tipo que debe de ir en el mensaje. El constructor lo convertirá en un String en formato JSON
 * @param user Usuario datos del usaurio necesarios para mandar el mensaje en formato Usuario. El propio método lo convierte en un String con formato JSON
 */
public MensajeRespuesta(Codes code, String peticion, Object data, Usuario user){
  gson = new Gson();
  this.code=code;
  this.peticion=peticion;
  this.data = gson.toJson(data);
  this.dataUser=gson.toJson(user);
}

/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve el código con la respuesta del mensaje
 * @return Codes código que contiene lo ocurrido al procesar la petición
 */
public Codes getCode() {
  return code;
}

/**
 * Devuelve la petición original que se responde en este mensaje
 * @return String petición a la que se responde
 */
public String getPeticion() {
  return peticion;
}

/**
 * Devuelve los datos vinculados a la respuesta
 * @return String datos en formato JSON con la respuesta del servidor. Puede ser nulo
 */
public String getData() {
  return data;
}

/******************
 * SETTERS
 ******************
 */

/**
 * Establece el código del mensaje. Explica lo ocurrido con la petición. 
 * @param code Code con el código de respuesta del mensaje
 */
public void setCode(Codes code) {
  this.code = code;
}

/**
 * Establece la petición que se responde en el mensaje
 * @param peticion String petición que se responde
 */
public void setPeticion(String peticion) {
  this.peticion = peticion;
}

/**
 * Establece los datos que se enviarán en el mensaje de respuesta en formato String JSON
 * @param data String en formato JSON con los datos que se responden. 
 */
public void setData(String data) {
  this.data = data;
}

/**
 * Establece los datos que se enviarán en el mensaje de respuesta en bruto (como un objeto)
 * @param data Object un objeto que representa los datos de respuesta del mensaje. 
 */
public void setData (Object data){
  this.data = gson.toJson(data);
}
  
/**
 * Establece los datos del usuario que se enviarán en el mensaje de respuesta en formato String JSON
 * @param dataUser String en formato JSON de un usuario
 */
public void setDataUser(String dataUser) {
  this.dataUser = dataUser;
}
  
/**
 * Establece los datos del usuario que se enviarán en el mensaje de respuesta en formato Usuario. 
 * @param user Usuarui un usuario vinculado a la petición solicitada. 
 */
  public void setDataUser (Usuario user){
    this.data = gson.toJson(user);
  }
    
  

/******************
 * SETTERS
 ******************
 */
  
/**
 * Devuelve una representación en String de un MensajeRespuesta
 * @return String
 */  
@Override
public String toString(){
  StringBuilder builder = new StringBuilder();
  builder.append("------\nMENSAJE:");
  builder.append("\nCódigo: "+code.getCode()+"\nMensaje: "+code.getMessage());
  builder.append("\nPetición respondida: "+peticion);
  builder.append("\nDatos: "+data);
  if(dataUser!=null){
    builder.append("\nUsuario: "+dataUser);
  }    
  builder.append("\n------");
  return builder.toString();
}
}
